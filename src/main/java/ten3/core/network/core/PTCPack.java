package ten3.core.network.core;

import java.util.Collection;
import java.util.List;

import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public interface PTCPack {

	void run(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf,
			PacketSender responseSender);

	void writeBuffer(FriendlyByteBuf buf);

	default void send(Collection<ServerPlayer> players) {
		FriendlyByteBuf buf = PacketByteBufs.create();
		writeBuffer(buf);
		ServerPlayNetworking.send(players, getId(), buf);
	}

	default void send(ServerPlayer player) {
		send(List.of(player));
	}

	ResourceLocation getId();

}
