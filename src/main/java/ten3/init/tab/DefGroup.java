package ten3.init.tab;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.item.CreativeModeTab;
import ten3.TConst;
import ten3.init.ItemInit;

public class DefGroup {

	public static final CreativeModeTab BLOCK = FabricItemGroup.builder(TConst.asResource("block"))
			.icon(() -> ItemInit.getItem("tin_ore").getDefaultInstance()).build();

	public static final CreativeModeTab MAC = FabricItemGroup.builder(TConst.asResource("machine"))
			.icon(() -> ItemInit.getItem("machine_pulverizer").getDefaultInstance()).build();

	public static final CreativeModeTab ITEM = FabricItemGroup.builder(TConst.asResource("item"))
			.icon(() -> ItemInit.getItem("tin_ingot").getDefaultInstance()).build();

	public static final CreativeModeTab TOOL = FabricItemGroup.builder(TConst.asResource("tool"))
			.icon(() -> ItemInit.getItem("photosyn_levelup").getDefaultInstance()).build();

}
