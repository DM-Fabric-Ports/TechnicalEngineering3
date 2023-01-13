package ten3.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("all")
public class TransferUtil {

	public static <T> T execute(Function<Transaction, T> func) {
		try (Transaction t = getTransaction()) {
			return func.apply(t);
		}
	}

	public static Transaction getTransaction() throws IllegalStateException {
		if (Transaction.isOpen()) {
			TransactionContext open = Transaction.getCurrentUnsafe();
			if (open != null)
				return open.openNested();
		}
		return Transaction.openOuter();
	}

	/**
	 * The recommended way to get an item storage.
	 *
	 * @see TransferUtil#getItemStorage(Level, BlockPos, BlockEntity, Direction)
	 */
	@Nullable
	public static Storage<ItemVariant> getItemStorage(Level level, BlockPos pos, @Nullable Direction side) {
		return getItemStorage(level, pos, null, side);
	}

	/**
	 * Prefer {@link TransferUtil#getItemStorage(Level, BlockPos, Direction)}
	 * variant when possible.
	 *
	 * @see TransferUtil#getItemStorage(Level, BlockPos, BlockEntity, Direction)
	 */
	@Nullable
	public static Storage<ItemVariant> getItemStorage(Level level, BlockPos pos) {
		return getItemStorage(level, pos, null);
	}

	/**
	 * Prefer {@link TransferUtil#getItemStorage(Level, BlockPos, Direction)}
	 * variant when possible.
	 *
	 * @see TransferUtil#getItemStorage(Level, BlockPos, BlockEntity, Direction)
	 */
	@Nullable
	public static Storage<ItemVariant> getItemStorage(BlockEntity be, @Nullable Direction side) {
		return getItemStorage(null, null, be, side);
	}

	/**
	 * Prefer {@link TransferUtil#getItemStorage(Level, BlockPos, Direction)}
	 * variant when possible.
	 *
	 * @see TransferUtil#getItemStorage(Level, BlockPos, BlockEntity, Direction)
	 */
	@Nullable
	public static Storage<ItemVariant> getItemStorage(BlockEntity be) {
		return getItemStorage(be, null);
	}

	/**
	 * Using the provided Level and BlockPos OR BlockEntity, find a Storage
	 * containing ItemVariants.
	 * Prefer {@link TransferUtil#getItemStorage(Level, BlockPos, Direction)}
	 * variant generally.
	 *
	 * @param level the Level to check in - may be client only, despite what regular
	 *              FAPI allows. Null is allowed as long as 'be' is NOT null.
	 * @param pos   the position to check. Null is allowed as long as 'be' is NOT
	 *              null.
	 * @param be    the Block Entity to check. Null is allowed as long as 'level'
	 *              and 'pos' are NOT null.
	 * @param side  the Direction to check in - null is considered all sides and
	 *              will return a wrapper around all found.
	 * @see TransferUtil#getItemStorage(Level, BlockPos, Direction)
	 * @return a Storage of ItemVariants, or null if none found.
	 */
	@Nullable
	public static Storage<ItemVariant> getItemStorage(Level level, BlockPos pos, BlockEntity be,
			@Nullable Direction side) {
		if (be == null) {
			Objects.requireNonNull(level, "If a null Block Entity is provided, the Level may NOT be null!");
			Objects.requireNonNull(pos, "If a null Block Entity is provided, the pos may NOT be null!");
		}
		if (level == null || pos == null) {
			Objects.requireNonNull(be, "If a null level or pos is provided, the Block Entity may NOT be null!");
			level = be.getLevel();
			pos = be.getBlockPos();
		}
		// on the client we only allow lib handling.
		if (be instanceof SidedStorageBlockEntity t) {
			return t.getItemStorage(side);
		}
		List<Storage<ItemVariant>> itemStorages = new ArrayList<>();
		BlockState state = be == null ? level.getBlockState(pos) : be.getBlockState();
		for (Direction direction : getDirections(side)) {
			Storage<ItemVariant> fluidStorage = ItemStorage.SIDED.find(level, pos, state, be, direction);

			if (fluidStorage != null) {
				if (itemStorages.size() == 0) {
					itemStorages.add(fluidStorage);
					continue;
				}

				for (Storage<ItemVariant> storage : itemStorages) {
					if (!Objects.equals(fluidStorage, storage)) {
						itemStorages.add(fluidStorage);
						break;
					}
				}
			}
		}

		if (itemStorages.isEmpty())
			return null;
		if (itemStorages.size() == 1)
			return itemStorages.get(0);
		return new CombinedStorage<>(itemStorages);
	}

	/**
	 * Given a Direction, determine which Directions should be checked for storages.
	 *
	 * @return the provided Direction if non-null, otherwise all Directions
	 */
	private static Direction[] getDirections(@Nullable Direction direction) {
		if (direction == null)
			return Direction.values();
		return new Direction[] { direction };
	}

}
