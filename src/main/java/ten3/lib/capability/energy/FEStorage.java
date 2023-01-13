package ten3.lib.capability.energy;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import team.reborn.energy.api.EnergyStorage;

public class FEStorage implements EnergyStorage {

	protected long pro_energy;
	protected long capacity;
	protected long maxReceive;
	protected long maxExtract;

	public FEStorage(long capacity, long maxReceive, long maxExtract) {
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
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
	public long insert(long maxAmount, TransactionContext transaction) {
		if (getMaxExtract() <= 0)
			return 0;
		long energyReceived = Math.min(getCapacity() - getAmount(), Math.min(getMaxReceive(), maxReceive));
		translateEnergy(energyReceived);
		return energyReceived;
	}

	@Override
	public long extract(long maAmount, TransactionContext transaction) {
		if (getMaxReceive() <= 0)
			return 0;
		long energyExtracted = Math.min(getAmount(), Math.min(getMaxExtract(), maxExtract));
		translateEnergy(-energyExtracted);
		return energyExtracted;
	}

	@Override
	public long getAmount() {
		return pro_energy;
	}

	@Override
	public long getCapacity() {
		return capacity;
	}

}
