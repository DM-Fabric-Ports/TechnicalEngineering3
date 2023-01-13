package ten3.core.item.energy;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import ten3.lib.tile.CmTileMachine;
import ten3.util.ComponentUtil;
import ten3.util.ItemUtil;

import java.util.List;

import static ten3.lib.tile.CmTileMachine.ENERGY;

@SuppressWarnings("ALL")
public class EnergyItemHelper {

	public static void addTooltip(List<Component> tooltips, ItemStack stack) {

		double e = ItemUtil.getTag(stack, "energy");
		double me = ItemUtil.getTag(stack, "maxEnergy");

        tooltips.add(ComponentUtil.join(e, me));

	}

	public static void setState(ItemStack s, int sto, int rec, int ext) {

		ItemUtil.setTag(s, "receive", rec);
		ItemUtil.setTag(s, "extract", ext);
		ItemUtil.setTag(s, "maxEnergy", sto);

	}

	public static ItemStack getState(Item i, int sto, int rec, int ext) {

		ItemStack def = new ItemStack(i);
		setState(def, sto, rec, ext);

		return def;

	}

	public static void fillFull(Item i, FabricItemGroupEntries stacks, int sto, int rec, int ext) {
		ItemStack full = getState(i, sto, rec, ext);
		ItemUtil.setTag(full, "energy", sto);
		stacks.accept(full);

	}

	public static void fillEmpty(Item i, FabricItemGroupEntries stacks, int sto, int rec, int ext) {

		ItemStack def = getState(i, sto, rec, ext);
		stacks.accept(def);

	}

	// MACHINES:

	public static ItemStack fromMachine(CmTileMachine tile, ItemStack stack) {

		ItemUtil.setTag(stack, "energy", tile.data.get(ENERGY));
		ItemUtil.setTag(stack, "receive", tile.maxReceive);
		ItemUtil.setTag(stack, "extract", tile.maxExtract);
		ItemUtil.setTag(stack, "maxEnergy", tile.maxStorage);
		tile.nbtManager.writeNBTUpg(stack.getOrCreateTag());

		for (int i = CmTileMachine.upgSlotFrom; i <= CmTileMachine.upgSlotTo; i++) {
			stack.getOrCreateTag().put("upg" + i, tile.inventory.getItem(i).getTag());
		}

		return stack;

	}

	public static void pushToTile(CmTileMachine tile, ItemStack stack) {

		tile.data.set(ENERGY, ItemUtil.getTag(stack, "energy"));
		tile.nbtManager.readNBTUpg(stack.getOrCreateTag());// it's not nec to set caps.

		for (int i = CmTileMachine.upgSlotFrom; i <= CmTileMachine.upgSlotTo; i++) {
			CompoundTag nbt = stack.getOrCreateTag().getCompound("upg" + i);
			tile.inventory.setItem(i, ItemStack.of(nbt));
		}

	}

}
