package ten3.lib.capability.fluid;

import java.util.Iterator;

import org.jetbrains.annotations.NotNull;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.Direction;
import ten3.lib.capability.UtilCap;
import ten3.lib.tile.mac.CmTileMachine;
import ten3.util.RAUtil;

public class TankArray implements Storage<FluidVariant> {

	public CmTileMachine tile;
	Direction di;

	public TankArray(Direction d, CmTileMachine t) {
		di = d;
		tile = t;
	}

	public int getTanks() {
		return tile.tanks.size();
	}

	@NotNull
	public ResourceAmount<FluidVariant> getFluidInTank(int tank) {
		if (tile.tanks.size() <= tank) {
			return RAUtil.emptyFluid();
		}
		if (!UtilCap.canOutFLU(di, tile)) {
			return RAUtil.emptyFluid();
		}
		return RAUtil.of(tile.tanks.get(tank));
	}

	public long getTankCapacity(int tank) {
		if (tile.tanks.size() <= tank)
			return 0;
		return tile.tanks.get(tank).getCapacity();
	}

	// public boolean isFluidValid(int tank, @NotNull ResourceAmount<FluidVariant>
	// stack) {
	// if (tile.tanks.size() <= tank)
	// return false;
	// return tile.tanks.get(tank).isFluidValid(stack);
	// }

	@Override
	public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
		if (!UtilCap.canInFLU(di, tile)) {
			return 0;
		}
		long copy = maxAmount;
		for (Tank t : tile.tanks) {
			if (t == null)
				continue;
			if (!t.in)
				continue;

			copy -= t.insert(resource, copy, transaction);
			if (copy < 0) {
				copy = 0;
				break;
			}
		}
		return maxAmount - copy;
	}

	@Override
	public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
		if (!UtilCap.canOutFLU(di, tile)) {
			return 0;
		}
		long s = 0;
		for (Tank t : tile.tanks) {
			if (t == null)
				continue;
			if (!t.supportsExtraction())
				continue;
			s += t.extract(resource, maxAmount, transaction);
			if (s >= maxAmount)
				break;
		}
		return s;
	}

	@Override
	public Iterator<StorageView<FluidVariant>> iterator() {
		return tile.tanks.stream().map(t -> (StorageView<FluidVariant>) t).iterator();
	}

}
