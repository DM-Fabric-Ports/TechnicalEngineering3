package ten3.lib.capability.fluid;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.nbt.CompoundTag;

public class Tank extends SingleVariantStorage<FluidVariant> {

	public interface Condition {
		boolean valid(FluidVariant s);
	}

	Condition lmt;
	boolean in, out;

	final long capacity;

	public Tank(long capacity, Condition limit, boolean in, boolean out) {
		this.capacity = capacity;
		lmt = limit;
		this.in = in;
		this.out = out;
	}

	public boolean isEmpty() {
		return this.isResourceBlank() || this.getAmount() <= 0;
	}

	@Override
	protected FluidVariant getBlankVariant() {
		return FluidVariant.blank();
	}

	@Override
	protected long getCapacity(FluidVariant variant) {
		return this.lmt.valid(variant) ? this.capacity : 0;
	}

	@Override
	public boolean supportsExtraction() {
		return out;
	}

	@Override
	public boolean supportsInsertion() {
		return in;
	}

	public void readFromNBT(CompoundTag compound) {
		this.variant = FluidVariant.fromNbt(compound.getCompound("variant"));
		this.amount = compound.getLong("amount");
	}

}
