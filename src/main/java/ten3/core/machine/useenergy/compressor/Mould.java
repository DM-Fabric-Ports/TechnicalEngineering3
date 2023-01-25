package ten3.core.machine.useenergy.compressor;

import net.minecraft.world.item.CreativeModeTab;
import ten3.init.tab.DefGroup;
import ten3.init.template.DefItem;

public class Mould extends DefItem {

	public Mould() {
		super(build(1));
	}

	@Override
	public CreativeModeTab getTab() {
		return DefGroup.TOOL;
	}

}
