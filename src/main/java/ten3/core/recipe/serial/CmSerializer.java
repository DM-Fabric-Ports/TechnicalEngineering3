package ten3.core.recipe.serial;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public interface CmSerializer<T extends Recipe<?>> extends RecipeSerializer<T> {

    int fallBackTime = 150;

    ResourceLocation id();

}
