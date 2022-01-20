package shagejack.industrimania.foundation.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PlayerDataSyncPacket {

    public PlayerDataSyncPacket() {
    }

    PlayerDataSyncPacket(FriendlyByteBuf buffer) {
    }

    void encode(FriendlyByteBuf buffer) {
    }

    void handle(NetworkEvent.Context context)
    {
        context.enqueueWork(() -> {
        });
    }
}
