package ten3.lib.capability.item;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.impl.transfer.item.InventoryStorageImpl;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import ten3.lib.tile.CmTileMachine;
import ten3.lib.tile.option.FaceOption;

public class InventoryWrapperCm extends CombinedStorage<ItemVariant, SingleSlotStorage<ItemVariant>>
		implements InventoryStorage {

	Direction di;
	CmTileMachine tile;

	public InventoryWrapperCm(Direction d, CmTileMachine t) {
        super(t.inventory);
        di = d;
        tile = t;
	}

    // @NotNull
    // @Override
    // public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {

    //     if ((di == null
    //             || tile.direCheckItem(di) == FaceOption.IN
    //             || tile.direCheckItem(di) == FaceOption.BE_IN
    //             || tile.direCheckItem(di) == FaceOption.BOTH)) {
    //         if (tile.inventory.isIn(slot) && tile.inventory.isUsed(slot) && isItemValid(slot, stack)) {
    //             return super.insertItem(slot, stack, simulate);
    //         }
    //     }

    //     return stack;

    // }

    // // still check item valid(in super)
    // public ItemStack forceInsert(int slot, @NotNull ItemStack stack, boolean simulate) {
    //     if (tile.inventory.isUsed(slot)) {
    //         return super.insertItem(slot, stack, simulate);
    //     }
    //     return stack;
    // }

    // public ItemStack forceExtract(int slot, int value, boolean simulate) {
    //     if (tile.inventory.isUsed(slot)) {
    //         return super.extractItem(slot, value, simulate);
    //     }
    //     return ItemStack.EMPTY;
    // }

    // @Override
    // public boolean isItemValid(int slot, @NotNull ItemStack stack) {
    //     return getInv().canPlaceItem(slot, stack) && tile.customFitStackIn(stack, slot);
    // }

    // @NotNull
    // @Override
    // public ItemStack extractItem(int slot, int amount, boolean simulate) {

    //     amount = Math.min(tile.maxExtractItem, amount);

    //     if (di == null
    //             || tile.direCheckItem(di) == FaceOption.OUT
    //             || tile.direCheckItem(di) == FaceOption.BE_OUT
    //             || tile.direCheckItem(di) == FaceOption.BOTH) {
    //         if (tile.inventory.isExt(slot) && tile.inventory.isUsed(slot)) {// !!!!
    //             return super.extractItem(slot, amount, simulate);
    //         }
    //     }

    //     return ItemStack.EMPTY;

    // }

    // @Override
    // public Container getInv() {
    //     return tile.inventory;
    // }

	@Override
	public List<SingleSlotStorage<ItemVariant>> getSlots() {
		// TODO Auto-generated method stub
		return null;
	}

}
