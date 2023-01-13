package ten3.util;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemUtil {

    public static ItemStack[] merge(ItemStack i1, ItemStack i2) {

        SimpleContainer inv = new SimpleContainer(2);
        inv.setItem(0, i1.copy());
        InventoryStorage wrapper = InventoryStorage.of(inv, null);
        TransferUtil.execute(t -> wrapper.insert(ItemVariant.of(i2), i2.getCount(), t));
        return wrapper.getSlots().stream().filter(s -> s.getAmount() > 0)
                .map(s -> s.getResource().toStack((int) s.getAmount())).toList().toArray(new ItemStack[0]);

    }

    public static void damage(ItemStack stack, Level world, int am) {

        if (stack.hurt(am, world.getRandom(), null)) {
            stack.setCount(0);
        }

    }

    public static void setTag(ItemStack stack, String name, int cr) {

        setTagD(stack, name, cr);

    }

    public static void tranTag(ItemStack stack, String name, int move) {

        setTagD(stack, name, getTag(stack, name) + move);

    }

    public static int getTag(ItemStack stack, String name) {

        return (int) getTagD(stack, name);

    }

    public static double getTagD(ItemStack stack, String name) {

        if (!stack.hasTag()) {
            return 0;
        }

        if (!stack.getTag().contains(name)) {
            return 0;
        }

        return stack.getTag().getDouble(name);

    }

    public static void setTagD(ItemStack stack, String name, double cr) {

        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putDouble(name, cr);
        stack.setTag(nbt);

    }

    public static void tranTagD(ItemStack stack, String name, double move) {

        setTagD(stack, name, getTagD(stack, name) + move);

    }

}
