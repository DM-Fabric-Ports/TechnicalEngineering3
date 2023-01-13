package ten3.init.template;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class DefBlock extends Block {

	public static Properties build(double h, double r, Material m, SoundType s, int l, int light,
			boolean solid) {

		Properties p =
				Properties.of(m, m.getColor()).destroyTime((float) h).explosionResistance((float) r)
						.requiresCorrectToolForDrops().lightLevel((state) -> light).sound(s);

		if (!solid)
			p.noOcclusion();

		return p;

	}

	public DefBlock(double h, double r, Material m, SoundType s, int l, int light, boolean solid) {

		super(build(h, r, m, s, l, light, solid));

	}

	public DefBlock(Properties p) {
		super(p);
	}

}
