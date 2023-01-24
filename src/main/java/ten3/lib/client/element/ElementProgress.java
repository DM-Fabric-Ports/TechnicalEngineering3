package ten3.lib.client.element;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ten3.lib.client.RenderHelper;
import ten3.lib.tile.CmContainerMachine;
import ten3.lib.wrapper.IntArrayCm;
import ten3.util.KeyUtil;

public class ElementProgress extends ElementBase {

	double p;
	boolean dv;

	public ElementProgress(int x, int y, int width, int height, int xOff, int yOff, ResourceLocation resourceLocation) {

		super(x, y, width, height, xOff, yOff, resourceLocation);

	}

	public ElementProgress(int x, int y, int width, int height, int xOff, int yOff, ResourceLocation resourceLocation,
			boolean display) {

		super(x, y, width, height, xOff, yOff, resourceLocation);
		dv = display;
	}

	@Override
	public void draw(PoseStack matrixStack) {

		RenderHelper.render(matrixStack, x, y, width, height, textureW, textureH, xOff, yOff, resourceLocation);

		RenderHelper.bindTexture(resourceLocation);
		RenderHelper.render(matrixStack, x, y, (int) (p * width), height, textureW, textureH, xOff, yOff + height,
				resourceLocation);

	}

	@Override
	public void addToolTip(List<Component> tooltips) {
		if (dv)
			tooltips.add(KeyUtil.make((int) (p * 100) + "%"));
	}

	int ie;
	int je;

	public void setEs(int i1, int i2, CmContainerMachine c) {

		IntArrayCm ia = c.data;
		ie = ia.get(i1);
		je = ia.get(i2);

	}

	public void setPer(double per) {

		p = per;

	}

}
