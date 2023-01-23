package ten3.init;

import static ten3.init.template.DefItem.build;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import ten3.TConst;
import ten3.core.item.Spanner;
import ten3.core.item.energy.BlockItemFEStorage;
import ten3.core.item.upgrades.LevelupAug;
import ten3.core.item.upgrades.LevelupBlast;
import ten3.core.item.upgrades.LevelupIce;
import ten3.core.item.upgrades.LevelupKnow;
import ten3.core.item.upgrades.LevelupMagma;
import ten3.core.item.upgrades.LevelupMineral;
import ten3.core.item.upgrades.LevelupPotion;
import ten3.core.item.upgrades.LevelupPower;
import ten3.core.item.upgrades.LevelupRg;
import ten3.core.item.upgrades.LevelupShulker;
import ten3.core.item.upgrades.LevelupSmoke;
import ten3.core.item.upgrades.LevelupStream;
import ten3.core.item.upgrades.LevelupSyn;
import ten3.core.machine.useenergy.compressor.Mould;
import ten3.init.template.DefItem;
import ten3.init.template.DefItemBlock;

public class ItemInit {

	public static void regAll() {

		// Protected:
		// regItem("pedia", InvisibleItem::new);
		// regItem("technical_item", InvisibleItem::new);
		// regItem("technical_block", InvisibleItem::new);

		regItem("spanner", Spanner::new);
		regItem("mould_gear", Mould::new);
		regItem("mould_plate", Mould::new);
		regItem("mould_rod", Mould::new);
		regItem("mould_string", Mould::new);
		// regItem("energy_capacity", () -> new ItemFEStorage(kFE(500), kFE(5),
		// kFE(5)));

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
		regItemDef("starlight_dust");

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

		// upgrades
		regItem("augmented_levelup", LevelupAug::new);
		regItem("powered_levelup", LevelupPower::new);
		regItem("relic_levelup", LevelupShulker::new);
		regItem("photosyn_levelup", LevelupSyn::new);
		regItem("range_levelup", LevelupRg::new);
		regItem("smoke_levelup", LevelupSmoke::new);
		regItem("blast_levelup", LevelupBlast::new);
		regItem("potion_levelup", LevelupPotion::new);
		regItem("stream_levelup", LevelupStream::new);
		regItem("knowledge_levelup", LevelupKnow::new);
		regItem("ice_levelup", LevelupIce::new);
		regItem("magma_levelup", LevelupMagma::new);
		regItem("mineral_levelup", LevelupMineral::new);

		// ores
		regItemBlockDef("tin_ore");
		regItemBlockDef("nickel_ore");
		regItemBlockDef("deep_tin_ore");
		regItemBlockDef("deep_nickel_ore");
		regItemDef("raw_tin");
		regItemDef("raw_nickel");
		regItemBlockDef("raw_tin_block");
		regItemBlockDef("raw_nickel_block");

		// machines
		regItemMachineWithoutID("engine_extraction");
		regItemMachineWithoutID("engine_metal");
		regItemMachineWithoutID("engine_biomass");
		regItemMachineWithoutID("engine_solar");
		regItemMachine("smelter");
		regItemMachine("farm_manager");
		regItemMachine("pulverizer");
		regItemMachine("compressor");
		regItemMachine("beacon_simulator");
		regItemMachine("mob_ripper");
		regItemMachine("quarry");
		regItemMachine("psionicant");
		regItemMachine("induction_furnace");
		regItemMachine("enchantment_flusher");

		regItemMachineWithoutID("cell");
		regItemBlockDefInMac("pipe");
		regItemBlockDefInMac("pipe_white");
		regItemBlockDefInMac("pipe_black");
		regItemBlockDefInMac("cable");
		regItemBlockDefInMac("cable_quartz");
		regItemBlockDefInMac("cable_azure");
		regItemBlockDefInMac("cable_star");
		// regItemBlockDef("pole");

	}

	public static void regPairMetal(String id, boolean vanilla) {

		if (!vanilla) {
			regItemDef(id + "_ingot");
			regItemDef(id + "_nugget");
			regItemBlockDef(id + "_block");
		}

		regItemDef(id + "_dust");
		regItemDef(id + "_plate");
		regItemDef(id + "_gear");
	}

	public static void regItemBlockDef(String id) {
		regItem(id, () -> new DefItemBlock(BlockInit.getBlock(id), build(64)));
	}

	public static void regItemBlockDefInMac(String id) {
		regItemBlockDef(id);
	}

	public static void regItemMachine(String id) {
		String idi = "machine_" + id;
		regItem(idi, () -> new BlockItemFEStorage(BlockInit.getBlock(idi)));
	}

	public static void regItemMachineWithoutID(String id) {
		regItem(id, () -> new BlockItemFEStorage(BlockInit.getBlock(id)));
	}

	public static void regItemDef(String id) {
		regItem(id, DefItem::new);
	}

	public static void regItem(String id, Supplier<Item> im) {
		Registry.register(BuiltInRegistries.ITEM, TConst.asResource(id), im.get());
	}

	public static Item getItem(String id) {
		return BuiltInRegistries.ITEM.get(TConst.asResource(id));
	}

}
