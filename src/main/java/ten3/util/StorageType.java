package ten3.util;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;

public class StorageType<T extends TransferVariant<?>> {

    private StorageType() {
    }

    public static final StorageType<ItemVariant> ITEM = new StorageType<>();
    public static final StorageType<ItemVariant> FLUID = new StorageType<>();

}
