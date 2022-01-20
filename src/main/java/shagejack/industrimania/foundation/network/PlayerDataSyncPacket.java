package shagejack.industrimania.foundation.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PlayerDataSyncPacket {

    public PlayerDataSyncPacket(String particleOptions, double x, double y, double z, double vx, double vy, double vz) {
    }

    PlayerDataSyncPacket(FriendlyByteBuf buffer)
    {
    }

    void encode(FriendlyByteBuf buffer)
    {
    }

    void handle(NetworkEvent.Context context)
    {
        context.enqueueWork(() -> {
        });
    }
}
