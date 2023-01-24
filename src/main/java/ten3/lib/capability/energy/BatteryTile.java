package ten3.lib.capability.energy;

import static ten3.lib.tile.mac.CmTileMachine.ENERGY;

import net.minecraft.core.Direction;
import ten3.lib.capability.UtilCap;
import ten3.lib.tile.mac.CmTileMachine;

public class BatteryTile extends Battery {

	Direction di;
	public CmTileMachine tile;

	public BatteryTile(Direction d, CmTileMachine t) {
		super(0, 0, 0);

		di = d;
		tile = t;
	}

	public BatteryTile with(Direction d) {
		di = d;
		return this;
	}

	@Override
	public long getMaxExtract() {
		return tile.info.maxExtractEnergy;
	}

	@Override
	public long getMaxReceive() {
		return tile.info.maxReceiveEnergy;
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
		return tile.info.maxStorageEnergy;
	}

	@Override
	public boolean supportsExtraction() {
		return UtilCap.canOut(di, tile);
	}

	@Override
	public boolean supportsInsertion() {
		return UtilCap.canIn(di, tile);
	}

}
