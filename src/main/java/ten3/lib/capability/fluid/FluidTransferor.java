package ten3.lib.capability.fluid;

import java.util.Iterator;
import java.util.Queue;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import ten3.lib.tile.mac.CmTileMachine;
import ten3.lib.tile.option.FaceOption;
import ten3.util.DireUtil;
import ten3.util.RAUtil;

@SuppressWarnings("all")
public class FluidTransferor {

	CmTileMachine t;

	public FluidTransferor(CmTileMachine t) {
		this.t = t;
	}

	public final Queue<Direction> fluidQR = DireUtil.newQueueOffer();

	public void transferFluid(TransactionContext transaction) {
		// if(getTileAliveTime() % 10 == 0) {
		fluidQR.offer(fluidQR.remove());
		for (Direction d : fluidQR) {
			transferTo(d, t.info.maxExtractFluid, transaction);
			transferFrom(d, t.info.maxReceiveFluid, transaction);
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

	public static Storage<FluidVariant> handlerOf(BlockEntity t, Direction d) {
		return FluidStorage.SIDED.find(t.getLevel(), t.getBlockPos(), t.getLevel().getBlockState(t.getBlockPos()), t,
				d);
	}

	public void transferTo(BlockPos p, Direction d, long v, TransactionContext transaction) {

		if (FaceOption.isPassive(t.info.direCheckFluid(d)))
			return;
		if (!FaceOption.isOut(t.info.direCheckFluid(d)))
			return;

		BlockEntity tile = checkTile(p);
		if (tile != null) {
			Storage<FluidVariant> src = handlerOf(t, d);
			if (src == null || !src.supportsExtraction())
				return;
			Storage<FluidVariant> dest = handlerOf(tile, DireUtil.safeOps(d));
			if (dest == null || !dest.supportsInsertion())
				return;

			int min = -1;
			int i = 0;
			for (Tank tank : t.tanks) {
				if (!tank.isEmpty()) {
					min = i;
					break;
				}
				i++;
			}
			if (min == -1)
				return;

			var tat = t.tanks.get(min);
			src.extract(tat.getResource(), dest.insert(tat.getResource(), Math.min(tat.getAmount(), v), transaction),
					transaction);
		}

	}

	public void transferFrom(BlockPos p, Direction d, int v, TransactionContext transaction) {
		if (FaceOption.isPassive(t.info.direCheckFluid(d)))
			return;
		if (!FaceOption.isIn(t.info.direCheckFluid(d)))
			return;

		BlockEntity tile = checkTile(p);
		if (tile != null) {
			Storage<FluidVariant> src = handlerOf(tile, DireUtil.safeOps(d));
			if (src == null || !src.supportsExtraction())
				return;
			Storage<FluidVariant> dest = handlerOf(t, d);
			if (dest == null || !dest.supportsInsertion())
				return;

			Iterator<StorageView<FluidVariant>> iter = src.iterator();
			while (iter.hasNext()) {
				var tst = iter.next();
				if (!(tst.isResourceBlank() || tst.getAmount() <= 0)) {
					src.extract(tst.getResource(),
							dest.insert(tst.getResource(), Math.min(tst.getAmount(), v), transaction),
							transaction);
					break;
				}
			}
		}
	}

	public boolean selfGive(ResourceAmount<FluidVariant> stack, TransactionContext transaction) {
		if (RAUtil.isEmpty(stack))
			return true;
		TankArray tka = (TankArray) handlerOf(t, null);
		if (tka == null)
			return false;
		return tka.insert(stack.resource(), stack.amount(), transaction) == stack.amount();
	}

	public long selfGet(ResourceAmount<FluidVariant> stack, TransactionContext transaction) {
		TankArray tka = (TankArray) handlerOf(t, null);
		if (tka == null)
			return 0;
		return tka.extract(stack.resource(), stack.amount(), transaction);
	}

	public void transferTo(Direction d, int v, TransactionContext transaction) {
		transferTo(t.getBlockPos().offset(d.getNormal()), d, v, transaction);
	}

	public void transferFrom(Direction d, int v, TransactionContext transaction) {
		transferFrom(t.getBlockPos().offset(d.getNormal()), d, v, transaction);
	}

}
