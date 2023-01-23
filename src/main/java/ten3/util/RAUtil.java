package ten3.util;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.minecraft.world.level.material.Fluid;

public class RAUtil {
	public static ResourceAmount<FluidVariant> of(Fluid fluid, long amount) {
		return new ResourceAmount<>(FluidVariant.of(fluid), amount);
	}

	public static <T> ResourceAmount<T> of(StorageView<T> storage) {
		return new ResourceAmount<T>(storage.getResource(), storage.getAmount());
	}

	public static ResourceAmount<FluidVariant> emptyFluid() {
		return new ResourceAmount<>(FluidVariant.blank(), 0);
	}

	public static boolean isEmpty(ResourceAmount<?> i) {
		return i.amount() <= 0 || (i.resource() instanceof TransferVariant<?> v && v.isBlank()) || i.resource() == null;
	}

	public static <T> ResourceAmount<T> clone(ResourceAmount<T> i, long amount) {
		return new ResourceAmount<T>(i.resource(), amount);
	}
}
