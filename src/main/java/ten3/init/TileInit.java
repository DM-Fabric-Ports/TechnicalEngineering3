package ten3.init;

import java.util.HashMap;
import java.util.Map;

import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import ten3.TConst;
import ten3.core.machine.cable.CableTile;
import ten3.core.machine.cell.CellTile;
import ten3.core.machine.engine.biomass.BiomassTile;
import ten3.core.machine.engine.extractor.ExtractorTile;
import ten3.core.machine.engine.metalizer.MetalizerTile;
import ten3.core.machine.pipe.PipeTile;
import ten3.core.machine.useenergy.beacon.BeaconTile;
import ten3.core.machine.useenergy.compressor.CompressorTile;
import ten3.core.machine.useenergy.farm.FarmTile;
import ten3.core.machine.useenergy.indfur.IndfurTile;
import ten3.core.machine.useenergy.mobrip.MobRipTile;
import ten3.core.machine.useenergy.psionicant.PsionicantTile;
import ten3.core.machine.useenergy.pulverizer.PulverizerTile;
import ten3.core.machine.useenergy.quarry.QuarryTile;
import ten3.core.machine.useenergy.smelter.FurnaceTile;
import ten3.lib.tile.CmTileEntity;

public class TileInit {

	static Map<String, BlockEntityType<? extends CmTileEntity>> regs = new HashMap<>();

	public static void regAll() {
		regTile("engine_extraction", ExtractorTile::new);
		regTile("engine_metal", MetalizerTile::new);
		regTile("engine_biomass", BiomassTile::new);

		regTile("machine_smelter", FurnaceTile::new);
		regTile("machine_farm_manager", FarmTile::new);
		regTile("machine_pulverizer", PulverizerTile::new);
		regTile("machine_compressor", CompressorTile::new);
		regTile("machine_beacon_simulator", BeaconTile::new);
		regTile("machine_mob_ripper", MobRipTile::new);
		regTile("machine_quarry", QuarryTile::new);
		regTile("machine_psionicant", PsionicantTile::new);
		regTile("machine_induction_furnace", IndfurTile::new);

		regTile("cable", CableTile::new);
		regTile("pipe", PipeTile::new);
		regTile("cell", CellTile::new);
	}

	@Deprecated
	public static void regTile(String id, BlockEntityType.BlockEntitySupplier<CmTileEntity> im) {

		BlockEntityType<? extends CmTileEntity> reg = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
				new ResourceLocation(TConst.modid, id),
				QuiltBlockEntityTypeBuilder.create(im, BlockInit.getBlock(id)).build());
		regs.put(id, reg);

	}

	public static BlockEntityType<? extends CmTileEntity> getType(String id) {

		return regs.get(id);

	}

}
