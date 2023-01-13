package ten3;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import ten3.init.*;

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
    }

}
