package ten3.core.network.receivers;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import io.netty.buffer.Unpooled;

import java.util.Objects;

public class PTCOpenGuiReceiver implements ClientPlayNetworking.ChannelReceiver {

	@Override
	public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf,
	                    PacketSender responseSender) {
		int typeId = buf.readVarInt();
		int syncId = buf.readVarInt();
		Component title = buf.readComponent();
		FriendlyByteBuf extraData =
				new FriendlyByteBuf(Unpooled.wrappedBuffer(buf.readByteArray(32600)));
		buf.retain();

		client.execute(() -> openScreen(typeId, syncId, title, extraData));
	}

	private void openScreen(int typeId, int syncId, Component title, FriendlyByteBuf buf) {
		try {
			MenuType<?> type = BuiltInRegistries.MENU.byId(typeId);

			if (!(type instanceof ExtendedScreenHandlerType<?>))
				return;

			assert Minecraft.getInstance().player != null;
			AbstractContainerMenu c = ((ExtendedScreenHandlerType<?>) type).create(syncId,
					Minecraft.getInstance().player.getInventory(), buf);
			@SuppressWarnings("unchecked")
			Screen s = ((MenuScreens.ScreenConstructor<AbstractContainerMenu, ?>) Objects.requireNonNull(MenuScreens
					.getConstructor(type))).create(c, Minecraft.getInstance().player.getInventory(),
					title);
			Minecraft.getInstance().player.containerMenu = ((MenuAccess<?>) s).getMenu();
			Minecraft.getInstance().setScreen(s);
		} finally {
			buf.release(); // Release the buf
		}
	}

}
