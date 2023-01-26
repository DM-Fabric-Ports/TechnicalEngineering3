package ten3.plugin.emi.util;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.emi.emi.api.render.EmiRenderable;
import ten3.TConst;
import ten3.lib.client.RenderHelper;
import ten3.lib.client.element.ElementProgress;

public class ProcessDraw implements EmiRenderable {

	int u, v, wi, hi, ru, rv, rx, ry;
    double per, p;
    String n;
    ElementProgress progress;

    public ProcessDraw(int u, int v, int w, int h, String name, int rowU, int rowV, int rowX, int rowY)
    {
        wi = w;hi = h;ru = rowU;rv = rowV;rx = rowX;ry = rowY;n = name;this.u=u;this.v=v;
        progress = new ElementProgress(rx, ry, 22, 16, ru, rv, TConst.guiHandler);
    }

    public void cacheTime(int t)
    {
        per = 1.0 / t;
    }

	@Override
	public void render(PoseStack matrices, int x, int y, float delta) {
		p += per * 0.2;
        if(p >= 1) p = 0;

        RenderHelper.render(matrices, 0, 0, wi, hi, 256, 256, u, v, TConst.emiHandler);

        progress.draw(matrices);
        progress.setPer(p);
	}

}
