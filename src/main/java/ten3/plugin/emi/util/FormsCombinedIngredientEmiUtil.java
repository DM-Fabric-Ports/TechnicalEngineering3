package ten3.plugin.emi.util;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import ten3.lib.recipe.FormsCombinedIngredient;

public class FormsCombinedIngredientEmiUtil {

	public static EmiIngredient ofIngredient(FormsCombinedIngredient ingredient) {
		return switch (ingredient.form) {
			case "item" ->
				switch (ingredient.type) {
					case "tag" ->
						EmiIngredient.of(TagKey.create(Registries.ITEM, ingredient.key),
								ingredient.amountOrCount);
					case "static" ->
						EmiStack.of(BuiltInRegistries.ITEM.get(ingredient.key), ingredient.amountOrCount);
					default ->
						null;
				};
			case "fluid" ->
				switch (ingredient.type) {
					case "tag" -> new FluidTagEmiIngredient(TagKey.create(Registries.FLUID, ingredient.key),
							ingredient.amountOrCount);
					case "static" -> EmiStack.of(BuiltInRegistries.FLUID.get(ingredient.key), ingredient.amountOrCount);
					default ->
						null;
				};
			default ->
				null;
		};
	}

	public static EmiStack ofStack(FormsCombinedIngredient ingredient) {
		return switch (ingredient.form) {
			case "item" ->
				switch (ingredient.type) {
					case "static" ->
						EmiStack.of(BuiltInRegistries.ITEM.get(ingredient.key), ingredient.amountOrCount);
					default ->
						null;
				};
			case "fluid" ->
				switch (ingredient.type) {
					case "static" -> EmiStack.of(BuiltInRegistries.FLUID.get(ingredient.key), ingredient.amountOrCount);
					default ->
						null;
				};
			default ->
				null;
		};
	}

}
