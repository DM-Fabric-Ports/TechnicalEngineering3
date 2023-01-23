<<<<<<<< HEAD:src/main/java/ten3/core/recipe/serial/CmSerializer.java
package ten3.core.recipe.serial;
========
package ten3.lib.recipe;
>>>>>>>> forge:src/main/java/ten3/lib/recipe/CmSerializer.java

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public interface CmSerializer<T extends Recipe<?>> extends RecipeSerializer<T> {

    int fallBackTime = 150;

    ResourceLocation id();

}
