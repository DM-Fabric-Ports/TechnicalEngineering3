package ten3;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.intellij.lang.annotations.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import ten3.core.client.HudSpanner;
import ten3.init.*;

public class TechnicalEngineering implements ModInitializer, ClientModInitializer {

	public TechnicalEngineering() {
		ContInit.CONS.register(bus);
	}

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
		FluidInit.clientInit();
		HudRenderCallback.EVENT.register(new HudSpanner.RenderCallback());
	}

}
