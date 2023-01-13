package ten3.util;

public record StorageType(String type) {

	public static final StorageType ITEM = new StorageType("item");
	public static final StorageType FLUID = new StorageType("fluid");
	public static final StorageType ENERGY = new StorageType("energy");

}
