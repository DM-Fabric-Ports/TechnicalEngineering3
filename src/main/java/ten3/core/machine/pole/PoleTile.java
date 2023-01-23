package ten3.core.machine.pole;

import java.util.Optional;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import team.reborn.energy.api.EnergyStorage;
import ten3.lib.tile.mac.CmTileMachine;
import ten3.lib.tile.option.Type;
import ten3.util.StorageType;

public class PoleTile extends CmTileMachine {

	public BlockPos bind;

	public PoleTile(BlockPos pos, BlockState state) {

		super(pos, state);

	}

	@Override
	public void rdt(CompoundTag nbt) {
		super.rdt(nbt);
		if (nbt.contains("bind"))
			bind = BlockPos.of(nbt.getLong("bind"));
	}

	@Override
	public void wdt(CompoundTag nbt) {
		super.wdt(nbt);
		if (bind != null)
			nbt.putLong("bind", bind.asLong());
	}

	@Override
	public Type typeOf() {
		return Type.CABLE;
	}

	@Override
	public Optional<EnergyStorage> getEnergyHandler(Direction d) {
		if (d != Direction.DOWN)
			return Optional.empty();
		if (bind == null) {
			return Optional.empty();
		}
		BlockEntity be = level.getBlockEntity(bind.below());
		if (be == null) {
			return Optional.empty();
		}
		return Optional
				.ofNullable(EnergyStorage.SIDED.find(level, bind.below(), level.getBlockState(bind.below()), be,
						Direction.UP));
	}

	@Override
	public Optional<Storage<ItemVariant>> getItemHandler(Direction d) {
		return Optional.empty();
	}

	@Override
	public void update() {
	}

	@Override
	protected boolean hasFaceCapability(StorageType cap, Direction d) {
		if (cap != StorageType.ENERGY) {
			return false;
		}
		return super.hasFaceCapability(cap, d);
	}

	@Override
	public Optional<EnergyStorage> getEnergyStorage(Direction d) {
		if (d != Direction.DOWN)
			return Optional.empty();
		if (bind == null) {
			return Optional.empty();
		}
		BlockEntity be = level.getBlockEntity(bind.below());
		if (be == null) {
			return Optional.empty();
		}
		return Optional
				.ofNullable(EnergyStorage.SIDED.find(level, bind.below(), level.getBlockState(bind.below()), be, d));
	}

}
