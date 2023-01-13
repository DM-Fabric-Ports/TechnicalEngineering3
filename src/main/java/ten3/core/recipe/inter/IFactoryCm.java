package ten3.core.recipe.inter;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import ten3.core.recipe.CmItemList;
import ten3.core.recipe.SingleRecipe;

public interface IFactoryCm<T extends SingleRecipe> {
    T create(ResourceLocation reg, ResourceLocation idIn, CmItemList ingredientIn,
             ItemStack resultIn, ItemStack add, int cookTimeIn, int count, double cc);
}
