package ten3.core.machine;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class Cell extends MatedMac {

	public Cell(String name) {

		this(false, name);

	}

	public Cell(boolean solid, String name) {

		this(Material.METAL, SoundType.METAL, name, solid);

	}

	public Cell(Material m, SoundType s, String name, boolean solid) {

		super(m, s, name, solid);

	}

}
