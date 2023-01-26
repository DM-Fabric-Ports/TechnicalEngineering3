package ten3.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import ten3.TConst;
import ten3.lib.recipe.CmSerializer;
import ten3.lib.recipe.FormsCombinedRecipe;
import ten3.lib.recipe.FormsCombinedRecipeSerializer;
import ten3.lib.recipe.RecipeTypeCm;

public class RecipeInit {

	public static void regAll() {
		regFormsCombined("pulverizer", 1, 4);
		regFormsCombined("compressor", 2, 1);
		regFormsCombined("psionicant", 2, 1);
		regFormsCombined("induction_furnace", 3, 1);
	}

	public static void regFormsCombined(String id, int i, int o) {
		regRcp(new FormsCombinedRecipeSerializer<>(FormsCombinedRecipe::new, id, i, o));
	}

	public static void regRcp(CmSerializer<?> s) {
		String id = s.id();
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, TConst.asResource(id), s);
		Registry.register(BuiltInRegistries.RECIPE_TYPE, TConst.asResource(id), new RecipeTypeCm<>(id));
	}

	public static RecipeSerializer<?> getRcp(String id) {
		return BuiltInRegistries.RECIPE_SERIALIZER.get(TConst.asResource(id));
	}

	@SuppressWarnings("unchecked")
	public static <C extends Container, T extends Recipe<C>> RecipeType<T> getRcpType(String id) {
		return (RecipeType<T>) BuiltInRegistries.RECIPE_TYPE.get(TConst.asResource(id));
	}

}
