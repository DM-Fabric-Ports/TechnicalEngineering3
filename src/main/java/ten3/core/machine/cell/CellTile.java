package ten3.core.machine.cell;

import java.util.Optional;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import team.reborn.energy.api.EnergyStorage;
import ten3.lib.tile.mac.CmTileMachine;
import ten3.lib.tile.option.Type;
import ten3.lib.wrapper.SlotCm;
import ten3.util.TransferUtil;

public class CellTile extends CmTileMachine {

	public CellTile(BlockPos pos, BlockState state) {
		super(pos, state);

		info.setCap(getCapacity());

		addSlot(new SlotCm(inventory, 0, 42, 32, null, true, true));
		addSlot(new SlotCm(inventory, 1, 115, 32, null, true, true));
	}

	@Override
	public Type typeOf() {
		return Type.CELL;
	}

	public int getCapacity() {
		return kFE(1000);
	}

	@SuppressWarnings("unused")
	public void update() {
		super.update();

		if (!signalAllowRun()) {
			return;
		}

		ItemStack stack0 = inventory.getItem(0);
		ItemStack stack1 = inventory.getItem(1);
		var storage = InventoryStorage.of(inventory, null);

		try (Transaction transaction = TransferUtil.getTransaction()) {
			if (stack0.getCount() == 1) {
				Optional.ofNullable(EnergyStorage.ITEM.find(stack0, ContainerItemContext.ofSingleSlot(getSlot(0))))
						.ifPresent(
								(e) -> {
									if (e.supportsExtraction()) {
										long diff = e.extract(Math.min(info.maxReceiveEnergy,
												info.maxStorageEnergy - data.get(ENERGY)), transaction);
										if (diff != 0) {
											data.translate(ENERGY, (int) diff);
										}
									}
								});
			}
		}

		try (Transaction transaction = TransferUtil.getTransaction()) {
			if (stack1.getCount() == 1) {
				Optional.ofNullable(EnergyStorage.ITEM.find(stack1, ContainerItemContext.ofSingleSlot(getSlot(1))))
						.ifPresent(
								(e) -> {
									if (e.supportsInsertion()) {
										long diff = e.insert(Math.min(info.maxExtractEnergy, data.get(ENERGY)),
												transaction);
										if (diff != 0) {
											data.translate(ENERGY, (int) -diff);
										}
									}
								});
			}
		}
	}

	protected SingleSlotStorage<ItemVariant> getSlot(int i) {
		return this.handlerItemNull.getSlot(i);
	}

}
