package ten3.core.network.receivers;

import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class PTSCheckPackReceiver implements ServerPlayNetworking.ChannelReceiver {

	public static boolean GET = false;

	@Override
	public void receive(MinecraftServer server, ServerPlayer player,
			ServerGamePacketListenerImpl handler, FriendlyByteBuf buf,
			PacketSender responseSender) {
		server.execute(() -> GET = true);
	}

}
