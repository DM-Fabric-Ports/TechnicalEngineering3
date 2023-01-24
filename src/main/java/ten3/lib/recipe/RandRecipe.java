package ten3.lib.recipe;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface RandRecipe<T extends Container> extends IBaseRecipeCm<T> {

	default List<ItemStack> generateItems() {
		List<ItemStack> ss = new ArrayList<>();
		for (int i = 0; i < output().size(); i++) {
			FormsCombinedIngredient ing = output().get(i);
			ItemStack s = ing.genItem();
			ss.add(s);
		}
		return ss;
	}

	default List<ItemStack> allOutputItems() {
		List<ItemStack> ss = new ArrayList<>();
		for (int i = 0; i < output().size(); i++) {
			FormsCombinedIngredient ing = output().get(i);
			ss.add(ing.symbolItem());
		}
		return ss;
	}

	default List<ResourceAmount<FluidVariant>> generateFluids() {
		List<ResourceAmount<FluidVariant>> ss = new ArrayList<>();
		for (int i = 0; i < output().size(); i++) {
			FormsCombinedIngredient ing = output().get(i);
			ss.add(ing.genFluid());
		}
		return ss;
	}

	default List<ResourceAmount<FluidVariant>> allOutputFluids() {
		List<ResourceAmount<FluidVariant>> ss = new ArrayList<>();
		for (int i = 0; i < output().size(); i++) {
			FormsCombinedIngredient ing = output().get(i);
			ss.add(ing.symbolFluid());
		}
		return ss;
	}

	List<FormsCombinedIngredient> output();

	@Override
	default ItemStack getResultItem() {
		return output().get(0).symbolItem();
	}

}
