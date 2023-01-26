package ten3.plugin.emi.util;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.emi.emi.EmiClient;
import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.TagEmiIngredient;
import dev.emi.emi.config.EmiConfig;
import dev.emi.emi.mixin.accessor.ItemRendererAccessor;
import dev.emi.emi.screen.tooltip.RemainderTooltipComponent;
import dev.emi.emi.screen.tooltip.TagTooltipComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

public class FluidTagEmiIngredient implements EmiIngredient {
	private final ResourceLocation id;
	private List<EmiStack> stacks;
	public final TagKey<Fluid> key;
	private final long amount;

	public FluidTagEmiIngredient(TagKey<Fluid> key, long amount) {
		this(key,
				BuiltInRegistries.FLUID.getTag(key).isPresent()
						? BuiltInRegistries.FLUID.getTag(key).get().stream().map(holder -> EmiStack.of(holder.value()))
								.toList()
						: List.of(),
				amount);
	}

	public FluidTagEmiIngredient(TagKey<Fluid> key, List<EmiStack> stacks, long amount) {
		this.id = key.location();
		this.key = key;
		this.stacks = stacks;
		this.amount = amount;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TagEmiIngredient tag && tag.key.equals(this.key);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public List<EmiStack> getEmiStacks() {
		return stacks;
	}

	@Override
	public long getAmount() {
		return amount;
	}

	@Override
	public void render(PoseStack matrices, int x, int y, float delta, int flags) {
		Minecraft client = Minecraft.getInstance();

		if ((flags & RENDER_ICON) != 0) {
			if (!EmiClient.MODELED_TAGS.contains(id)) {
				if (stacks.size() > 0) {
					stacks.get(0).render(matrices, x, y, delta, -1 ^ RENDER_AMOUNT);
				}
			} else {
				BakedModel model = client.getModelManager()
						.getModel(new ModelResourceLocation("emi", "tags/" + id.getNamespace() + "/" + id.getPath(),
								"inventory"));

				PoseStack vs = RenderSystem.getModelViewStack();
				vs.pushPose();
				vs.translate(x, y, 100.0f);
				vs.translate(8.0, 8.0, 0.0);
				vs.scale(1.0f, -1.0f, 1.0f);
				vs.scale(16.0f, 16.0f, 16.0f);
				RenderSystem.applyModelViewMatrix();

				PoseStack ms = new PoseStack();
				model.getTransforms().getTransform(ItemTransforms.TransformType.GUI).apply(false, ms);
				ms.translate(-0.5, -0.5, -0.5);

				if (!model.usesBlockLight()) {
					Lighting.setupForFlatItems();
				}
				MultiBufferSource.BufferSource immediate = client.renderBuffers().bufferSource();
				((ItemRendererAccessor) client.getItemRenderer())
						.invokeRenderBakedItemModel(model,
								ItemStack.EMPTY, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ms,
								ItemRenderer.getFoilBufferDirect(immediate,
										Sheets.translucentItemSheet(), true, false));
				immediate.endBatch();

				if (!model.usesBlockLight()) {
					Lighting.setupFor3DItems();
				}

				vs.popPose();
				RenderSystem.applyModelViewMatrix();
			}
		}
		if ((flags & RENDER_AMOUNT) != 0) {
			String count = "";
			if (amount != 1) {
				count += amount;
			}
			client.getItemRenderer().renderGuiItemDecorations(client.font, stacks.get(0).getItemStack(), x, y, count);
		}
		if ((flags & RENDER_INGREDIENT) != 0) {
			EmiRender.renderTagIcon(this, matrices, x, y);
		}
		if ((flags & RENDER_REMAINDER) != 0) {
			EmiRender.renderRemainderIcon(this, matrices, x, y);
		}
	}

	@Override
	public List<ClientTooltipComponent> getTooltip() {
		String translation = EmiUtil.translateId("tag.", id);
		List<ClientTooltipComponent> list = Lists.newArrayList();
		if (I18n.exists(translation)) {
			list.add(ClientTooltipComponent.create(EmiPort.ordered(EmiPort.translatable(translation))));
			if (EmiUtil.showAdvancedTooltips()) {
				list.add(ClientTooltipComponent
						.create(EmiPort.ordered(EmiPort.literal("#" + id, ChatFormatting.DARK_GRAY))));
			}
		} else {
			list.add(ClientTooltipComponent.create(EmiPort.ordered(EmiPort.literal("#" + id))));
		}
		if (EmiConfig.appendModId) {
			String mod = EmiUtil.getModName(id.getNamespace());
			list.add(ClientTooltipComponent
					.create(EmiPort.ordered(EmiPort.literal(mod, ChatFormatting.BLUE, ChatFormatting.ITALIC))));
		}
		list.add(new TagTooltipComponent(stacks));
		for (EmiStack stack : stacks) {
			if (!stack.getRemainder().isEmpty()) {
				list.add(new RemainderTooltipComponent(this));
				break;
			}
		}
		return list;
	}
}
