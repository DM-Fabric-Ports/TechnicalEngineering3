package ten3.core.network.packets;

import org.quiltmc.qsl.networking.api.PacketSender;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.block.entity.BlockEntity;
import ten3.core.network.Network;
import ten3.core.network.core.PTSPack;
import ten3.lib.tile.mac.CmTileMachine;
import ten3.lib.tile.option.FaceOption;

public class PTSModeTransfPack implements PTSPack {

	BlockPos pos;
	int changeType;
	Direction d;

	public PTSModeTransfPack(FriendlyByteBuf b) {
		pos = b.readBlockPos();
		changeType = b.readInt();
		d = b.readEnum(Direction.class);
	}

	public final void writeBuffer(FriendlyByteBuf b) {
		b.writeBlockPos(pos);
		b.writeInt(changeType);
		b.writeEnum(d);
	}

	public PTSModeTransfPack(BlockPos pos, int type, Direction d) {
		this.pos = pos;
		this.changeType = type;
		this.d = d;
	}

	public final void run(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
			FriendlyByteBuf buf, PacketSender responseSender) {
		if (player == null)
			return;
		BlockEntity be = player.getLevel().getBlockEntity(pos);
		if (be instanceof CmTileMachine) {
			CmTileMachine tile = (CmTileMachine) be;
			switch (changeType) {
				case 0:// energy
					int i = tile.info.direCheckEnergy(d);
					i++;
					if (i >= FaceOption.size())
						i = 0;
					tile.info.setOpenEnergy(d, i);
					break;
				case 1:// item
					int j = tile.info.direCheckItem(d);
					j++;
					if (j >= FaceOption.size())
						j = 0;
					tile.info.setOpenItem(d, j);
					break;
				case 2:
					int k = tile.info.direCheckFluid(d);
					k++;
					if (k >= FaceOption.size())
						k = 0;
					tile.info.setOpenFluid(d, k);
					break;
			}
			PTCInfoClientPack.send(tile);// return to client after run
		}
	}

	@Override
	public ResourceLocation getId() {
		return Network.PTS_MODE_TRANSF_PACK;
	}

}
