package ten3.plugin.emi;

import java.util.Comparator;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ten3.util.KeyUtil;

public class TERecipeCategory extends EmiRecipeCategory {

	public TERecipeCategory(ResourceLocation id, EmiRenderable icon, EmiRenderable simplified, Comparator<EmiRecipe> sorter) {
		super(id, icon, simplified, sorter);
	}

	public TERecipeCategory(ResourceLocation id, EmiRenderable icon, EmiRenderable simplified) {
		super(id, icon, simplified);
	}

	public TERecipeCategory(ResourceLocation id, EmiRenderable icon) {
		super(id, icon);
	}

	@Override
	public Component getName() {
		return KeyUtil.translated(this.getId().getNamespace() + "." + "machine_" + this.getId().getPath());
	}

}
