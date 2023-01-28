package ten3.plugin.emi;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public abstract class EmiTERecipe<T extends Recipe<? extends Container>> implements EmiRecipe {

	protected final T recipe;

	public EmiTERecipe(T recipe) {
		this.recipe = recipe;
	}

	@Override
	public @Nullable ResourceLocation getId() {
		return recipe.getId();
	}

	@Override
	public int getDisplayHeight() {
		return 60;
	}

	@Override
	public int getDisplayWidth() {
		return 105;
	}

}
