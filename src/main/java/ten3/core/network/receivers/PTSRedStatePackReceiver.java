package ten3.core.network.receivers;

import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import ten3.lib.tile.CmTileMachine;

public class PTSRedStatePackReceiver implements ServerPlayNetworking.ChannelReceiver {

	@Override
	public void receive(MinecraftServer server, ServerPlayer player,
			ServerGamePacketListenerImpl handler, FriendlyByteBuf buf,
			PacketSender responseSender) {
		server.execute(() -> {
			if (player.getLevel().getBlockEntity(buf.readBlockPos()) instanceof CmTileMachine tile)
				tile.data.set(CmTileMachine.RED_MODE, buf.readInt());
		});
	}

}
