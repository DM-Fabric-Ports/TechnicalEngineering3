package ten3.lib.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ten3.lib.client.element.ElementBase;

import java.util.ArrayList;

import static net.minecraft.client.gui.GuiComponent.*;

public class RenderHelper {

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

        //ItemStack ret = stack;

		for (ElementBase widget : widgets) {
			if (widget.checkInstr(mouseX, mouseY) && widget.isVisible()) {
                /*
                if(widgets.getRcp(i) instanceof ElementSlot) {
                    ItemStack in = ((ElementSlot) widgets.getRcp(i)).item;
                    if(!stack.isEmpty() && in.isEmpty()) {
                        in = stack.copy();
                        ret = ItemStack.EMPTY;
                    }
                    else if(stack.isEmpty() && !in.isEmpty()) {
                        ret = in.copy();
                        in.setCount(0);
                    }
                    else if(!stack.isEmpty() && !in.isEmpty()) {
                        if(stack.getRcp() != in.getRcp()) {
                            ItemStack cac = stack.copy();
                            ret = in.copy();
                            in = cac;
                        }
                    }
                }
                 */
				widget.onMouseClicked(mouseX, mouseY);
			}
		}

        //return ret;

    }

    public static void bindTexture(ResourceLocation resourceLocation) {

        RenderSystem.setShaderTexture(0, resourceLocation);

    }

    public static void renderBackGround(PoseStack matrixStack, int i, int j, int textureW, int textureH, ResourceLocation resourceLocation) {

        bindTexture(resourceLocation);
        render(matrixStack,i, j, 176, 166, textureW, textureH, 0, 0, resourceLocation);

    }

    public static void renderBackGround(PoseStack matrixStack, int w, int h, int i, int j, int textureW, int textureH, ResourceLocation resourceLocation) {

        bindTexture(resourceLocation);
        render(matrixStack,i, j, w, h, textureW, textureH, 0, 0, resourceLocation);

    }

    public static void render(PoseStack matrixStack, int x, int y, int width, int height, int textureW, int textureH, int xOff, int yOff, ResourceLocation resourceLocation) {

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
