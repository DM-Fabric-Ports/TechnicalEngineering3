package ten3.core.machine.useenergy.beacon;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import ten3.core.item.upgrades.LevelupPotion;
import ten3.lib.tile.extension.CmTileMachineRadiused;
import ten3.lib.tile.option.Type;
import ten3.lib.wrapper.SlotCustomCm;

public class BeaconTile extends CmTileMachineRadiused {

	public BeaconTile(BlockPos pos, BlockState state) {

		super(pos, state);

		info.setCap(kFE(20));
		setEfficiency(300);
		initialRadius = 32;

		addSlot(new SlotCustomCm(inventory, 0, 79, 31, PotionBrewing::isIngredient, false, false));
	}

	@Override
	public Type typeOf() {
		return Type.MACHINE_EFFECT;
	}

	public void effect() {
		AABB axisalignedbb = (new AABB(worldPosition)).inflate(radius).expandTowards(0, level.getHeight(), 0);
		List<Player> list = level.getEntitiesOfClass(Player.class, axisalignedbb);

		ItemStack its = inventory.getItem(0);
		Potion pt = PotionUtils.getPotion(its);

		if (its.isEmpty())
			return;

		for (Player Player : list) {
			Player.addEffect(new MobEffectInstance(pt.getEffects().get(0).getEffect(), 40 * 10, match(), true, true));
		}
	}

	public boolean conditionStart() {
		return !inventory.getItem(0).isEmpty();
	}

	public double seconds() {
		return 10;
	}

	private int match() {
		if (upgradeSlots.countUpgrade(LevelupPotion.class) > 0) {
			return 1;
		}
		return 0;
	}

}
