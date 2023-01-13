package ten3.lib.capability.energy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import team.reborn.energy.api.EnergyStorage;
import ten3.core.machine.cable.CableTile;
import ten3.lib.capability.Finder;
import ten3.lib.tile.CmTileMachine;
import ten3.util.ExcUtil;
import ten3.util.TransferUtil;

// connect generator with machine.
// #find - return a list of all connections
public class FEStorageWayFinding extends FEStorageTile {

	public static Map<BlockPos, List<EnergyStorage>> nets = new HashMap<>();
	public static Map<BlockPos, Integer> caps = new HashMap<>();

	public static void updateNet(CableTile tile) {
		find(tile);
	}

	public FEStorageWayFinding(Direction d, CmTileMachine t) {

		super(d, t);

	}

	@Override
	public long insert(long maxAmount, TransactionContext transaction) {
		if (supportsInsertion()) {
			return listTransferTo(nets.get(tile.pos), maxAmount, transaction);
		}
		return 0;
	}

	@Override
	public long extract(long maAmount, TransactionContext transaction) {
		if (supportsExtraction()) {
			return listTransferFrom(nets.get(tile.pos), maAmount, transaction);
		}
		return 0;
	}

	@Override
	public boolean supportsInsertion() {
		return tile.checkCanRun() && tile.maxReceive > 0;
	}

	@Override
	public boolean supportsExtraction() {
		return tile.checkCanRun() && tile.maxExtract > 0;
	}

	@Override
	public long getAmount() {
		return listGetStored(nets.get(tile.pos));
	}

	@Override
	public long getCapacity() {
		return listGetStoredMax(nets.get(tile.pos));
	}

	@SuppressWarnings("all")
	private static void find(CableTile start, List<EnergyStorage> init,
			HashMap<BlockPos, BlockEntity> fouInit, Direction fr, int cap, boolean callFirst) {

		Finder.find(nets, caps, (t, d) -> EnergyTransferor.handlerOf(t, d),
				(t) -> ((CableTile) t).getCapacity(), (t) -> t instanceof CableTile, start, init,
				fouInit, fr, cap, callFirst);

	}

	private static void find(CableTile start) {
		find(start, null, null, null, Integer.MAX_VALUE, true);
	}

	public long listGetStored(List<EnergyStorage> es) {

		long total = 0;

		if (es == null)
			return 0;

		for (EnergyStorage e : es) {
			long diff = e.getAmount();
			total += diff;
		}

		return total;

	}

	public long listGetStoredMax(List<EnergyStorage> es) {

		long total = 0;

		if (es == null)
			return 0;

		for (EnergyStorage e : es) {
			long diff = e.getCapacity();
			total += diff;
		}

		return total;

	}


	public long listTransferTo(List<EnergyStorage> es, long v, TransactionContext transaction) {

		int size = getSizeCanTrsIn(es);

		if (es == null)
			return 0;
		if (size == 0)
			return 0;

		if (v < size) {
			return ExcUtil.randomInCollection(es).insert(v, transaction);
		}

		long total = 0;

		for (EnergyStorage e : es) {
			if (!e.supportsInsertion())
				continue;
			long diff = e.insert(v / size, transaction);
			total += diff;
		}

		return total;

	}

	public long listTransferFrom(List<EnergyStorage> es, long v, TransactionContext transaction) {

		int size = getSizeCanTrsOut(es);

		if (es == null)
			return 0;
		if (size == 0)
			return 0;

		if (v < size) {
			return ExcUtil.randomInCollection(es).extract(v, transaction);
		}

		long total = 0;

		for (EnergyStorage e : es) {
			if (!e.supportsExtraction())
				continue;
			long diff = TransferUtil.execute(tr -> e.extract(v / size, transaction));
			total += diff;
		}

		return total;

	}

	public int getSizeCanTrsOut(List<EnergyStorage> es) {
		int size = 0;
		if (es == null)
			return 0;
		for (EnergyStorage e : es) {
			if (e.supportsExtraction()) {
				size++;
			}
		}
		return size;
	}

	public int getSizeCanTrsIn(List<EnergyStorage> es) {
		int size = 0;
		if (es == null)
			return 0;
		for (EnergyStorage e : es) {
			if (e.supportsInsertion()) {
				size++;
			}
		}
		return size;
	}

}
