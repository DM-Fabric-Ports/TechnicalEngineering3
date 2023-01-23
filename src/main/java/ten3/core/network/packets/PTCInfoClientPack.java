package ten3.core.network.packets;

import java.util.ArrayList;

import org.quiltmc.qsl.networking.api.PacketSender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import ten3.core.client.ClientHolder;
import ten3.core.network.Network;
import ten3.core.network.core.PTCPack;
import ten3.lib.tile.extension.CmTileMachineRadiused;
import ten3.lib.tile.mac.CmTileMachine;
import ten3.util.DireUtil;

public class PTCInfoClientPack implements PTCPack {

	int ene;
	int itm;
	int res;
	int rd;
	int fld;
	BlockPos pos;
	Direction d;

	public static void send(CmTileMachine t) {
		for (Direction d : Direction.values()) {
			Network.sendToClient(new PTCInfoClientPack(t, d), t.getLevel().getServer());
		}
	}

	public PTCInfoClientPack(FriendlyByteBuf b) {

		ene = b.readInt();
		itm = b.readInt();
		fld = b.readInt();
		res = b.readInt();
		rd = b.readInt();
		pos = b.readBlockPos();
		d = b.readEnum(Direction.class);

	}

	public PTCInfoClientPack(CmTileMachine t, Direction d) {

		ene = t.info.direCheckEnergy(d);
		itm = t.info.direCheckItem(d);
		fld = t.info.direCheckFluid(d);
		res = t.data.get(CmTileMachine.RED_MODE);
		pos = t.getBlockPos();
		if (t instanceof CmTileMachineRadiused) {
			rd = ((CmTileMachineRadiused) t).radius;
		}
		this.d = d;

	}

	public void writeBuffer(FriendlyByteBuf b) {

		b.writeInt(ene);
		b.writeInt(itm);
		b.writeInt(fld);
		b.writeInt(res);
		b.writeInt(rd);
		b.writeBlockPos(pos);
		b.writeEnum(d);

	}

	public void run(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf,
			PacketSender responseSender) {
		client.execute(this::handler);
	}

	@SuppressWarnings("unused")
	public void handler() {
		ArrayList<Integer> energy = ClientHolder.energy.get(pos);
		ArrayList<Integer> item = ClientHolder.item.get(pos);
		ArrayList<Integer> fluid = ClientHolder.fluid.get(pos);

		if (energy == null) {
			energy = new ArrayList<>(6);
			for (Direction ignore : Direction.values()) {
				energy.add(0);
			}
		}
		if (item == null) {
			item = new ArrayList<>(6);
			for (Direction ignore : Direction.values()) {
				item.add(0);
			}
		}
		if (fluid == null) {
			fluid = new ArrayList<>(6);
			for (Direction ignore : Direction.values()) {
				fluid.add(0);
			}
		}

		int i = DireUtil.direToInt(d);
		energy.set(i, ene);
		item.set(i, itm);
		fluid.set(i, fld);

		ClientHolder.energy.put(pos, energy);
		ClientHolder.item.put(pos, item);
		ClientHolder.fluid.put(pos, fluid);
		ClientHolder.redstone.put(pos, res);
		ClientHolder.radius.put(pos, rd);
	}

	@Override
	public ResourceLocation getId() {
		return Network.PTC_INFO_CLIENT_PACK;
	}

}
