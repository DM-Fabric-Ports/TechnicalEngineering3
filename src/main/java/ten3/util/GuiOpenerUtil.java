package ten3.util;

import java.util.function.Consumer;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class GuiOpenerUtil {
	public static void openGui(ServerPlayer player, MenuProvider containerProvider, BlockPos pos) {
		openGui(player, containerProvider, buf -> buf.writeBlockPos(pos));
	}

	public static void openGui(ServerPlayer player, MenuProvider factory, Consumer<FriendlyByteBuf> extraDataWriter) {
		player.doCloseContainer();
		player.nextContainerCounter();
		int openContainerId = player.containerCounter;

		FriendlyByteBuf extraData = new FriendlyByteBuf(Unpooled.buffer());
		extraDataWriter.accept(extraData);
		extraData.readerIndex(0);
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		AbstractContainerMenu menu = factory.createMenu(openContainerId, player.getInventory(), player);
		assert menu != null;
		buf.writeVarInt(BuiltInRegistries.MENU.getId(menu.getType()));
		buf.writeVarInt(openContainerId);
		buf.writeComponent(factory.getDisplayName());
		buf.writeVarInt(extraData.readableBytes());
		buf.writeBytes(extraData);

		//TODO
		// ServerPlayNetworking.send(player, Network.PTC_OPEN_GUI, buf);

		player.containerMenu = menu;
		player.initMenu(menu);
	}

}
