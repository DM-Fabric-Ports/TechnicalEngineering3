package ten3.lib.capability.item;

import java.util.List;
import java.util.Queue;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import ten3.lib.tile.mac.CmTileMachine;
import ten3.lib.tile.option.FaceOption;
import ten3.util.DireUtil;
import ten3.util.TransferUtil;

@SuppressWarnings("all")
public class ItemTransferor {

	CmTileMachine t;

	public ItemTransferor(CmTileMachine t) {
		this.t = t;
	}

	public final Queue<Direction> itemQR = DireUtil.newQueueOffer();

	public void transferItem(TransactionContext transaction) {
		// if(getTileAliveTime() % 10 == 0) {
		itemQR.offer(itemQR.remove());
		for (Direction d : itemQR) {
			transferTo(d, transaction);
			transferFrom(d, transaction);
			break;
		}
		// }
	}

	private BlockEntity checkTile(Direction d) {

		return checkTile(t.getBlockPos().offset(d.getNormal()));

	}

	private BlockEntity checkTile(BlockPos pos) {

		return t.getLevel().getBlockEntity(pos);

	}

	public static Storage<ItemVariant> handlerOf(BlockEntity t, Direction d) {
		return ItemStorage.SIDED.find(t.getLevel(), t.getBlockPos(), t.getLevel().getBlockState(t.getBlockPos()), t, d);
	}

	public void transferTo(BlockPos p, Direction d, TransactionContext transaction) {

		if (FaceOption.isPassive(t.info.direCheckItem(d)))
			return;
		if (!FaceOption.isOut(t.info.direCheckItem(d)))
			return;

		BlockEntity tile = checkTile(p);
		if (tile != null) {
			Storage<ItemVariant> src = handlerOf(t, d);
			if (src == null)
				return;
			Storage<ItemVariant> dest = handlerOf(tile, DireUtil.safeOps(d));
			if (dest == null)
				return;

			srcToDest(src, dest, false, transaction);
		}

	}

	public void transferFrom(BlockPos p, Direction d, TransactionContext transaction) {

		if (FaceOption.isPassive(t.info.direCheckItem(d)))
			return;
		if (!FaceOption.isIn(t.info.direCheckItem(d)))
			return;

		BlockEntity tile = checkTile(p);
		if (tile != null) {
			Storage<ItemVariant> src = handlerOf(tile, DireUtil.safeOps(d));
			if (src == null)
				return;
			Storage<ItemVariant> dest = handlerOf(t, d);
			if (dest == null)
				return;

			srcToDest(src, dest, true, transaction);
		}

	}

	public void transferTo(Direction d, TransactionContext transaction) {

		transferTo(t.getBlockPos().offset(d.getNormal()), d, transaction);

	}

	public void transferFrom(Direction d, TransactionContext transaction) {

		transferFrom(t.getBlockPos().offset(d.getNormal()), d, transaction);

	}

	// s - the extract item from src
	// return - src's max cap for <s>
	public static int getFirstSlotFit(InventoryStorage src, ItemStack s) {
		long sin = s.getCount();
		long orid = sin;
		for (int i = 0; i < src.getSlots().size(); i++) {
			var it = i;
			var sint = sin;
			sin = TransferUtil.simulateExecute(tr -> src.getSlot(it).insert(ItemVariant.of(s), sint, tr));
			if (orid > sin) {
				return i;
			}
		}
		return -1;
	}

	public void srcToDest(Storage<ItemVariant> src, Storage<ItemVariant> dest, boolean into,
			TransactionContext transaction) {
		srcToDest(-1, src, dest, into, transaction);
	}

	public void srcToDest(int init, Storage<ItemVariant> src, Storage<ItemVariant> dest, boolean into,
			TransactionContext transaction) {
		int iniSlot = init == -1 ? 0 : init;

		ItemStack stack = ItemStack.EMPTY;
		{
			var iter = src.iterator();
			while (iter.hasNext()) {
				var s = iter.next();
				if (stack.isEmpty())
					stack = s.getResource().toStack((int) src.simulateExtract(s.getResource(),
							into ? t.info.maxReceiveItem : t.info.maxExtractItem, transaction));
			}
		}

		int cache = stack.getCount();

		if (!stack.isEmpty())
			stack.shrink((int) dest.simulateInsert(ItemVariant.of(stack), stack.getCount(), transaction));

		int ins = cache - stack.getCount();
		if (ins == 0) {
			srcToDest(iniSlot + 1, src, dest, into, transaction);
		}

		ItemStack s2 = ItemStack.EMPTY;
		{
			var iter = src.iterator();
			while (iter.hasNext()) {
				var s = iter.next();
				if (s2.isEmpty())
					s2 = s.getResource().toStack((int) src.simulateExtract(s.getResource(), ins, transaction));
			}
		}

		if (!s2.isEmpty())
			s2.shrink((int) dest.insert(ItemVariant.of(s2), s2.getCount(), transaction));

	}

	// return : stack is completely given.
	public boolean selfGive(ItemStack stack, int from, int to, boolean sim) {
		if (stack.isEmpty() || !(handlerOf(t, null) instanceof InventoryStorage dest))
			return true;

		if (dest == null)
			return false;

		int k = from - 1;
		while (true) {
			k++;
			if (k > to)
				break;

			var kt = k;
			stack.shrink(sim
					? TransferUtil
							.simulateExecute(tr -> dest.getSlot(kt).insert(ItemVariant.of(stack), stack.getCount(), tr))
							.intValue()
					: TransferUtil.execute(tr -> dest.getSlot(kt).insert(ItemVariant.of(stack), stack.getCount(), tr))
							.intValue());

			if (stack.isEmpty())
				break;
		}

		return stack.isEmpty();
	}

	public boolean selfGive(ItemStack stack, boolean sim) {
		return selfGive(stack, 0, t.inventory.getContainerSize() - 1, false);
	}

	public boolean selfGiveList(List<ItemStack> ss, boolean sim) {
		boolean allReceive = true;

		for (ItemStack stack : ss) {
			if (!selfGive(stack, sim)) {
				allReceive = false;
			}
		}

		return allReceive;
	}

	public ItemStack selfGet(int max, int from, int to, boolean sim) {
		var temp = handlerOf(t, null);
		if (temp == null || !(temp instanceof InventoryStorage src))
			return ItemStack.EMPTY;

		ItemStack s = ItemStack.EMPTY;
		int i = from - 1;
		while (s.getCount() < max || s.isEmpty()) {
			i++;
			if (i > to)
				break;

			var resource = src.getSlot(i).getResource();
			var it = i;
			s = resource.toStack(
					sim ? TransferUtil.simulateExecute(tr -> src.getSlot(it).extract(resource, max, tr)).intValue()
							: TransferUtil.execute(tr -> src.getSlot(it).extract(resource, max, tr)).intValue());
		}

		return s;
	}

	public ItemStack selfGet(int max, boolean sim) {
		return selfGet(max, 0, t.inventory.getContainerSize() - 1, sim);
	}

}
