package ten3.core.machine;

import org.jetbrains.annotations.NotNull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import ten3.util.DireUtil;
import ten3.util.TransferUtil;

public class PipeBased extends CableBased {

	public PipeBased(Material m, SoundType s, String n) {

		super(m, s, n);

	}

	public int connectType(@NotNull Level world, @NotNull Direction facing, BlockPos pos) {

		BlockState sf = world.getBlockState(pos.offset(facing.getNormal()));

		BlockEntity t = world.getBlockEntity(pos);
		BlockEntity tf = world.getBlockEntity(pos.offset(facing.getNormal()));

		if (tf == null)
			return 0;
		if (t == null)
			return 0;

		boolean k = TransferUtil.getItemStorage(tf, DireUtil.safeOps(facing)) != null
				&& TransferUtil.getItemStorage(t, facing) != null;

		if (k) {
			if (sf.getBlock() != this)
				return 2;
			else
				return 1;
		}

		return 0;

	}

}
