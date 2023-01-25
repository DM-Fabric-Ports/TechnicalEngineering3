package ten3.core.item.energy;

import java.util.List;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyItem;
import ten3.init.tab.DefGroup;
import ten3.init.template.DefItem;
import ten3.util.ItemUtil;

public class ItemFEStorage extends DefItem implements SimpleEnergyItem {

	int sto, rec, ext;

	public ItemFEStorage(int s, int r, int e) {
		super(build(1));
		sto = s;
		rec = r;
		ext = e;
		EnergyStorage.ITEM.registerForItems((stack, ctx) -> SimpleEnergyItem.createStorage(ctx,
				getEnergyCapacity(stack), getEnergyMaxInput(stack), getEnergyMaxOutput(stack)), this);
	}

	@Override
	public ItemStack getDefaultInstance() {
		return EnergyItemHelper.getState(this, sto, rec, ext);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return ItemUtil.getTag(stack, "energy") != 0;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		if (ItemUtil.getTag(stack, "maxEnergy") == 0)
			return 0;
		return (int) (13 * (ItemUtil.getTag(stack, "energy") / (double) sto));
	}

	@Override
	public int getBarColor(ItemStack p_150901_) {
		return Mth.color(1f, 0.1f, 0.1f);
	}

	public void fillItemCategory(FabricItemGroupEntries entries) {
		EnergyItemHelper.fillEmpty(this, entries, sto, rec, ext);
		EnergyItemHelper.fillFull(this, entries, sto, rec, ext);
	}

	@Override
	public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level p_40573_,
			List<Component> tooltip, TooltipFlag p_40575_) {
		EnergyItemHelper.addTooltip(tooltip, stack);
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level p_41448_, Player p_41449_) {
		EnergyItemHelper.setState(stack, sto, rec, ext);
	}

	@Override
	public long getEnergyCapacity(ItemStack stack) {
		return stack.getTag().getInt("maxEnergy");
	}

	@Override
	public long getEnergyMaxInput(ItemStack stack) {
		return stack.getTag().getInt("receive");
	}

	@Override
	public long getEnergyMaxOutput(ItemStack stack) {
		return stack.getTag().getInt("extract");
	}

	@Override
	public CreativeModeTab getTab() {
		return DefGroup.TOOL;
	}

}
