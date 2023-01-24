package ten3.lib.client.element;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import ten3.lib.client.RenderHelper;
import ten3.lib.tile.CmContainerMachine;
import ten3.util.KeyUtil;
import ten3.util.PatternUtil;

public class ElementFluid extends ElementBase {

	double p;
	boolean dv;
	int id;
	FluidVariant variant;
	long amount = 0;

	public ElementFluid(int id, int x, int y, int width, int height, int xOff, int yOff,
			ResourceLocation resourceLocation) {
		super(x, y, width, height, xOff, yOff, resourceLocation);
		this.id = id;
	}

	public ElementFluid(int id, int x, int y, int width, int height, int xOff, int yOff,
			ResourceLocation resourceLocation, boolean displayValue) {
		super(x, y, width, height, xOff, yOff, resourceLocation);
		dv = displayValue;
		this.id = id;
	}

	public void update(CmContainerMachine ct) {
		Fluid fid = BuiltInRegistries.FLUID.byId(ct.fluidData.get(id));
		amount = ct.fluidAmount.get(id);
		variant = FluidVariant.of(fid);
		setValue(amount, ct.tile.tanks.get(id).getCapacity());
		setPer(val / (double) m_val);
	}

	@Override
	public void draw(PoseStack matrixStack) {
		int h = (int) ((height - 2) * (1 - p));
		RenderHelper.render(matrixStack, x, y, width, height, textureW, textureH, xOff, yOff, resourceLocation);

		if (variant == null || variant.isBlank() || amount <= 0)
			return;
		RenderHelper.drawFlTil(matrixStack, variant.getFluid(), x + 1, y + 1 + h, width - 2, height - h - 2);
	}

	@Override
	public void addToolTip(List<Component> tooltips) {
		if (!dv) {
			tooltips.add(KeyUtil.make((int) (p * 100) + "%"));
		} else {
			tooltips.add(PatternUtil.joinmB(val, m_val));
		}
	}

	int val;
	int m_val;

	public void setValue(long v, long mv) {
		val = (int) v;
		m_val = (int) mv;
	}

	public void setPer(double per) {
		p = per;
	}

}
