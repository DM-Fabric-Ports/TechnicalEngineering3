package ten3.core.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import ten3.core.network.Network;
import ten3.lib.tile.mac.CmTileMachine;

public class PTSRedStatePack extends ServerIntSetterPacket {

	public PTSRedStatePack(FriendlyByteBuf b) {
		super(b);
	}

	public PTSRedStatePack(int mode, BlockPos pos) {
		super(mode, pos);
	}

	@Override
	protected void run(BlockEntity tile) {
		((CmTileMachine) tile).data.set(CmTileMachine.RED_MODE, value);
	}

	@Override
	public ResourceLocation getId() {
		return Network.PTS_RED_STATE_PACK;
	}

}
