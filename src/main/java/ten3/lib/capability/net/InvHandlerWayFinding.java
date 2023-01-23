package ten3.lib.capability.net;

import java.util.List;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.core.Direction;
import ten3.core.machine.pipe.PipeTile;
import ten3.lib.capability.item.ItemTransferor;
import ten3.lib.tile.mac.CmTileMachine;
import ten3.util.ExcUtil;

//connect generator with machine.
//#find - return a list of all connections
public class InvHandlerWayFinding extends CombinedStorage<ItemVariant, InventoryStorage> implements InventoryStorage {
	public static LevelNetsManager<InventoryStorage> manager = new LevelNetsManager<>();

	public static void updateNet(PipeTile tile) {
		find(tile);
	}

	private static void find(PipeTile start) {
		Finder.find(manager,
				(t, d) -> (InventoryStorage) ItemTransferor.handlerOf(t, d),
				(t) -> t.getType() == start.getType(), // for item filter
				(t) -> ((PipeTile) t).getCapacity(),
				start);
	}

	public CmTileMachine tile;

	InventoryStorage object;

	public InvHandlerWayFinding(Direction d, PipeTile t) {
		super(manager.getLevelNet(t).getNet(t.getBlockPos()).elements);
		object = ExcUtil.randomInCollection(parts);
		this.tile = t;
	}

	@Override
	public List<SingleSlotStorage<ItemVariant>> getSlots() {
		return object == null ? List.of() : object.getSlots();
	}

}
