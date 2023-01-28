package ten3.plugin.emi;

import java.util.List;
import java.util.function.Supplier;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import ten3.lib.recipe.FormsCombinedRecipe;
import ten3.plugin.emi.util.FormsCombinedIngredientEmiUtil;

public class EmiTERecipeSg extends EmiTERecipe<FormsCombinedRecipe> {

	final EmiRecipeCategory category;

	public EmiTERecipeSg(FormsCombinedRecipe recipe, EmiRecipeCategory category) {
		super(recipe);
		this.category = category;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return this.category;
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return recipe.input.stream().map(FormsCombinedIngredientEmiUtil::ofIngredient).toList();
	}

	@Override
	public List<EmiStack> getOutputs() {
		return recipe.output.stream().map(FormsCombinedIngredientEmiUtil::ofStack).toList();
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		switch (this.getInputs().size()) {
			case 1 -> {
				addSlotSafe(widgets, () -> this.getInputs().get(0), 8, 22);
			}
			case 2 -> {
				addSlotSafe(widgets, () -> this.getInputs().get(0), 2, 22);
				addSlotSafe(widgets, () -> this.getInputs().get(1), 20, 22);
			}
			case 3 -> {
				addSlotSafe(widgets, () -> this.getInputs().get(0), 11, 13);
				addSlotSafe(widgets, () -> this.getInputs().get(1), 2, 31);
				addSlotSafe(widgets, () -> this.getInputs().get(2), 20, 31);
			}
		}

		if (this.getOutputs().size() <= 1) {
			addSlotSafe(widgets, () -> this.getOutputs().get(0), 79, 22);
		} else {
			addSlotSafe(widgets, () -> this.getOutputs().get(0), 65, 13);
			addSlotSafe(widgets, () -> this.getOutputs().get(1), 83, 13);
			addSlotSafe(widgets, () -> this.getOutputs().get(2), 65, 31);
			addSlotSafe(widgets, () -> this.getOutputs().get(3), 83, 31);
		}

		widgets.addFillingArrow(this.getOutputs().size() <= 1 ? 43 + 1 : 37 + 1, 22 + 1, recipe.time() * 50);
	}

	protected void addSlotSafe(WidgetHolder widgets, Supplier<EmiIngredient> ingredient, int x, int y) {
		try {
			widgets.addSlot(ingredient.get(), x + 1, y + 1);
		} catch (Throwable e) {
			widgets.addSlot(x + 1, y + 1);
		}
	}

}
