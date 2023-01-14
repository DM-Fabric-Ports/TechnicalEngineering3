package ten3.core.network.receivers;

import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import ten3.core.network.Network;

public class PTCCheckPackReceiver implements ClientPlayNetworking.ChannelReceiver {

	@Override
	public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf,
			PacketSender responseSender) {
		client.execute(() -> ClientPlayNetworking.send(Network.PTS_CHECK, buf));
	}

}
