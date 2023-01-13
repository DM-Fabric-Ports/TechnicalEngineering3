package ten3.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import ten3.TConst;
import ten3.core.item.Spanner;
import ten3.core.item.energy.BlockItemFEStorage;
import ten3.core.item.energy.ItemFEStorage;
import ten3.core.item.upgrades.*;
import ten3.init.template.DefItem;
import ten3.init.template.DefItemBlock;
import ten3.init.template.InvisibleItem;

import static net.minecraft.world.item.Items.BUCKET;
import static ten3.lib.tile.CmTileMachine.kFE;

public class ItemInit {

    public static void regAll() {

        // Protected:
        regItem("pedia", new InvisibleItem());
        regItem("technical_item", new InvisibleItem());
        regItem("technical_block", new InvisibleItem());

        regItem("spanner", new Spanner());
        regItem("energy_capacity", new ItemFEStorage(kFE(500), kFE(5), kFE(5)));

        // produced things
        // regItemDef("energy_core");
        // regItemDef("machine_frame");
        regItemDef("redstone_conductor");
        regItemDef("redstone_converter");
        regItemDef("redstone_storer");
        regItemDef("indigo");
        regItemDef("azure_glass");
        regItemDef("royal_jelly");
        regItemDef("spicy_jelly");
        regItemDef("bizarrerie");
        // too imba
        // regItem("world_bag", new WorldBag());

        // base materials
        regPairMetal("iron", true);
        regPairMetal("gold", true);
        regPairMetal("copper", true);
        regPairMetal("tin", false);
        regPairMetal("nickel", false);
        regPairMetal("powered_tin", false);
        regPairMetal("chlorium", false);
        regItemDef("starlight_dust");

        // upgrades
        regItem("augmented_levelup", new LevelupAug());
        regItem("powered_levelup", new LevelupPower());
        regItem("relic_levelup", new LevelupAnc());
        regItem("range_levelup", new LevelupRg());
        regItem("photosyn_levelup", new LevelupSyn());

        // ores
        regItemBlockDef("tin_ore");
        // regItemBlockDef("copper_ore");
        regItemBlockDef("nickel_ore");
        regItemBlockDef("deep_tin_ore");
        regItemBlockDef("deep_nickel_ore");
        regItemDef("raw_tin");
        regItemDef("raw_nickel");

        // machines
        regItemMachineWithoutID("engine_extraction");
        regItemMachineWithoutID("engine_metal");
        regItemMachineWithoutID("engine_biomass");
        regItemMachine("smelter");
        regItemMachine("farm_manager");
        regItemMachine("pulverizer");
        regItemMachine("compressor");
        regItemMachine("beacon_simulator");
        regItemMachine("mob_ripper");
        regItemMachine("quarry");
        regItemMachine("psionicant");
        regItemMachine("induction_furnace");

        regItemMachineWithoutID("cell");
        regItemBlockDef("pipe");
        regItemBlockDef("cable");

		regBucket("molten_chlorium");

		regBucket("molten_powered_tin");
    }

    public static void regPairMetal(String id, boolean vanilla) {

        if (!vanilla) {
            regItemDef(id + "_ingot");
        }

        regItemDef(id + "_dust");
        regItemDef(id + "_plate");
        regItemDef(id + "_gear");

    }

    public static void regItemBlockDef(String id) {

        regItem(id, new DefItemBlock(BlockInit.getBlock(id)));

    }

    public static void regItemMachine(String id) {
        String idi = "machine_" + id;
        regItem(idi, new BlockItemFEStorage(BlockInit.getBlock(idi)));
    }

    public static void regItemMachineWithoutID(String id) {
        regItem(id, new BlockItemFEStorage(BlockInit.getBlock(id)));
    }

    public static void regItemDef(String id) {
        regItem(id, new DefItem());
    }

    public static void regItem(String id, Item im) {
        Registry.register(BuiltInRegistries.ITEM, TConst.asResource(id), im);
    }

	public static void regBucket(String id) {
		regItem(id + "_bucket", new BucketItem(FluidInit.getFluid(id), new Item.Properties().craftRemainder(BUCKET).stacksTo(1)));
	}

    public static Item getItem(String id) {
        return BuiltInRegistries.ITEM.get(TConst.asResource(id));
    }

}
