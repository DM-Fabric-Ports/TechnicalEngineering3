<<<<<<<< HEAD:src/main/java/ten3/core/recipe/inter/IBaseRecipeCm.java
package ten3.core.recipe.inter;
========
package ten3.lib.recipe;
>>>>>>>> forge:src/main/java/ten3/lib/recipe/IBaseRecipeCm.java

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

<<<<<<<< HEAD:src/main/java/ten3/core/recipe/inter/IBaseRecipeCm.java
import java.util.List;

========
>>>>>>>> forge:src/main/java/ten3/lib/recipe/IBaseRecipeCm.java
public interface IBaseRecipeCm<T extends Container> extends Recipe<T> {

    int time();

    int inputLimit(ItemStack stack);

<<<<<<<< HEAD:src/main/java/ten3/core/recipe/inter/IBaseRecipeCm.java
	@Override
	default boolean isSpecial() {
		return true;
	}
========
    default boolean isSpecial()
    {
        return true;
    }

    @Override
    default boolean canCraftInDimensions(int p_43999_, int p_44000_)
    {
        return true;
    }

>>>>>>>> forge:src/main/java/ten3/lib/recipe/IBaseRecipeCm.java
}
