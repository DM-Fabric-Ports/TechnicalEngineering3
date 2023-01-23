package ten3.lib.capability.energy;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import team.reborn.energy.api.EnergyStorage;
import ten3.lib.tile.mac.CmTileMachine;
import ten3.lib.tile.option.FaceOption;
import ten3.util.DireUtil;

import java.util.Queue;

import static ten3.lib.tile.mac.CmTileMachine.ENERGY;

@SuppressWarnings("all")
public class EnergyTransferor {

	final CmTileMachine t;

	public EnergyTransferor(CmTileMachine t) {
		this.t = t;
	}

	public final Queue<Direction> energyQR = DireUtil.newQueueOffer();

	public void transferEnergy(TransactionContext transaction) {
		// if(getTileAliveTime() % 2 == 0) {
		energyQR.offer(energyQR.remove());
		for (Direction d : energyQR) {
			transferTo(d, t.info.maxExtractEnergy, transaction);
			transferFrom(d, t.info.maxReceiveEnergy, transaction);
		}
		// }
	}

	public static EnergyStorage handlerOf(BlockEntity e, Direction d) {
		return EnergyStorage.SIDED.find(e.getLevel(), e.getBlockPos(), e.getLevel().getBlockState(e.getBlockPos()), e,
				d);
	}

	private BlockEntity checkTile(Direction d) {
		return checkTile(t.getBlockPos().offset(d.getNormal()));
	}

	private BlockEntity checkTile(BlockPos pos) {
		return t.getLevel().getBlockEntity(pos);
	}

	public void transferTo(BlockPos p, Direction d, int v, TransactionContext transaction) {

		if (FaceOption.isPassive(t.info.direCheckEnergy(d)))
			return;
		if (!FaceOption.isOut(t.info.direCheckEnergy(d)))
			return;

		BlockEntity tile = checkTile(p);

		if (tile != null) {
			EnergyStorage e = handlerOf(tile, DireUtil.safeOps(d));
			if (e == null)
				return;
			if (e.supportsInsertion()) {
				long diff = e.insert(Math.min(v, t.data.get(ENERGY)), transaction);
				if (e.insert(Math.min(v, t.data.get(ENERGY)), transaction) != 0) {
					t.data.translate(ENERGY, (int) -diff);
				}
			}
		}

	}

	public void transferFrom(BlockPos p, Direction d, int v, TransactionContext transaction) {

		if (FaceOption.isPassive(t.info.direCheckEnergy(d)))
			return;
		if (!FaceOption.isIn(t.info.direCheckEnergy(d)))
			return;

		BlockEntity tile = checkTile(p);

		if (tile != null) {
			EnergyStorage e = handlerOf(tile, DireUtil.safeOps(d));
			if (e == null)
				return;
			if (e.supportsExtraction()) {
				long diff = e.extract(Math.min(v, t.info.maxStorageEnergy - t.data.get(ENERGY)), transaction);
				if (diff != 0) {
					t.data.translate(ENERGY, (int) diff);
				}
			}
		}

	}

	public void transferTo(Direction d, int v, TransactionContext transaction) {
		transferTo(t.getBlockPos().offset(d.getNormal()), d, v, transaction);
	}

	public void transferFrom(Direction d, int v, TransactionContext transaction) {
		transferFrom(t.getBlockPos().offset(d.getNormal()), d, v, transaction);
	}

	public int getSizeCan() {
		int size = 0;

		BlockEntity tile;

		for (Direction d : Direction.values()) {
			tile = checkTile(d);
			if (tile != null) {
				if (handlerOf(tile, DireUtil.safeOps(d)) != null) {
					size++;
				}
			}
		}

		return size;
	}

}
