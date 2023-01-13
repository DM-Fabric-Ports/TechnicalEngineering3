package ten3.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import ten3.TConst;
import ten3.core.block.OreCm;
import ten3.core.machine.*;

public class BlockInit {

    public static void regAll() {
        regBlock("tin_ore", new OreCm(3));
        regBlock("nickel_ore", new OreCm(4));
        regBlock("deepslate_tin_ore", new OreCm(4));
        regBlock("deepslate_nickel_ore", new OreCm(5));

        regEngine("engine_extraction");
        regEngine("engine_metal");
        regEngine("engine_biomass");

        regMachine("smelter");
        regMachine("farm_manager");
        regMachine("pulverizer");
        regMachine("compressor");
        regMachine("beacon_simulator");
        regMachine("mob_ripper");
        regMachine("quarry");
        regMachine("psionicant");
        regMachine("induction_furnace");

        regCable("cable", Material.GLASS, SoundType.GLASS);
        regPipe("pipe", Material.METAL, SoundType.METAL);
        regCell("cell", Material.GLASS, SoundType.GLASS);

		regFluid("molten_chlorium", FluidInit.getFluid("molten_chlorium"), 15);
		regFluid("molten_powered_tin", FluidInit.getFluid("molten_powered_tin"), 14);
    }

    public static void regMachine(String id_out) {
        String id = "machine_" + id_out;
        regBlock(id_out, new Machine(id));
    }

	public static void regFluid(String id, Fluid fluid, int light) {
		regBlock(id, new LiquidBlock(
				(FlowingFluid) fluid, BlockBehaviour.Properties.of(Material.LAVA).noCollission().strength(100.0F).lightLevel(state -> light).noLootTable()
		));
	}

    public static void regEngine(String id) {
        regBlock(id, new Engine(id));
    }

    public static void regCell(String id, Material m, SoundType s) {

        regBlock(id, new Cell(m, s, id));

    }

    public static void regCable(String id, Material m, SoundType s) {
        regBlock(id, new CableBased(m, s, id));
    }

    public static void regPipe(String id, Material m, SoundType s) {
        regBlock(id, new PipeBased(m, s, id));
    }

    public static void regBlock(String id, Block im) {
        Registry.register(BuiltInRegistries.BLOCK, TConst.asResource(id), im);
    }

    public static Block getBlock(String id) {
        return BuiltInRegistries.BLOCK.get(TConst.asResource(id));
    }

}
