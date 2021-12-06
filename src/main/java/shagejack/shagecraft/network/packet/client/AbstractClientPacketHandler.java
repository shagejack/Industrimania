package shagejack.shagecraft.network.packet.client;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import shagejack.shagecraft.network.packet.AbstractPacketHandler;

public abstract class AbstractClientPacketHandler<T extends IMessage> extends AbstractPacketHandler<T> {
    public AbstractClientPacketHandler() {
    }

    public final void handleServerMessage(EntityPlayerMP player, T message, MessageContext ctx) {

    }
}
