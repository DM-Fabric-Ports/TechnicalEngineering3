package ten3.core.network.check;

import org.quiltmc.qsl.networking.api.PacketSender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import ten3.core.network.Network;
import ten3.core.network.core.PTCPack;

public class PTCCheckPack implements PTCPack {

	public PTCCheckPack(FriendlyByteBuf buf) {
	}

	public PTCCheckPack() {
	}

	public void writeBuffer(FriendlyByteBuf b) {
	}

	@Override
	public void run(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf,
			PacketSender responseSender) {
		client.execute(this::handler);
	}

	public void handler() {
		Network.sendToServer(new PTSCheckPack());
	}

	@Override
	public ResourceLocation getId() {
		return Network.PTC_CHECK_PACK;
	}

}
