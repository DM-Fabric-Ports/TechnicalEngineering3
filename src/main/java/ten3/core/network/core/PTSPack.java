package ten3.core.network.core;

import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public interface PTSPack {

	void run(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf,
			PacketSender responseSender);

	void writeBuffer(FriendlyByteBuf buf);

	default void send() {
		FriendlyByteBuf buf = PacketByteBufs.create();
		writeBuffer(buf);
		ClientPlayNetworking.send(getId(), buf);
	}

	ResourceLocation getId();

}
