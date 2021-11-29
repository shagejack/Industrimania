package shagejack.minecraftology.network;

import io.netty.channel.ChannelHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import shagejack.minecraftology.Reference;
import shagejack.minecraftology.network.packet.AbstractBiPacketHandler;
import shagejack.minecraftology.network.packet.client.AbstractClientPacketHandler;
import shagejack.minecraftology.network.packet.server.PacketSendMachineNBT;

@ChannelHandler.Sharable
public class PacketPipeline {
    public final SimpleNetworkWrapper dispatcher;
    protected int packetID;

    public PacketPipeline() {
        dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.CHANNEL_NAME);
        packetID = 0;
    }

    public void registerPackets() {
        registerBiPacket(PacketSendMachineNBT.BiHandler.class, PacketSendMachineNBT.class);
    }

    public <REQ extends IMessage, REPLY extends IMessage> void registerPacket(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType) {
        try {
            Side side = AbstractClientPacketHandler.class.isAssignableFrom(messageHandler) ? Side.CLIENT : Side.SERVER;
            dispatcher.registerMessage(messageHandler, requestMessageType, packetID++, side);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <REQ extends IMessage, REPLY extends IMessage> void registerBiPacket(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType) {
        if (AbstractBiPacketHandler.class.isAssignableFrom(messageHandler)) {
            dispatcher.registerMessage(messageHandler, requestMessageType, packetID, Side.CLIENT);
            dispatcher.registerMessage(messageHandler, requestMessageType, packetID++, Side.SERVER);
        } else {
            throw new IllegalArgumentException("Cannot register " + messageHandler.getName() + " on both sides - must extend AbstractBiMessageHandler!");
        }
    }


    public void sendToServer(IMessage message) {
        dispatcher.sendToServer(message);
    }

    public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
        dispatcher.sendToAllAround(message, point);
    }

    public void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range) {
        dispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
    }

    public void sendToAllAround(IMessage message, EntityPlayer player, double range) {
        dispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(player.world.provider.getDimension(), player.posX, player.posY, player.posZ, range));
    }

    public void sendToAllAround(IMessage message, TileEntity tileEntity, double range) {
        dispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(tileEntity.getWorld().provider.getDimension(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ(), range));
    }

    public void sendTo(IMessage message, EntityPlayerMP player) {
        dispatcher.sendTo(message, player);
    }

    public void sendToDimention(IMessage message, int dimention) {
        dispatcher.sendToDimension(message, dimention);
    }

    public void sendToDimention(IMessage message, World world) {
        sendToDimention(message, world.provider);
    }

    public void sendToDimention(IMessage message, WorldProvider worldProvider) {
        dispatcher.sendToDimension(message, worldProvider.getDimension());
    }

}
