package ten3.core.network.packets;

import org.quiltmc.qsl.networking.api.PacketSender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import ten3.core.client.ClientHolder;
import ten3.core.network.Network;
import ten3.core.network.core.PTCPack;

public class PTCBindPack implements PTCPack {

	BlockPos bind, pos;

	public PTCBindPack(FriendlyByteBuf b) {
		bind = b.readBlockPos();
		pos = b.readBlockPos();
	}

	public final void writeBuffer(FriendlyByteBuf b) {
		b.writeBlockPos(bind);
		b.writeBlockPos(pos);
	}

	public PTCBindPack(BlockPos v, BlockPos pos) {
		this.pos = pos;
		this.bind = v;
	}

	public final void run(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf,
			PacketSender responseSender) {
		client.execute(() -> ClientHolder.binds.put(pos, bind));
	}

	@Override
	public ResourceLocation getId() {
		return Network.PTC_BIND_PACK;
	}

}
