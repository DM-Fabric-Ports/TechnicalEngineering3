package ten3.lib.tile.mac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import team.reborn.energy.api.EnergyStorage;
import ten3.TConst;
import ten3.core.network.packets.PTCInfoClientPack;
import ten3.init.ContInit;
import ten3.lib.capability.energy.BatteryTile;
import ten3.lib.capability.energy.EnergyTransferor;
import ten3.lib.capability.fluid.FluidTransferor;
import ten3.lib.capability.fluid.Tank;
import ten3.lib.capability.fluid.TankArray;
import ten3.lib.capability.item.AdvancedInventory;
import ten3.lib.capability.item.ItemTransferor;
import ten3.lib.tile.CmContainerMachine;
import ten3.lib.tile.option.FaceOption;
import ten3.lib.tile.option.RedstoneMode;
import ten3.lib.tile.option.Type;
import ten3.lib.wrapper.IntArrayCm;
import ten3.lib.wrapper.SlotCm;
import ten3.lib.wrapper.SlotUpgCm;
import ten3.util.DireUtil;
import ten3.util.ExcUtil;
import ten3.util.KeyUtil;
import ten3.util.StorageType;
import ten3.util.TransferUtil;

public abstract class CmTileMachine extends CmTileEntity {

	public EnergyTransferor etr;
	public ItemTransferor itr;
	public FluidTransferor ftr;

	public List<SlotCm> slots = new ArrayList<>();
	public AdvancedInventory inventory = new AdvancedInventory(64, slots, this);
	public List<Tank> tanks = new ArrayList<>();
	public IntArrayCm fluidData = ContInit.createDefaultIntArr();
	public IntArrayCm fluidAmount = ContInit.createDefaultIntArr();

	public static int kFE(double k) {
		return (int) (1000 * k);
	}

	// data poses.
	// how to use them:
	// data.set(energy<-index, xxx<-value)
	public static final int ENERGY = 2;
	public static final int MAX_ENERGY = 3;
	public static final int PROGRESS = 0;
	public static final int MAX_PROGRESS = 1;
	public static final int FUEL = 4;
	public static final int MAX_FUEL = 5;
	public static final int E_REC = 6;
	public static final int E_EXT = 7;
	public static final int I_REC = 8;
	public static final int I_EXT = 9;

	public static final int RED_MODE = 10;
	public static final int FACE = 11;
	public static final int EFF_AUC = 12;
	public static final int EFF = 13;
	public static final int UPGSIZE = 16;
	// public static int tranMode = 11;

	public int efficientIn;
	public int initialEfficientIn;// initial vars won't be change!

	public int initialFacing;

	public TransferManager info;
	public MacNBTManager nbtManager;
	public UpgradeSlots upgradeSlots;
	public MachineReflection reflection;

	public CmTileMachine(BlockPos pos, BlockState state) {
		super(BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath(), pos, state);

		initHandlers();
		info = new TransferManager(this);
		upgradeSlots = new UpgradeSlots(this);
		etr = new EnergyTransferor(this);
		itr = new ItemTransferor(this);
		ftr = new FluidTransferor(this);
		nbtManager = new MacNBTManager(this);
		reflection = new MachineReflection(this);

		if (typeOf() != Type.CABLE) {
			addSlot(new SlotUpgCm(inventory, 34, 32, -28));
			addSlot(new SlotUpgCm(inventory, 35, 51, -28));
			addSlot(new SlotUpgCm(inventory, 36, 70, -28));
			addSlot(new SlotUpgCm(inventory, 37, 89, -28));
			addSlot(new SlotUpgCm(inventory, 38, 108, -28));
			addSlot(new SlotUpgCm(inventory, 39, 127, -28));
		}
	}

	public List<ItemStack> drops() {
		List<ItemStack> stacks = new ArrayList<>();

		for (int i = 0; i < inventory.getContainerSize(); i++) {
			if (!canDrop(inventory.getItem(i), i))
				continue;
			stacks.add(inventory.getItem(i));
		}

		return stacks;
	}

	public void addSlot(SlotCm s) {
		slots.add(s);
	}

	public void addTank(Tank s) {
		tanks.add(s);
	}

	@Nullable
	public <T extends Recipe<Container>> T getRecipe(RecipeType<T> type, ItemStack... stacks) {
		return ExcUtil.safeGetRecipe(level, type, new SimpleContainer(stacks)).orElse(null);
	}

	@Nullable
	public <T extends Recipe<Container>> T getRecipe(RecipeType<T> type, SimpleContainer simpleContainer) {
		return ExcUtil.safeGetRecipe(level, type, simpleContainer).orElse(null);
	}

	public void setEfficiency(int eff) {
		initialEfficientIn = efficientIn = eff;
	}

	public boolean customFitStackIn(ItemStack s, int slot) {
		return true;
	}

	public abstract Type typeOf();

	public int initialFaceMode(StorageType cap) {
		return FaceOption.BOTH;
	}

	@Override
	public void whenPlaceToWorld() {
		for (Direction d : Direction.values()) {
			info.setOpenEnergy(d, initialFaceMode(StorageType.ENERGY));
			info.setOpenItem(d, initialFaceMode(StorageType.ITEM));
			info.setOpenFluid(d, initialFaceMode(StorageType.FLUID));
		}

		initialFacing = DireUtil.direToInt(reflection.direction());
	}

	public void rdt(CompoundTag nbt) {
		super.rdt(nbt);
		nbtManager.rdt(nbt);
	}

	public void wdt(CompoundTag nbt) {
		super.wdt(nbt);
		nbtManager.wdt(nbt);
	}

	double actualEffPercent;

	public MutableComponent getDisplayWith() {
		return KeyUtil.translated(TConst.modid + "." + id);
		// TConst.modid + ".level." + levelIn);
	}

	protected boolean canDrop(ItemStack stack, int slot) {
		// do not drop upgrades
		return !upgradeSlots.isUpgradeSlot(slot);
	}

	public void resetAll() {
		efficientIn = initialEfficientIn;
		info.resetAll();
	}

	int cacheTimeActive;

	public void doBaseData() {
		resetAll();
		upgradeSlots.upgradeUpdate(true);
		upgradeSlots.upgradeUpdate(false);

		component = getDisplayWith();

		// to client
		data.set(MAX_ENERGY, info.maxStorageEnergy);
		data.set(E_REC, info.maxReceiveEnergy);
		data.set(E_EXT, info.maxExtractEnergy);
		data.set(EFF, efficientIn);
		data.set(I_REC, info.maxReceiveItem);
		data.set(I_EXT, info.maxExtractItem);
		data.set(UPGSIZE, upgradeSlots.upgSize);

		data.set(FACE, initialFacing);
		reflection.setFace(DireUtil.intToDire(initialFacing));

		if (getTileAliveTime() % 20 == 0) {
			int i = 0;
			for (Tank tank : tanks) {
				long amt = tank.getAmount();
				int id = BuiltInRegistries.FLUID.getId(tank.getResource().getFluid());
				fluidAmount.set(i, (int) amt);
				fluidData.set(i, id);
				i++;
			}
		}

		// avoid out of cap
		if (data.get(ENERGY) > info.maxStorageEnergy) {
			data.set(ENERGY, info.maxStorageEnergy);
		}

		// set actual efficientIn
		if (getTileAliveTime() % 4 == 0) {
			actualEffPercent = EfficientCalculator.gen(typeOf(), data);
			data.set(EFF_AUC, (int) (actualEffPercent * efficientIn));
		}

		if (getActualEfficiencyPercent() > 0 && signalAllowRun()) {
			cacheTimeActive = 12;
		}
		cacheTimeActive--;
		reflection.setActive(cacheTimeActive > 0);

		// tran items
		if (signalAllowRun()) {
			TransferUtil.execute(tr -> {
				etr.transferEnergy(tr);
				itr.transferItem(tr);
				ftr.transferFluid(tr);
				return 0;
			});
		}

		if (getTileAliveTime() % 200 == 0) {
			PTCInfoClientPack.send(this);// 10s send a pack
		}
	}

	@Override
	public void update() {
		doBaseData();
	}

	public int getActualEfficiency() {
		return data.get(EFF_AUC);
	}

	public double getActualEfficiencyPercent() {
		return actualEffPercent;
	}

	public boolean signalAllowRun() {
		boolean power = level.hasNeighborSignal(worldPosition);

		return switch (data.get(RED_MODE)) {
			case RedstoneMode.LOW -> !power;
			case RedstoneMode.HIGH -> power;
			case RedstoneMode.OFF -> true;
			default -> false;
		};
	}

	public boolean energyAllowRun() {
		boolean checkEnergy = false;
		if (typeOf() == Type.MACHINE_PROCESS) {
			checkEnergy = data.get(ENERGY) >= efficientIn;
		}
		if (typeOf() == Type.MACHINE_EFFECT) {
			checkEnergy = data.get(ENERGY) >= efficientIn;
		}
		if (typeOf() == Type.GENERATOR) {
			checkEnergy = data.get(ENERGY) + getActualEfficiency() <= info.maxStorageEnergy;
		}
		return checkEnergy;
	}

	@Override
	public AbstractContainerMenu createMenu(int cid, Inventory pi, Player p_createMenu_3_) {
		return new CmContainerMachine(cid, id, this, pi, worldPosition, data, fluidData, fluidAmount);
	}

	public Optional<Storage<ItemVariant>> getItemStorage(Direction side) {
		if (side == null) {
			return getItemHandler(side);
		}

		return info.direCheckItem(side) != FaceOption.OFF && hasFaceCapability(StorageType.ITEM, side)
				&& signalAllowRun() ? getItemHandler(side) : Optional.empty();
	}

	public Optional<Storage<FluidVariant>> getFluidStorage(Direction side) {
		if (side == null) {
			return getFluidHandler(side);
		}

		return info.direCheckFluid(side) != FaceOption.OFF && hasFaceCapability(StorageType.FLUID, side)
				&& signalAllowRun()
						? getFluidHandler(side)
						: Optional.empty();
	}

	public Optional<EnergyStorage> getEnergyStorage(Direction side) {
		if (side == null) {
			return getEnergyHandler(side);
		}

		return info.direCheckEnergy(side) != FaceOption.OFF && hasFaceCapability(StorageType.ENERGY, side)
				&& signalAllowRun()
						? getEnergyHandler(side)
						: Optional.empty();
	}

	protected boolean hasFaceCapability(StorageType cap, Direction d) {
		return true;
	}

	public Optional<Storage<ItemVariant>> getItemHandler(Direction d) {
		if (d == null) {
			return Optional.ofNullable(handlerItemNull);
		}
		return Optional.ofNullable(handlerItem.get(d));
	}

	public Optional<EnergyStorage> getEnergyHandler(Direction d) {
		if (d == null) {
			return Optional.ofNullable(handlerEnergyNull);
		}
		return Optional.ofNullable(handlerEnergy.get(d));
	}

	public Optional<Storage<FluidVariant>> getFluidHandler(Direction d) {
		if (d == null) {
			return Optional.ofNullable(handlerFluidNull);
		}
		return Optional.ofNullable(handlerFluid.get(d));
	}

	public Map<Direction, EnergyStorage> handlerEnergy = new HashMap<>();
	public Map<Direction, InventoryStorage> handlerItem = new HashMap<>();
	public Map<Direction, Storage<FluidVariant>> handlerFluid = new HashMap<>();
	public EnergyStorage handlerEnergyNull;
	public InventoryStorage handlerItemNull;
	public Storage<FluidVariant> handlerFluidNull;

	public void initHandlers() {
		for (Direction d : Direction.values()) {
			handlerEnergy.put(d, new BatteryTile(d, this));
			handlerItem.put(d, InventoryStorage.of(this.inventory, d));
			handlerFluid.put(d, new TankArray(d, this));
		}
		handlerItemNull = InventoryStorage.of(this.inventory, null);
		handlerEnergyNull = new BatteryTile(null, this);
		handlerFluidNull = new TankArray(null, this);
	}

	@Override
	public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
		buf.writeBlockPos(getBlockPos());
	}

}
