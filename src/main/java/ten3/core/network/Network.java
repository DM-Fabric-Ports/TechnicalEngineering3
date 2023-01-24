package ten3.core.network;

import java.util.function.Function;

import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.PlayerLookup;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import ten3.TConst;
import ten3.core.network.check.PTCCheckPack;
import ten3.core.network.check.PTSCheckPack;
import ten3.core.network.core.PTCPack;
import ten3.core.network.core.PTSPack;
import ten3.core.network.packets.PTCInfoClientPack;
import ten3.core.network.packets.PTSModeTransfPack;
import ten3.core.network.packets.PTSRedStatePack;

public class Network {

	public static final ResourceLocation PTC_CHECK_PACK = TConst.asResource("ptc_check");
	public static final ResourceLocation PTS_CHECK_PACK = TConst.asResource("pts_check");

	public static final ResourceLocation PTC_BIND_PACK = TConst.asResource("ptc_bind");
	public static final ResourceLocation PTC_INFO_CLIENT_PACK = TConst.asResource("ptc_info_client");
	public static final ResourceLocation PTS_MODE_TRANSF_PACK = TConst.asResource("pts_mode_transf");
	public static final ResourceLocation PTS_RED_STATE_PACK = TConst.asResource("pts_red_state");

	public static void register() {
		registerPTSPacket(PTS_RED_STATE_PACK, PTSRedStatePack::new);
		registerPTSPacket(PTS_CHECK_PACK, PTSCheckPack::new);
		registerPTSPacket(PTS_MODE_TRANSF_PACK, PTSModeTransfPack::new);
	}

	public static void registerClient() {
		registerPTCPacket(PTC_CHECK_PACK, PTCCheckPack::new);
		registerPTCPacket(PTC_INFO_CLIENT_PACK, PTCInfoClientPack::new);
	}

	public static void sendToServer(PTSPack o) {
		o.send();
	}

	public static void sendToClient(PTCPack o, MinecraftServer server) {
		if (server != null)
			o.send(PlayerLookup.all(server));
	}

	private static void registerPTCPacket(ResourceLocation id, Function<FriendlyByteBuf, PTCPack> factory) {
		ClientPlayNetworking.ChannelReceiver rec = new ClientPlayNetworking.ChannelReceiver() {

			@Override
			public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf,
					PacketSender responseSender) {
				factory.apply(buf).run(client, handler, buf, responseSender);
			}

		};

		ClientPlayNetworking.registerGlobalReceiver(id, rec);
	}

	private static void registerPTSPacket(ResourceLocation id, Function<FriendlyByteBuf, PTSPack> factory) {
		ServerPlayNetworking.ChannelReceiver rec = new ServerPlayNetworking.ChannelReceiver() {

			@Override
			public void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
					FriendlyByteBuf buf, PacketSender responseSender) {
				factory.apply(buf).run(server, player, handler, buf, responseSender);
			}

		};

		ServerPlayNetworking.registerGlobalReceiver(id, rec);
	}

}
