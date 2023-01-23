package ten3.lib.tile.mac;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import ten3.TConst;
import ten3.init.BlockInit;
import ten3.init.ContInit;
import ten3.init.TileInit;
import ten3.lib.wrapper.IntArrayCm;
import ten3.util.KeyUtil;

public abstract class CmTileEntity extends BlockEntity implements ExtendedScreenHandlerFactory {

	public static CmTileEntity ofType(BlockEntityType<?> type, BlockPos... pos) {
		return (CmTileEntity) type.create(pos.length > 0 ? pos[0] : BlockPos.ZERO,
				BlockInit.getBlock(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(type).getPath()).defaultBlockState());
	}

	public IntArrayCm data = ContInit.createDefaultIntArr();
	public Component component;
	public String id;

	boolean init;

	protected int globalTimer = 0;

	public CmTileEntity(String key, BlockPos pos, BlockState state) {
		super(TileInit.getType(key), pos, state);
		component = KeyUtil.translated(TConst.modid + "." + key);
		id = key;
	}

	public void rdt(CompoundTag nbt) {
	}

	public void wdt(CompoundTag nbt) {
	}

	public void load(CompoundTag nbt) {
		rdt(nbt);
		super.load(nbt);
	}

	public void saveAdditional(CompoundTag compound) {
		wdt(compound);
		super.saveAdditional(compound);
	}

	public int getTileAliveTime() {
		return globalTimer;
	}

	public void serverTick() {
		if (level == null)
			return;

		if (!level.isClientSide()) {
			globalTimer++;
			if (!init) {
				init = true;
				whenPlaceToWorld();
			}
			update();
			endTick();
		}

		updateRemote();
	}

	public void endTick() {
	}

	public void whenPlaceToWorld() {
	}

	public void updateRemote() {
	}

	public void update() {
	}

	public Component getDisplayName() {
		return component;
	}

}
