package ten3.lib.capability.energy;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.Direction;
import ten3.lib.tile.CmTileMachine;
import ten3.lib.tile.option.FaceOption;

import static ten3.lib.tile.CmTileMachine.ENERGY;

public class FEStorageTile extends FEStorage {

	Direction di;
	CmTileMachine tile;

	public FEStorageTile(Direction d, CmTileMachine t) {

		super(t.maxStorage, t.maxReceive, t.maxExtract);

		di = d;
		tile = t;

	}

	public FEStorageTile with(Direction d) {
		di = d;
		return this;
	}

	@Override
	public long insert(long maxAmount, TransactionContext transaction) {
		if (canReceive()) {
			return super.insert(maxAmount, transaction);
		}
		return 0;
	}

	@Override
	public long getMaxExtract() {
		return tile.maxExtract;
	}

	@Override
	public long getMaxReceive() {
		return tile.maxReceive;
	}

	@Override
	public long extract(long maAmount, TransactionContext transaction) {
		if (canExtract()) {
			return super.extract(maAmount, transaction);
		}
		return 0;
	}

	@Override
	public void translateEnergy(long diff) {
		tile.data.translate(ENERGY, (int) diff);
	}

	@Override
	public void setEnergy(long diff) {
		tile.data.set(ENERGY, (int) diff);
	}

	@Override
	public long getAmount() {
		return tile.data.get(ENERGY);
	}

	@Override
	public long getCapacity() {
		return tile.maxStorage;
	}

	public boolean canExtract() {
		return tile.maxExtract > 0
				&&
				(di == null
						|| tile.direCheckEnergy(di) == FaceOption.OUT
						|| tile.direCheckEnergy(di) == FaceOption.BE_OUT
						|| tile.direCheckEnergy(di) == FaceOption.BOTH)
				&& tile.checkCanRun();
		// && tile.openEnergy;
	}

	public boolean canReceive() {
		return tile.maxReceive > 0
				&&
				(di == null
						|| tile.direCheckEnergy(di) == FaceOption.IN
						|| tile.direCheckEnergy(di) == FaceOption.BE_IN
						|| tile.direCheckEnergy(di) == FaceOption.BOTH)
				&& tile.checkCanRun();
		// && tile.openEnergy;
	}

}
