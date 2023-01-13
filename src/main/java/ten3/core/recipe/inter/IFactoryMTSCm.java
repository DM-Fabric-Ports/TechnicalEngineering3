package ten3.core.recipe.inter;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import ten3.core.recipe.CmItemList;
import ten3.core.recipe.SingleRecipe;

import java.util.List;

public interface IFactoryMTSCm<T extends SingleRecipe> {
    T create(ResourceLocation reg, ResourceLocation idIn, List<CmItemList> ings,
             ItemStack resultIn, ItemStack add, int cookTimeIn, int count, double cc);
}
