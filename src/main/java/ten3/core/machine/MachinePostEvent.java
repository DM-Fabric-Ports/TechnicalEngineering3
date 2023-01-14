package ten3.core.machine;

import static ten3.lib.tile.CmTileMachine.RED_MODE;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PlayerLookup;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import ten3.core.item.Spanner;
import ten3.core.item.upgrades.UpgradeItem;
import ten3.core.network.Network;
import ten3.init.ItemInit;
import ten3.lib.tile.CmTileMachine;
import ten3.lib.tile.option.FaceOption;
import ten3.lib.tile.option.RedstoneMode;
import ten3.lib.tile.option.Type;
import ten3.lib.tile.recipe.CmTileMachineRadiused;
import ten3.util.ItemUtil;
import ten3.util.TranslateKeyUtil;

public class MachinePostEvent {

	/**
	 * @return can open gui
	 */
	public static boolean clickMachineEvent(Level worldIn, BlockPos pos, Player player,
			BlockHitResult hit) {

		CmTileMachine tile = (CmTileMachine) worldIn.getBlockEntity(pos);
		Direction d = hit.getDirection();
		if (tile == null) {
			return false;
		}
		ItemStack i = player.getMainHandItem();

		if (!player.isShiftKeyDown() && i.getItem() == ItemInit.getItem("spanner")) {
			int ene = tile.direCheckEnergy(d);
			int itm = tile.direCheckItem(d);
			int res = tile.data.get(RED_MODE);
			itm++;
			ene++;
			res++;
			if (itm >= FaceOption.size())
				itm = 0;
			if (ene >= FaceOption.size())
				ene = 0;
			if (res >= RedstoneMode.size())
				res = 0;

			if (ItemUtil.getTag(i, "mode") == Spanner.Modes.ENERGY.getIndex()) {
				tile.setOpenEnergy(d, ene);
			} else if (ItemUtil.getTag(i, "mode") == Spanner.Modes.ITEM.getIndex()) {
				tile.setOpenItem(d, itm);
			} else if (ItemUtil.getTag(i, "mode") == Spanner.Modes.REDSTONE.getIndex()) {
				tile.data.set(RED_MODE, res);
			}
			updateToClient(tile, d, pos);
			return false;
		} else if (i.getItem() instanceof UpgradeItem && tile.typeOf() != Type.CABLE) {
			boolean success = ((UpgradeItem) i.getItem()).effect(tile);
			boolean giveSuc = tile.itr.selfGive(i.copy(), CmTileMachine.upgSlotFrom,
					CmTileMachine.upgSlotTo, false);
			if (success && giveSuc) {
				player.sendSystemMessage(TranslateKeyUtil.translated(TranslateKeyUtil.GREEN,
						i.getDisplayName().getString(), "ten3.info.upgrade_successfully"));
				if (!player.isCreative()) {
					i.shrink(1);
				}
			} else if (!giveSuc) {
				player.sendSystemMessage(TranslateKeyUtil.translated(TranslateKeyUtil.RED,
						"ten3.info.too_much_upgrades"));
			} else {
				player.sendSystemMessage(TranslateKeyUtil.translated(TranslateKeyUtil.RED,
						"ten3.info.not_support_upgrade"));
			}
			updateToClient(tile, d, pos);
			return false;
		}

		return true;

	}

	public static void updateToClient(BlockEntity be, int ene, int itm, int res, int lv, int rd,
			BlockPos pos, Direction d) {
		FriendlyByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(pos);
		buf.writeIntIdList(IntList.of(ene, itm, res, lv, rd, d.get3DDataValue()));

		ServerPlayNetworking.send(PlayerLookup.tracking(be), Network.PTC_INFO_CLIENT, buf);
	}

	public static void updateToClient(CmTileMachine tile, Direction d, BlockPos pos) {
		updateToClient(tile, tile.direCheckEnergy(d), tile.direCheckItem(d),
				tile.data.get(RED_MODE), tile.levelIn,
				tile instanceof CmTileMachineRadiused ? ((CmTileMachineRadiused) tile).radius : 0,
				pos, d);
	}

}
