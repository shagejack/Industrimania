package shagejack.industrimania.foundation.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.TargetPoint;
import net.minecraftforge.network.simple.SimpleChannel;

import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.primalAge.block.woodenFaucet.WoodenFaucetFluidPacket;
import shagejack.industrimania.content.steamAge.steam.SteamUpdatePacket;
import shagejack.industrimania.foundation.network.packet.FilteringCountUpdatePacket;
import shagejack.industrimania.foundation.network.packet.SimplePacketBase;


import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER;

/**
 * @author Create Team
 */
public enum AllPackets {
    //Client to Server
    CONFIGURE_FILTERING_AMOUNT(FilteringCountUpdatePacket.class, FilteringCountUpdatePacket::new, PLAY_TO_SERVER),


    //Server to Client
    STEAM_UPDATE(SteamUpdatePacket.class, SteamUpdatePacket::new, PLAY_TO_CLIENT),
    WOODEN_FAUCET_FLUID(WoodenFaucetFluidPacket.class, WoodenFaucetFluidPacket::new, PLAY_TO_CLIENT)

    ;

    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(Industrimania.MOD_ID, "main");
    public static final int PROTOCOL_VERSION = 1;
    public static final String PROTOCOL_VERSION_STR = String.valueOf(PROTOCOL_VERSION);
    public static SimpleChannel CHANNEL;

    private LoadedPacket<?> packet;

    <T extends SimplePacketBase> AllPackets(Class<T> type, Function<FriendlyByteBuf, T> factory,
                                            NetworkDirection direction) {
        packet = new LoadedPacket<>(type, factory, direction);
    }

    public static void registerPackets() {
        CHANNEL = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .serverAcceptedVersions(PROTOCOL_VERSION_STR::equals)
                .clientAcceptedVersions(PROTOCOL_VERSION_STR::equals)
                .networkProtocolVersion(() -> PROTOCOL_VERSION_STR)
                .simpleChannel();
        for (AllPackets packet : values())
            packet.packet.register();
    }

    public static void sendToNear(Level world, BlockPos pos, Object message) {
        CHANNEL.send(PacketDistributor.NEAR
                .with(TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), 256, world.dimension())), message);
    }

    public static void sendToNear(Level world, BlockPos pos, int range, Object message) {
        CHANNEL.send(PacketDistributor.NEAR
                .with(TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), range, world.dimension())), message);
    }

    private static class LoadedPacket<T extends SimplePacketBase> {
        private static int index = 0;

        private BiConsumer<T, FriendlyByteBuf> encoder;
        private Function<FriendlyByteBuf, T> decoder;
        private BiConsumer<T, Supplier<Context>> handler;
        private Class<T> type;
        private NetworkDirection direction;

        private LoadedPacket(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkDirection direction) {
            encoder = T::write;
            decoder = factory;
            handler = T::handle;
            this.type = type;
            this.direction = direction;
        }

        private void register() {
            CHANNEL.messageBuilder(type, index++, direction)
                    .encoder(encoder)
                    .decoder(decoder)
                    .consumer(handler)
                    .add();
        }
    }

}
