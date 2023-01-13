package ten3;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.config.ModConfig;
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

    @Override
    public void onInitializeClient(ModContainer mod) {

    }

}
