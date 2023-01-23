package ten3.lib.capability.net;

import java.util.List;
import java.util.Set;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import team.reborn.energy.api.EnergyStorage;
import ten3.core.machine.cable.CableTile;
import ten3.lib.capability.energy.BatteryTile;
import ten3.lib.capability.energy.EnergyTransferor;
import ten3.util.ExcUtil;

//connect generator with machine.
//#find - return a list of all connections
public class BatteryWayFinding extends BatteryTile {

	public static LevelNetsManager<EnergyStorage> manager = new LevelNetsManager<>();

	public static void updateNet(CableTile tile) {
		find(tile);
	}

	private static void find(CableTile start) {
		Finder.find(manager,
				(t, d) -> EnergyTransferor.handlerOf(t, d),
				(t) -> t instanceof CableTile,
				(t) -> ((CableTile) t).getCapacity(),
				start);
	}

	private int cap() {
		return manager.getLevelNet(tile).getNet(tile.getBlockPos()).getCap((e) -> {
			if (e instanceof CableTile)
				return ((CableTile) e).getCapacity();
			return Integer.MAX_VALUE;
		});
	}

	private void hangPoses() {
		Set<BlockEntity> tiles = manager.getLevelNet(tile).getNet(tile.getBlockPos()).netPoses;
		for (BlockEntity t : tiles) {
			if (t instanceof CableTile) {
				((CableTile) t).hangUp();
			}
		}
	}

	EnergyStorage object;

	public BatteryWayFinding(Direction d, CableTile t) {

		super(d, t);
		object = ExcUtil.randomInCollection(hand());

	}

	private List<EnergyStorage> hand() {
		return manager.getLevelNet(tile).getNet(tile.getBlockPos()).elements;
	}

	@Override
	public long insert(long maxAmount, TransactionContext transaction) {
		if (supportsInsertion()) {
			return listTransferTo(hand(), maxAmount, transaction);
		}
		return 0;
	}

	@Override
	public long extract(long maxAmount, TransactionContext transaction) {
		if (supportsExtraction()) {
			return listTransferFrom(hand(), maxAmount, transaction);
		}
		return 0;
	}

	@Override
	public boolean supportsInsertion() {
		return tile.signalAllowRun();
	}

	@Override
	public boolean supportsExtraction() {
		return tile.signalAllowRun();
	}

	@Override
	public long getAmount() {
		return 0;
	}

	@Override
	public long getCapacity() {
		return tile.info.maxStorageEnergy;
	}

	public long listTransferTo(List<EnergyStorage> es, long v, TransactionContext transaction) {

		int size = getSizeCanTrsIn(es);

		if (es == null)
			return 0;
		if (size == 0)
			return 0;

		if (v < size) {
			return ExcUtil.randomInCollection(es).insert(Math.min(cap(), v), transaction);
		}

		int total = 0;

		for (EnergyStorage e : es) {
			if (!e.supportsInsertion())
				continue;
			long diff = e.insert(Math.min(cap(), v / size), transaction);
			total += diff;
		}

		if (total > 0) {
			hangPoses();
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
			return ExcUtil.randomInCollection(es).extract(Math.min(cap(), v), transaction);
		}

		int total = 0;

		for (EnergyStorage e : es) {
			if (!e.supportsExtraction())
				continue;
			long diff = e.extract(Math.min(cap(), v / size), transaction);
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
