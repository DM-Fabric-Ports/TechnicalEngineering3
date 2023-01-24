package ten3.lib.capability.energy;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import team.reborn.energy.api.EnergyStorage;

public class Battery implements EnergyStorage {

	protected long pro_energy;
	protected long capacity;
	protected long maxReceive;
	protected long maxExtract;

	public Battery(long capacity, long maxReceive, long maxExtract) {
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	@Override
	public long insert(long maxAmount, TransactionContext transaction) {
		if (!supportsInsertion()) {
			return 0;
		}
		long energyReceived = Math.min(getCapacity() - getAmount(), Math.min(getMaxReceive(), maxAmount));
		translateEnergy(energyReceived);
		return energyReceived;
	}

	@Override
	public long extract(long maxAmount, TransactionContext transaction) {
		if (!supportsInsertion()) {
			return 0;
		}
		long energyExtracted = Math.min(getAmount(), Math.min(getMaxExtract(), maxAmount));
		translateEnergy(-energyExtracted);
		return energyExtracted;
	}

	@Override
	public long getAmount() {
		return pro_energy;
	}

	public long getMaxReceive() {
		return maxReceive;
	}

	public long getMaxExtract() {
		return maxExtract;
	}

	public void translateEnergy(long diff) {
		pro_energy += diff;
	}

	public void setEnergy(long diff) {
		pro_energy = diff;
	}

	@Override
	public long getCapacity() {
		return capacity;
	}

	@Override
	public boolean supportsExtraction() {
		return this.maxExtract > 0;
	}

	@Override
	public boolean supportsInsertion() {
		return this.maxReceive > 0;
	}

}
