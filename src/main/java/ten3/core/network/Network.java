package ten3.core.network;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import ten3.TConst;
import ten3.core.network.receivers.PTCCheckPackReceiver;
import ten3.core.network.receivers.PTCInfoClientPackReceiver;
import ten3.core.network.receivers.PTCOpenGuiReceiver;
import ten3.core.network.receivers.PTSCheckPackReceiver;
import ten3.core.network.receivers.PTSRedStatePackReceiver;

public class Network {

	public static final ResourceLocation PTS_RED_STATE = TConst.asResource("pts_red_state");
	public static final ResourceLocation PTC_INFO_CLIENT = TConst.asResource("ptc_info_client");
	public static final ResourceLocation PTC_CHECK = TConst.asResource("ptc_check");
	public static final ResourceLocation PTS_CHECK = TConst.asResource("pts_check");
	public static final ResourceLocation PTC_OPEN_GUI = TConst.asResource("ptc_open_gui");

	public static void register() {
		ServerPlayNetworking.registerGlobalReceiver(PTS_RED_STATE, new PTSRedStatePackReceiver());
		ServerPlayNetworking.registerGlobalReceiver(PTS_CHECK, new PTSCheckPackReceiver());
	}

	@ClientOnly
	public static void registerClient() {
		ClientPlayNetworking.registerGlobalReceiver(PTC_INFO_CLIENT,
				new PTCInfoClientPackReceiver());
		ClientPlayNetworking.registerGlobalReceiver(PTC_CHECK, new PTCCheckPackReceiver());
		ClientPlayNetworking.registerGlobalReceiver(PTC_OPEN_GUI, new PTCOpenGuiReceiver());
	}

}
