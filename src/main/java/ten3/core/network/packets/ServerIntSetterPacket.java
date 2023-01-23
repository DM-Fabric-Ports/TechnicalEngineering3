package ten3.core.network.packets;

import org.quiltmc.qsl.networking.api.PacketSender;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import ten3.core.network.core.PTSPack;

public abstract class ServerIntSetterPacket implements PTSPack {

	int value;
	BlockPos pos;

	public ServerIntSetterPacket(FriendlyByteBuf b) {

		value = b.readInt();
		pos = b.readBlockPos();

	}

	public ServerIntSetterPacket(int v, BlockPos pos) {

		this.value = v;
		this.pos = pos;

	}

	public final void writeBuffer(FriendlyByteBuf b) {

		b.writeInt(value);
		b.writeBlockPos(pos);

	}

	public final void run(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
			FriendlyByteBuf buf, PacketSender responseSender) {
		server.execute(() -> {
			try {
				handler(player);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	protected final void handler(Player player) {
		if (player == null)
			return;
		Level world = player.level;
		BlockEntity e = world.getBlockEntity(pos);
		if (e != null) {
			run(e);
		}
	}

	protected void run(BlockEntity tile) {

	}

}
