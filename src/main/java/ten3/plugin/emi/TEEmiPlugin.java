package ten3.plugin.emi;

import java.util.function.Function;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import ten3.TConst;
import ten3.init.ItemInit;
import ten3.init.RecipeInit;
import ten3.lib.recipe.FormsCombinedRecipe;

public class TEEmiPlugin implements EmiPlugin {

	public static final TERecipeCategory PULV = new TERecipeCategory(TConst.asResource("pulverizer"),
			EmiStack.of(ItemInit.getItem("machine_pulverizer")));
	public static final TERecipeCategory COMP = new TERecipeCategory(TConst.asResource("compressor"),
			EmiStack.of(ItemInit.getItem("machine_compressor")));
	public static final TERecipeCategory PSIO = new TERecipeCategory(TConst.asResource("psionicant"),
			EmiStack.of(ItemInit.getItem("machine_psionicant")));
	public static final TERecipeCategory INDF = new TERecipeCategory(TConst.asResource("induction_furnace"),
			EmiStack.of(ItemInit.getItem("machine_induction_furnace")));

	@Override
	public void register(EmiRegistry registry) {
		registerCategory(registry, PULV, "pulverizer", EmiStack.of(ItemInit.getItem("machine_pulverizer")),
				recipe -> new EmiTERecipeSg((FormsCombinedRecipe) recipe, PULV));
		registerCategory(registry, COMP, "compressor", EmiStack.of(ItemInit.getItem("machine_compressor")),
				recipe -> new EmiTERecipeSg((FormsCombinedRecipe) recipe, COMP));
		registerCategory(registry, PSIO, "psionicant", EmiStack.of(ItemInit.getItem("machine_psionicant")),
				recipe -> new EmiTERecipeSg((FormsCombinedRecipe) recipe, PSIO));
		registerCategory(registry, INDF, "induction_furnace",
				EmiStack.of(ItemInit.getItem("machine_induction_furnace")),
				recipe -> new EmiTERecipeSg((FormsCombinedRecipe) recipe, INDF));
	}

	@SuppressWarnings("unchecked")
	protected <C extends Container, T extends Recipe<C>> void registerCategory(EmiRegistry registry,
			EmiRecipeCategory category, String type, EmiIngredient workspace, Function<T, EmiRecipe> factory) {
		registry.addCategory(category);
		registry.addWorkstation(category, workspace);
		for (Recipe<Container> recipe : registry.getRecipeManager().getAllRecipesFor(RecipeInit.getRcpType(type)))
			registry.addRecipe(factory.apply((T) recipe));
	}

}
