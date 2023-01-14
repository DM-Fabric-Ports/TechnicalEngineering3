package ten3.lib.capability.item;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import ten3.core.machine.pipe.PipeTile;
import ten3.lib.capability.Finder;
import ten3.lib.tile.CmTileMachine;
import ten3.util.ExcUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// connect generator with machine.
// #find - return a list of all connections

@SuppressWarnings("NonExtendableApiUsage")
public class ItemStorageWayFinding extends CombinedStorage<ItemVariant, InventoryStorage>
		implements InventoryStorage {

	public static Map<BlockPos, List<InventoryStorage>> nets = new HashMap<>();
	public static Map<BlockPos, Long> caps = new HashMap<>();

	public static void updateNet(PipeTile tile) {
		find(tile);
	}

	InventoryStorage object;

	public ItemStorageWayFinding(Direction d, CmTileMachine t) {
		super(nets.get(t.getBlockPos()));
		object = ExcUtil.randomInCollection(nets.get(t.pos));
	}

	@Override
	public List<SingleSlotStorage<ItemVariant>> getSlots() {
		return object == null ? List.of() : object.getSlots();
	}

	@SuppressWarnings("all")
	private static void find(PipeTile start, List<InventoryStorage> init,
			HashMap<BlockPos, BlockEntity> fouInit, Direction fr, long cap, boolean callFirst) {

		Finder.find(nets, caps, (t, d) -> (InventoryStorage) ItemTransferor.handlerOf(t, d),
				(t) -> ((PipeTile) t).getCapacity(), (t) -> t instanceof PipeTile, start, init,
				fouInit, fr, cap, callFirst);

	}

	private static void find(PipeTile start) {
		find(start, null, null, null, Integer.MAX_VALUE, true);
	}

}
