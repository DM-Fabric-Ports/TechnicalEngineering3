package ten3.core.machine.cable;

import java.util.Optional;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import team.reborn.energy.api.EnergyStorage;
import ten3.core.machine.CableBased;
import ten3.lib.capability.energy.EnergyTransferor;
import ten3.lib.capability.energy.FEStorageWayFinding;
import ten3.lib.tile.CmTileMachine;
import ten3.lib.tile.option.FaceOption;
import ten3.lib.tile.option.Type;
import ten3.util.StorageType;

public class CableTile extends CmTileMachine {

	public CableTile(BlockPos pos, BlockState state) {

		super(pos, state);

		setCap(getCapacity(), FaceOption.BOTH, FaceOption.OFF, getCapacity());

	}

	@Override
	public Type typeOf() {
		return Type.CABLE;
	}

	@Override
	public Optional<EnergyStorage> crtEne(Direction d) {
		return Optional.of(new FEStorageWayFinding(d, this));
	}

	@Override
	public Optional<Storage<ItemVariant>> crtItm(Direction d) {
		return Optional.empty();
	}

	public int getCapacity() {

		return kFE(10);

	}

	boolean eneWFS;

	@Override
	public void update() {

		if (getTileAliveTime() % 5 == 0) {
			((CableBased) getBlockState().getBlock()).update(world, pos);
			FEStorageWayFinding.updateNet(this);
		}

		if (getTileAliveTime() % 15 == 0) {
			eneWFS = EnergyTransferor.handlerOf(this, null).getAmount() > 0;
		}

		setActive(eneWFS && checkCanRun());

	}

	@Override
	protected boolean can(StorageType cap, Direction d) {
		if (cap.equals(StorageType.ITEM)) {
			return false;
		}
		return super.can(cap, d);
	}

}
