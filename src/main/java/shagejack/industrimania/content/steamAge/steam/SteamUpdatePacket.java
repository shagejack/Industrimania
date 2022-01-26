package shagejack.industrimania.content.steamAge.steam;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import shagejack.industrimania.foundation.network.packet.SimplePacketBase;
import shagejack.industrimania.foundation.tileEntity.SyncedTileEntity;

import java.util.function.Supplier;

public class SteamUpdatePacket extends SimplePacketBase {

    protected SteamStack stack;

    public SteamUpdatePacket(FriendlyByteBuf buffer) {
        this.stack = SteamStack.readFromPacket(buffer);
    }

    public SteamUpdatePacket(SteamStack stack) {
        this.stack = stack;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        stack.writeToPacket(buffer);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get()
                .enqueueWork(() -> {
                    //TODO: Handle Packet

                });
        context.get()
                .setPacketHandled(true);
    }

}
