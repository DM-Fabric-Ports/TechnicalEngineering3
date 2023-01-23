package ten3.lib.client;

import static net.minecraft.client.gui.GuiComponent.blit;
import static net.minecraft.client.gui.GuiComponent.drawCenteredString;
import static net.minecraft.client.gui.GuiComponent.drawString;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import ten3.lib.client.element.ElementBase;

public class RenderHelper {

	static ResourceLocation SHADER_BLOCK = new ResourceLocation("textures/atlas/blocks.png");

	// * stolen from CoFH Core thanks *
	public static void drawFlTil(PoseStack matrixStack, Fluid fluid, int x, int y, int width, int height) {
		if (fluid == Fluids.EMPTY)
			return;
		TextureAtlasSprite icon = Minecraft.getInstance()
				.getBlockRenderer().getBlockModelShaper()
				.getBlockModel(fluid.defaultFluidState().createLegacyBlock())
				.getParticleIcon();
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		int color = FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidColor(null, null, fluid.defaultFluidState());
		float red = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >> 8 & 255) / 255.0F;
		float blue = (float) (color & 255) / 255.0F;
		RenderSystem.setShaderColor(red, green, blue, 1.0F);
		RenderSystem.setShaderTexture(0, SHADER_BLOCK);

		int drawHeight;
		int drawWidth;

		for (int i = 0; i < width; i += 16) {
			for (int j = 0; j < height; j += 16) {
				drawWidth = Math.min(width - i, 16);
				drawHeight = Math.min(height - j, 16);
				drawSprite(matrixStack, icon, x + i, y + j, drawWidth, drawHeight);
			}
		}
	}

	public static void drawSprite(PoseStack matrixStack, TextureAtlasSprite icon, int x, int y, int width, int height) {
		float minU = icon.getU0();
		float maxU = icon.getU1();
		float minV = icon.getV0();
		float maxV = icon.getV1();

		float u = minU + (maxU - minU) * width / 16F;
		float v = minV + (maxV - minV) * height / 16F;

		Matrix4f matrix = matrixStack.last().pose();

		BufferBuilder buffer = Tesselator.getInstance().getBuilder();
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		buffer.vertex(matrix, x, y + height, 0).uv(minU, v).endVertex();
		buffer.vertex(matrix, x + width, y + height, 0).uv(u, v).endVertex();
		buffer.vertex(matrix, x + width, y, 0).uv(u, minV).endVertex();
		buffer.vertex(matrix, x, y, 0).uv(minU, minV).endVertex();
		Tesselator.getInstance().end();
	}

	public static void drawAll(ArrayList<ElementBase> widgets, PoseStack matrixStack) {

		for (ElementBase widget : widgets) {
			if (widget.isVisible())
				widget.draw(matrixStack);
		}

	}

	public static void updateAll(ArrayList<ElementBase> widgets) {

		for (ElementBase widget : widgets) {
			if (widget.isVisible())
				widget.update();
		}

	}

	public static void hangingAll(ArrayList<ElementBase> widgets, boolean hang, int mouseX, int mouseY) {

		for (ElementBase widget : widgets) {
			if (widget.isVisible())
				widget.hangingEvent(hang, mouseX, mouseY);
		}

	}

	public static void clickAll(ArrayList<ElementBase> widgets, int mouseX, int mouseY) {

		// ItemStack ret = stack;

		for (ElementBase widget : widgets) {
			if (widget.checkInstr(mouseX, mouseY) && widget.isVisible()) {
				/*
				 * if(widgets.getRecipe(i) instanceof ElementSlot) {
				 * ItemStack in = ((ElementSlot) widgets.getRecipe(i)).item;
				 * if(!stack.isEmpty() && in.isEmpty()) {
				 * in = stack.copy();
				 * ret = ItemStack.EMPTY;
				 * }
				 * else if(stack.isEmpty() && !in.isEmpty()) {
				 * ret = in.copy();
				 * in.setCount(0);
				 * }
				 * else if(!stack.isEmpty() && !in.isEmpty()) {
				 * if(stack.getRecipe() != in.getRecipe()) {
				 * ItemStack cac = stack.copy();
				 * ret = in.copy();
				 * in = cac;
				 * }
				 * }
				 * }
				 */
				widget.onMouseClicked(mouseX, mouseY);
			}
		}

		// return ret;

	}

	public static void bindTexture(ResourceLocation resourceLocation) {

		RenderSystem.setShaderTexture(0, resourceLocation);

	}

	public static void renderBackGround(PoseStack matrixStack, int i, int j, int textureW, int textureH,
			ResourceLocation resourceLocation) {

		bindTexture(resourceLocation);
		render(matrixStack, i, j, 176, 166, textureW, textureH, 0, 0, resourceLocation);

	}

	public static void renderBackGround(PoseStack matrixStack, int w, int h, int i, int j, int textureW, int textureH,
			ResourceLocation resourceLocation) {

		bindTexture(resourceLocation);
		render(matrixStack, i, j, w, h, textureW, textureH, 0, 0, resourceLocation);

	}

	public static void render(PoseStack matrixStack, int x, int y, int width, int height, int textureW, int textureH,
			int xOff, int yOff, ResourceLocation resourceLocation) {

		bindTexture(resourceLocation);
		blit(matrixStack, x, y, xOff, yOff, width, height, textureW, textureH);

	}

	@SuppressWarnings("all")
	public static void renderString(PoseStack matrixStack, int x, int y, int color, Component str) {

		Font font = Minecraft.getInstance().font;
		drawString(matrixStack, font, str, x, y, color);

	}

	@SuppressWarnings("all")
	public static void renderCString(PoseStack matrixStack, int x, int y, int color, Component str) {

		Font font = Minecraft.getInstance().font;
		drawCenteredString(matrixStack, font, str, x, y, color);

	}

}
