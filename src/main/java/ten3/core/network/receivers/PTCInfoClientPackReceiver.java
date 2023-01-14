package ten3.core.network.receivers;

import java.util.ArrayList;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import ten3.core.client.ClientHolder;

public class PTCInfoClientPackReceiver implements ClientPlayNetworking.ChannelReceiver {

	@Override
	public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf,
			PacketSender responseSender) {
		BlockPos pos = buf.readBlockPos();
		ArrayList<Integer> energy = ClientHolder.energy.get(pos);
		ArrayList<Integer> item = ClientHolder.item.get(pos);
		IntList ls = buf.readIntIdList();
		int redstone;
		int lev;

		if (energy == null) {
			energy = new ArrayList<>();
			for (int i = 0; i < Direction.values().length; i++)
				energy.add(0);
		}
		if (item == null) {
			item = new ArrayList<>();
			for (int i = 0; i < Direction.values().length; i++)
				item.add(0);
		}

		int i = ls.getInt(5);
		energy.set(i, ls.getInt(0));
		item.set(i, ls.getInt(1));
		redstone = ls.getInt(2);
		lev = ls.getInt(3);

		ClientHolder.energy.put(pos, energy);
		ClientHolder.item.put(pos, item);
		ClientHolder.redstone.put(pos, redstone);
		ClientHolder.level.put(pos, lev);
		ClientHolder.radius.put(pos, ls.getInt(4));
	}

}
