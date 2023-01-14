package ten3.init;

import java.awt.Color;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import ten3.TConst;
import ten3.core.fluid.FluidCm;

public class FluidInit {

	public static void regAll() {
		quickRegFluid("molten_chlorium");
		quickRegFluid("molten_powered_tin");
	}

	@ClientOnly
	public static void clientInit() {
		fluidRenderer("molten_chlorium", Color.GREEN);
		fluidRenderer("molten_powered_tin", 0x78CFE8);
	}

	public static void regFluid(String id, Fluid fluid) {
		Registry.register(BuiltInRegistries.FLUID, TConst.asResource(id), fluid);
	}

	public static void quickRegFluid(String id, Block block, Item bucket) {
		regFluid("flowing_" + id,
				new FluidCm.Flowing(block, bucket, getFluid(id), getFluid("flowing_" + id)));
		regFluid(id, new FluidCm.Source(block, bucket, getFluid(id), getFluid("flowing_" + id)));
	}

	public static void quickRegFluid(String id) {
		quickRegFluid(id, BlockInit.getBlock(id), ItemInit.getItem(id + "_bucket"));
	}

	public static Fluid getFluid(String id) {
		return BuiltInRegistries.FLUID.get(TConst.asResource(id));
	}

	@ClientOnly
	public static void fluidRenderer(String id, Color color) {
		FluidRenderHandlerRegistry.INSTANCE.register(getFluid(id), getFluid("flowing_" + id),
				new SimpleFluidRenderHandler(new ResourceLocation("block/lava_still"),
						new ResourceLocation("block/lava_flow"), color.getRGB()));
	}

	@ClientOnly
	public static void fluidRenderer(String id, int rgb) {
		fluidRenderer(id, new Color(rgb));
	}
}
