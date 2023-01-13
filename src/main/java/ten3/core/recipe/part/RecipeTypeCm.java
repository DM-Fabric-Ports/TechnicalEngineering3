package ten3.core.recipe.part;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class RecipeTypeCm<T extends Recipe<?>> implements RecipeType<T> {

    ResourceLocation reg;

    public RecipeTypeCm(ResourceLocation rl) {
        reg = rl;
    }

    @Override
    public String toString()
    {
        return reg.toString();
    }

}
