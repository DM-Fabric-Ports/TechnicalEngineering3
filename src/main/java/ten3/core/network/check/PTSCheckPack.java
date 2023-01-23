package ten3.core.network.check;

import org.quiltmc.qsl.networking.api.PacketSender;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import ten3.core.network.core.PTSPack;

public class PTSCheckPack implements PTSPack {

	public static boolean GET = false;

	public PTSCheckPack(FriendlyByteBuf b) {
	}

	public PTSCheckPack() {
	}

	public void writeBuffer(FriendlyByteBuf b) {
	}

	public void run(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
			FriendlyByteBuf buf, PacketSender responseSender) {
		server.execute(this::handler);
	}

	public void handler() {
		GET = true;
	}

	@Override
	public ResourceLocation getId() {
		return null;
	}

}
