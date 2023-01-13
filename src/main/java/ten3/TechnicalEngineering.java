package ten3;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import ten3.core.client.HudSpanner;
import ten3.init.BlockInit;
import ten3.init.ContInit;
import ten3.init.FluidInit;
import ten3.init.ItemInit;
import ten3.init.RecipeInit;
import ten3.init.TileInit;

public class TechnicalEngineering implements ModInitializer, ClientModInitializer {

	@Override
	public void onInitialize(ModContainer mod) {
		BlockInit.regAll();
		TileInit.regAll();
		ContInit.regAll();

		ItemInit.regAll();
		RecipeInit.regAll();
	}

	@ClientOnly
	@Override
	public void onInitializeClient(ModContainer mod) {
		ContInit.doBinding();
		FluidInit.clientInit();
		HudRenderCallback.EVENT.register(new HudSpanner.RenderCallback());
	}

}
