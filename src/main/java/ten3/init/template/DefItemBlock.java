package ten3.init.template;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import ten3.init.tab.DefGroup;
import ten3.lib.item.ItemGroupProvider;
import ten3.util.KeyUtil;

public class DefItemBlock extends BlockItem implements ItemGroupProvider {

	public DefItemBlock(Block b, Properties prp) {
		super(b, prp);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> tooltip,
			TooltipFlag p_41424_) {
		List<Component> list = new ArrayList<>();

		for (int i = 0; true; i++) {
			// *getPATH!
			String k = "ten3." + BuiltInRegistries.ITEM.getKey(this).getPath() + "." + i;
			Component ttc = KeyUtil.translated(KeyUtil.GOLD, k);
			if (ttc.getString().equals(k))
				break;

			list.add(ttc);
		}

		if (DefItem.shift()) {
			tooltip.addAll(list);
		} else if (list.size() > 0) {
			tooltip.add(KeyUtil.translated(KeyUtil.GOLD, "ten3.shift"));
		}
	}

	@Override
	public String getDescriptionId() {
		return KeyUtil.getKey(BuiltInRegistries.ITEM.getKey(this).getPath());
	}

	@Override
	public CreativeModeTab getTab() {
		return DefGroup.BLOCK;
	}

}
