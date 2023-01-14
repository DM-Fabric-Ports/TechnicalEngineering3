package ten3;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import ten3.core.client.HudSpanner;
import ten3.core.network.Network;
import ten3.init.BlockInit;
import ten3.init.ContInit;
import ten3.init.ItemInit;
import ten3.init.RecipeInit;
import ten3.init.TileInit;
import ten3.init.template.InvisibleItem;

public class TechnicalEngineering implements ModInitializer, ClientModInitializer {

	public static final CreativeModeTab GROUP_BLOCK =
			FabricItemGroup.builder(TConst.asResource("block"))
					.icon(() -> ItemInit.getItem("technical_block").getDefaultInstance()).build();
	public static final CreativeModeTab GROUP_ITEM =
			FabricItemGroup.builder(TConst.asResource("item"))
					.icon(() -> ItemInit.getItem("technical_item").getDefaultInstance()).build();

	@Override
	public void onInitialize(ModContainer mod) {
		BlockInit.regAll();
		TileInit.regAll();
		ContInit.regAll();
		ItemInit.regAll();
		RecipeInit.regAll();

		ItemGroupEvents.modifyEntriesEvent(GROUP_BLOCK).register(entries -> BuiltInRegistries.ITEM
				.stream()
				.filter(i -> i instanceof BlockItem
						&& BuiltInRegistries.ITEM.getKey(i).getNamespace().equals(TConst.modid)
						&& !(i instanceof InvisibleItem))
				.forEach(entries::accept));
		ItemGroupEvents.modifyEntriesEvent(GROUP_ITEM).register(entries -> BuiltInRegistries.ITEM
				.stream()
				.filter(i -> !(i instanceof BlockItem)
						&& BuiltInRegistries.ITEM.getKey(i).getNamespace().equals(TConst.modid)
						&& !(i instanceof InvisibleItem))
				.forEach(entries::accept));

		Network.register();
	}

	@ClientOnly
	@Override
	public void onInitializeClient(ModContainer mod) {
		ContInit.doBinding();
		// TODO
		// FluidInit.clientInit();
		HudRenderCallback.EVENT.register(new HudSpanner.RenderCallback());
		Network.registerClient();
	}

}
