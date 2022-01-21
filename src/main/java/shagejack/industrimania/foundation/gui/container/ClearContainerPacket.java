package shagejack.industrimania.foundation.gui.container;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;
import shagejack.industrimania.foundation.network.packet.SimplePacketBase;

public class ClearContainerPacket extends SimplePacketBase {

	public ClearContainerPacket() {}

	public ClearContainerPacket(FriendlyByteBuf buffer) {}

	@Override
	public void write(FriendlyByteBuf buffer) {}

	@Override
	public void handle(Supplier<Context> context) {
		context.get()
			.enqueueWork(() -> {
				ServerPlayer player = context.get()
					.getSender();
				if (player == null)
					return;
				if (!(player.containerMenu instanceof IClearableContainer))
					return;
				((IClearableContainer) player.containerMenu).clearContents();
			});
		context.get()
			.setPacketHandled(true);
	}

}
