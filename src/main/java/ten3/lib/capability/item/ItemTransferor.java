package ten3.lib.capability.item;

import java.util.List;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import ten3.lib.tile.CmTileMachine;
import ten3.lib.tile.option.FaceOption;
import ten3.util.DireUtil;
import ten3.util.TransferUtil;

@SuppressWarnings("all")
public class ItemTransferor {

	CmTileMachine t;

	public ItemTransferor(CmTileMachine t) {
		this.t = t;
	}

	private BlockEntity checkTile(Direction d) {

		return checkTile(t.pos.offset(d.getNormal()));

	}

	private BlockEntity checkTile(BlockPos pos) {

		return t.getLevel().getBlockEntity(pos);

	}

	public static Storage<ItemVariant> handlerOf(BlockEntity t, Direction d) {

		return TransferUtil.getItemStorage(t, d);

	}

	public void transferTo(BlockPos p, Direction d) {

		if (FaceOption.isPassive(t.direCheckItem(d)))
			return;
		if (!FaceOption.isOut(t.direCheckItem(d)))
			return;

		BlockEntity tile = checkTile(p);
		if (tile != null) {
			Storage<ItemVariant> src = handlerOf(t, d);
			if (src == null)
				return;
			Storage<ItemVariant> dest = handlerOf(tile, DireUtil.safeOps(d));
			if (dest == null)
				return;

			srcToDest(src, dest, false);
		}

	}

	public void transferFrom(BlockPos p, Direction d) {

		if (FaceOption.isPassive(t.direCheckItem(d)))
			return;
		if (!FaceOption.isIn(t.direCheckItem(d)))
			return;

		BlockEntity tile = checkTile(p);
		if (tile != null) {
			Storage<ItemVariant> src = handlerOf(tile, DireUtil.safeOps(d));
			if (src == null)
				return;
			Storage<ItemVariant> dest = handlerOf(t, d);
			if (dest == null)
				return;

			srcToDest(src, dest, true);
		}

	}

	public void transferTo(Direction d) {

		transferTo(t.pos.offset(d.getNormal()), d);

	}

	public void transferFrom(Direction d) {

		transferFrom(t.pos.offset(d.getNormal()), d);

	}

	public static long getRemainSize(InventoryStorage src, ItemVariant variant, long amount) {
		long a = amount;

		for (int i = 0; i < src.getSlots().size(); i++) {
			var ia = a;
			a -= TransferUtil.execute(tr -> src.simulateInsert(variant, ia, tr));
			if (a <= 0)
				break;
		}

		return amount - a;

	}

	public void srcToDest(Storage<ItemVariant> srcs, Storage<ItemVariant> dests, boolean into) {
		if (srcs instanceof InventoryStorage src && dests instanceof InventoryStorage dest) {

			ItemStack s = ItemStack.EMPTY;
			int i = -1;
			while (s.isEmpty()) {
				i++;
				if (i >= src.getSlots().size())
					break;

				var ti = i;
				s = src.getSlot(i).getResource()
						.toStack(TransferUtil.execute(tr -> src.getSlot(ti).extract(src.getSlot(ti).getResource(),
								Math.min(into ? t.maxReceiveItem : t.maxExtractItem,
										getRemainSize(dest, src.getSlot(ti).getResource(),
												src.getSlot(ti).getAmount())),
								tr)).intValue());
			}

			int k = -1;
			while (true) {
				k++;
				if (k >= dest.getSlots().size())
					break;

				var kt = k;
				ItemVariant v = ItemVariant.of(s);
				var c = s.getCount();
				s.shrink(TransferUtil.execute(tr -> dest.getSlot(kt).insert(v, c, tr))
						.intValue());
				if (s.isEmpty())
					break;
			}
		}

	}

	// return : stack is completely given.
	public boolean selfGive(ItemStack stack, int from, int to) {
		if (handlerOf(t, null) instanceof InventoryStorage dest) {
			if (stack.isEmpty())
				return true;

			if (dest == null)
				return false;

			int k = from - 1;
			while (true) {
				k++;
				if (k > to)
					break;

				var slot = dest.getSlot(k);
				stack.shrink(TransferUtil.execute(tr -> slot.insert(ItemVariant.of(stack), stack.getCount(), tr))
						.intValue());
				if (stack.isEmpty())
					break;
			}

			return stack.isEmpty();
		}

		return false;
	}

	public boolean selfGive(ItemStack stack) {

		return selfGive(stack, 0, t.inventory.getContainerSize() - 1);

	}

	public boolean selfGiveList(List<ItemStack> ss) {

		boolean allReceive = true;

		for (ItemStack stack : ss) {
			if (!selfGive(stack)) {
				allReceive = false;
			}
		}

		return allReceive;

	}

	public ItemStack selfGet(int max, int from, int to) {
		if (handlerOf(t, null) instanceof InventoryStorage src) {
			if (src == null)
				return ItemStack.EMPTY;

			ItemStack s = ItemStack.EMPTY;
			int i = from - 1;
			while (s.getCount() < max || s.isEmpty()) {
				i++;
				if (i > to)
					break;

				var it = i;
				s.shrink(TransferUtil.execute(tr -> src.getSlot(it).extract(src.getSlot(it).getResource(), max, tr))
						.intValue());
			}

			return s;
		}

		return ItemStack.EMPTY;
	}

	public ItemStack selfGet(int max) {
		return selfGet(max, 0, t.inventory.getContainerSize() - 1);
	}

}
