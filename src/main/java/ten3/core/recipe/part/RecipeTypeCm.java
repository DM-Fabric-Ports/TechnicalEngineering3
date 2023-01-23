<<<<<<<< HEAD:src/main/java/ten3/core/recipe/part/RecipeTypeCm.java
package ten3.core.recipe.part;
========
package ten3.lib.recipe;
>>>>>>>> forge:src/main/java/ten3/lib/recipe/RecipeTypeCm.java

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
