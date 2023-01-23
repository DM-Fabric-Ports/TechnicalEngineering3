package ten3.core.item.energy;

import static ten3.init.template.DefItem.build;

import java.util.List;

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
import ten3.lib.tile.mac.CmTileEntity;
import ten3.lib.tile.mac.CmTileMachine;
import ten3.util.ItemUtil;

public class BlockItemFEStorage extends DefItemBlock implements SimpleEnergyItem {

	// in game item do

	private CmTileMachine getBind() {
		return (CmTileMachine) CmTileEntity.ofType(TileInit.getType(BuiltInRegistries.ITEM.getKey(this).getPath()));
	}

	public BlockItemFEStorage(Block b) {
		super(b, build(1));
		EnergyStorage.ITEM.registerForItems((stack, ctx) -> SimpleEnergyItem.createStorage(ctx,
				getEnergyCapacity(stack), getEnergyMaxInput(stack), getEnergyMaxOutput(stack)), this);
	}

	@Override
	public Component getName(ItemStack stack) {
		CmTileMachine t = getBind();
		return t.getDisplayWith();
	}

	@Override
	public ItemStack getDefaultInstance() {
		CmTileMachine t = getBind();
		return EnergyItemHelper.getState(this, t.info.maxStorageEnergy, t.info.maxReceiveEnergy,
				t.info.maxExtractEnergy);
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

	public void fillItemCategory(FabricItemGroupEntries entries) {
		CmTileMachine t = getBind();
		EnergyItemHelper.fillEmpty(this, entries, t.info.maxStorageEnergy, t.info.maxReceiveEnergy,
				t.info.maxExtractEnergy);

		if (getBlock() instanceof Cell) {
			EnergyItemHelper.fillFull(this, entries, t.info.maxStorageEnergy, t.info.maxReceiveEnergy,
					t.info.maxExtractEnergy);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level p_40573_,
			List<Component> tooltip, TooltipFlag p_40575_) {
		EnergyItemHelper.addTooltip(tooltip, stack);
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level p_41448_, Player p_41449_) {
		CmTileMachine t = getBind();
		EnergyItemHelper.setState(stack, t.info.maxStorageEnergy, t.info.maxReceiveEnergy, t.info.maxExtractEnergy);
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

}
