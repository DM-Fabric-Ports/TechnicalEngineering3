package ten3.init;

import java.util.ArrayList;
import java.util.List;

import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import ten3.TConst;
import ten3.core.machine.cell.CellScreen;
import ten3.core.machine.engine.EngineScreen;
import ten3.core.machine.engine.solar.SolarScreen;
import ten3.core.machine.pipe.PipeScreen;
import ten3.core.machine.useenergy.beacon.BeaconScreen;
import ten3.core.machine.useenergy.compressor.CompressorScreen;
import ten3.core.machine.useenergy.encflu.EncfluScreen;
import ten3.core.machine.useenergy.farm.FarmScreen;
import ten3.core.machine.useenergy.indfur.IndfurScreen;
import ten3.core.machine.useenergy.mobrip.MobRipScreen;
import ten3.core.machine.useenergy.psionicant.PsionicantScreen;
import ten3.core.machine.useenergy.pulverizer.PulverizerScreen;
import ten3.core.machine.useenergy.quarry.QuarryScreen;
import ten3.core.machine.useenergy.smelter.FurnaceScreen;
import ten3.lib.tile.CmContainerMachine;
import ten3.lib.tile.mac.CmTileMachine;
import ten3.lib.wrapper.IntArrayCm;

public class ContInit {

	public static void regAll() {
		regCont("engine_extraction");
		regCont("engine_metal");
		regCont("engine_biomass");
		regCont("engine_solar");

		regCont("machine_smelter");
		regCont("machine_farm_manager");
		regCont("machine_pulverizer");
		regCont("machine_compressor");
		regCont("machine_beacon_simulator");
		regCont("machine_mob_ripper");
		regCont("machine_quarry");
		regCont("machine_psionicant");
		regCont("machine_induction_furnace");
		regCont("machine_enchantment_flusher");

		regCont("cell");
		regCont("pipe_white");
		regCont("pipe_black");
	}

	public static IntArrayCm createDefaultIntArr() {
		return new IntArrayCm(40);
	}

	public static void regCont(String id) {
		Registry.register(BuiltInRegistries.MENU, TConst.asResource(id),
				new ExtendedScreenHandlerType<>((windowId, inv, data) -> {
					BlockPos pos = data.readBlockPos();
					return new CmContainerMachine(windowId, id,
							(CmTileMachine) TileInit.getType(id).create(pos, inv.player.level.getBlockState(pos)),
							inv, pos, createDefaultIntArr(), createDefaultIntArr(), createDefaultIntArr());
				}));
	}

	public static MenuType<?> getType(String id) {
		return BuiltInRegistries.MENU.get(TConst.asResource(id));
	}

	public static boolean hasType(String id) {
		return BuiltInRegistries.MENU.getOptional(TConst.asResource(id)).isPresent();
	}

	static List<String> translucent = new ArrayList<>();
	static List<String> cutout = new ArrayList<>();

	public static void regClient() {
		translucent.add("cable");
		translucent.add("pipe");
		translucent.add("pipe_white");
		translucent.add("pipe_black");
		translucent.add("cell");

		cutout.add("engine_metal");
		cutout.add("engine_extraction");
		cutout.add("engine_biomass");

		bindScr("engine_metal", EngineScreen::new);
		bindScr("engine_extraction", EngineScreen::new);
		bindScr("engine_biomass", EngineScreen::new);
		bindScr("engine_solar", SolarScreen::new);

		bindScr("machine_smelter", FurnaceScreen::new);
		bindScr("machine_farm_manager", FarmScreen::new);
		bindScr("machine_pulverizer", PulverizerScreen::new);
		bindScr("machine_compressor", CompressorScreen::new);
		bindScr("machine_beacon_simulator", BeaconScreen::new);
		bindScr("machine_mob_ripper", MobRipScreen::new);
		bindScr("machine_quarry", QuarryScreen::new);
		bindScr("machine_psionicant", PsionicantScreen::new);
		bindScr("machine_induction_furnace", IndfurScreen::new);
		bindScr("machine_enchantment_flusher", EncfluScreen::new);

		bindScr("cell", CellScreen::new);
		bindScr("pipe_white", PipeScreen::new);
		bindScr("pipe_black", PipeScreen::new);

		for (String s : translucent) {
			BlockRenderLayerMap.put(RenderType.translucent(), BlockInit.getBlock(s));
		}
		for (String s : cutout) {
			BlockRenderLayerMap.put(RenderType.cutout(), BlockInit.getBlock(s));
		}
	}

	@SuppressWarnings("all")
	private static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void bindScr(String s,
			MenuScreens.ScreenConstructor<M, U> fac) {
		MenuScreens.register((MenuType<? extends M>) getType(s), fac);
	}

}
