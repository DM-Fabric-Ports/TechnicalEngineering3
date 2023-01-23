package ten3.core.machine.pipe;

import java.util.Optional;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import ten3.core.machine.PipeBased;
import ten3.lib.capability.net.InvHandlerWayFinding;
import ten3.lib.tile.mac.CmTileMachine;
import ten3.lib.tile.option.Type;
import ten3.util.StorageType;

public class PipeTile extends CmTileMachine {

	public PipeTile(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public Type typeOf() {
		return Type.CABLE;
	}

	public boolean isItemCanBeTransferred(ItemStack stack) {
		return true;
	}

	public int getCapacity() {
		return 6;
	}

	@Override
	public void update() {
		if (getTileAliveTime() % 10 == 0) {
			((PipeBased) getBlockState().getBlock()).update(level, worldPosition);
			InvHandlerWayFinding.updateNet(this);
		}
	}

	@Override
	public Optional<Storage<ItemVariant>> getItemHandler(Direction d) {
		return Optional.of(new InvHandlerWayFinding(d, this));
	}

	@Override
	protected boolean hasFaceCapability(StorageType cap, Direction d) {
		if (cap == StorageType.ENERGY) {
			return false;
		}
		return super.hasFaceCapability(cap, d);
	}

}
