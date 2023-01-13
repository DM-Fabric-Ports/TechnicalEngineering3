package ten3.core.item.energy;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyItem;
import ten3.core.machine.Cell;
import ten3.init.TileInit;
import ten3.init.template.DefItemBlock;
import ten3.lib.tile.CmTileEntity;
import ten3.lib.tile.CmTileMachine;
import ten3.util.ItemUtil;

public class BlockItemFEStorage extends DefItemBlock implements SimpleEnergyItem {

	// in game item do

	private CmTileMachine getBind() {
		return (CmTileMachine) CmTileEntity.ofType(TileInit.getType(BuiltInRegistries.ITEM.getKey(this).getPath()));
	}

	public BlockItemFEStorage(Block b) {
		super(b, 1);

		EnergyStorage.ITEM.registerForItems((stack, ctx) -> {
			return SimpleEnergyItem.createStorage(ctx, getEnergyCapacity(stack), getEnergyMaxInput(stack),
					getEnergyMaxOutput(stack));
		}, this);
	}

	@Override
	public Component getName(ItemStack stack) {
		int level = ItemUtil.getTag(stack, "level");
		CmTileMachine t = getBind();
		t.levelIn = level;
		return t.getDisplayWith();
	}

	@Override
	public ItemStack getDefaultInstance() {
		CmTileMachine t = getBind();
		return EnergyItemHelper.getState(this, t.maxStorage, t.maxReceive, t.maxExtract);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return ItemUtil.getTag(stack, "energy") != 0;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		if (ItemUtil.getTag(stack, "maxEnergy") == 0)
			return 0;
		return (int) (13 * (ItemUtil.getTag(stack, "energy") / (double) ItemUtil.getTag(stack, "maxEnergy")));
	}

	@Override
	public int getBarColor(ItemStack p_150901_) {
		return Mth.color(1f, 0.1f, 0.1f);
	}

	public void fillItemCategory(FabricItemGroupEntries entry) {
		CmTileMachine t = getBind();
		EnergyItemHelper.fillEmpty(this, entry, t.maxStorage, t.maxReceive, t.maxExtract);

		if (getBlock() instanceof Cell) {
			EnergyItemHelper.fillFull(this, entry, t.maxStorage, t.maxReceive, t.maxExtract);
		}

	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level p_40573_, List<Component> tooltip,
			TooltipFlag p_40575_) {
		EnergyItemHelper.addTooltip(tooltip, stack);
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level p_41448_, Player p_41449_) {
		CmTileMachine t = getBind();
		EnergyItemHelper.setState(stack, t.maxStorage, t.maxReceive, t.maxExtract);
	}

	@Override
	public long getEnergyCapacity(ItemStack stack) {
		return ItemUtil.getTag(stack, "maxEnergy");
	}

	@Override
	public long getEnergyMaxInput(ItemStack stack) {
		return ItemUtil.getTag(stack, "receive");
	}

	@Override
	public long getEnergyMaxOutput(ItemStack stack) {
		return ItemUtil.getTag(stack, "extract");
	}

}
