package ten3.core.machine.pipe;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import team.reborn.energy.api.EnergyStorage;
import ten3.core.machine.PipeBased;
import ten3.lib.capability.item.ItemStorageWayFinding;
import ten3.lib.tile.CmTileMachine;
import ten3.lib.tile.option.FaceOption;
import ten3.lib.tile.option.Type;
import ten3.util.StorageType;

import java.util.Optional;

public class PipeTile extends CmTileMachine {

	public PipeTile(BlockPos pos, BlockState state) {

		super(pos, state);

		setCap(getCapacity(), FaceOption.OFF, FaceOption.BOTH, getCapacity());

	}

	@Override
	public Type typeOf() {
		return Type.CABLE;
	}

	@Override
	public Optional<EnergyStorage> crtEne(Direction d) {
		return Optional.empty();
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public Optional<Storage<ItemVariant>> crtItm(Direction d) {
		return Optional.of(new ItemStorageWayFinding(d, this));
	}


	public int getCapacity() {

		return 6;

	}

	@Override
	public void update() {

		if (getTileAliveTime() % 5 == 0) {
			((PipeBased) getBlockState().getBlock()).update(world, pos);
			ItemStorageWayFinding.updateNet(this);
		}

	}

	@Override
	protected boolean can(StorageType cap, Direction d) {
		if (cap == StorageType.ENERGY) {
			return false;
		}
		return super.can(cap, d);
	}

}
