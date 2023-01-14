package ten3.core.network;

import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import ten3.TConst;
import ten3.core.network.receivers.PTSRedStatePackReceiver;

public class Network {

	public static final ResourceLocation PTS_RED_STATE = TConst.asResource("pts_red_state");
	public static final ResourceLocation PTC_INFO_CLIENT = TConst.asResource("ptc_info_client");

	public static void register() {

		ServerPlayNetworking.registerGlobalReceiver(PTS_RED_STATE, new PTSRedStatePackReceiver());

		// instance = NetworkRegistry.newSimpleChannel
		// (
		// new ResourceLocation(TConst.modid, "ten3_network_handler"),
		// () -> "1.0",
		// (v) -> true,
		// (v) -> true
		// );
		// instance.registerMessage
		// (
		// id++,
		// PTSRedStatePack.class,
		// PTSRedStatePack::writeBuffer,
		// PTSRedStatePack::new,
		// PTSRedStatePack::run
		// );
		// instance.registerMessage
		// (
		// id++,
		// PTSCheckPack.class,
		// PTSCheckPack::writeBuffer,
		// PTSCheckPack::new,
		// PTSCheckPack::run
		// );
		// instance.registerMessage
		// (
		// id++,
		// PTCCheckPack.class,
		// PTCCheckPack::writeBuffer,
		// PTCCheckPack::new,
		// PTCCheckPack::run
		// );
		// instance.registerMessage
		// (
		// id++,
		// PTCInfoClientPack.class,
		// PTCInfoClientPack::writeBuffer,
		// PTCInfoClientPack::new,
		// PTCInfoClientPack::run
		// );

	}

}
