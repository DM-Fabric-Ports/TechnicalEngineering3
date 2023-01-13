package ten3.core.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class FluidCm extends FlowingFluid {
	private final Block block;
	private final Item bucket;

	private final Fluid flowing;
	private final Fluid source;

	public FluidCm(Block block, Item bucket, Fluid flowing,  Fluid source) {
		this.block = block;
		this.bucket = bucket;
		this.flowing = flowing;
		this.source = source;
	}

	@Override
	public Fluid getFlowing() {
		return flowing;
	}

	@Override
	public Fluid getSource() {
		return source;
	}

	@Override
	protected boolean canConvertToSource(Level level) {
		return false;
	}

	@Override
	protected void beforeDestroyingBlock(LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
		final BlockEntity blockEntity = state.hasBlockEntity() ? levelAccessor.getBlockEntity(pos) : null;
		Block.dropResources(state, levelAccessor, pos, blockEntity);
	}

	@Override
	protected int getSlopeFindDistance(LevelReader levelReader) {
		return 2;
	}

	@Override
	protected int getDropOff(LevelReader levelReader) {
		return 1;
	}

	@Override
	public int getAmount(FluidState fluidState) {
		return 0;
	}

	@Override
	public int getTickDelay(LevelReader levelReader) {
		return 5;
	}

	@Override
	protected float getExplosionResistance() {
		return 100.0F;
	}

	@Override
	public Item getBucket() {
		return bucket;
	}

	@Override
	protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, Direction direction) {
		return false;
	}

	@Override
	protected BlockState createLegacyBlock(FluidState fluidState) {
		return block.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(fluidState));
	}

	@Override
	public boolean isSource(FluidState fluidState) {
		return false;
	}

	public static class Flowing extends FluidCm {

		public Flowing(Block block, Item bucket, Fluid flowing, Fluid source) {
			super(block, bucket, flowing, source);
		}

		@Override
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getAmount(FluidState fluidState) {
			return fluidState.getValue(LEVEL);
		}

		@Override
		public boolean isSource(FluidState fluidState) {
			return false;
		}
	}

	public static class Source extends FluidCm {

		public Source(Block block, Item bucket, Fluid flowing, Fluid source) {
			super(block, bucket, flowing, source);
		}

		@Override
		public int getAmount(FluidState fluidState) {
			return 8;
		}

		@Override
		public boolean isSource(FluidState fluidState) {
			return true;
		}
	}
}
