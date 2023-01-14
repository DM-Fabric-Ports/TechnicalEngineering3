package ten3.init.template;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import ten3.util.TranslateKeyUtil;

public class DefItemBlock extends BlockItem {

	public DefItemBlock(Block b) {
		this(b, 64);
	}

	public DefItemBlock(Block b, int size) {
		super(b, new Properties().stacksTo(size));
	}

	@Override
	public Component getName(ItemStack p_41458_) {
		return TranslateKeyUtil.getKey(BuiltInRegistries.ITEM.getKey(this).getPath());
	}

}
