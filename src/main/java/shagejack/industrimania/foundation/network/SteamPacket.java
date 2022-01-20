package shagejack.industrimania.foundation.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SteamPacket {

    private final double steam_mass;
    private final double steam_temp;
    private final int steam_state;

    public SteamPacket(double mass, double temp, int state) {
        this.steam_mass = mass;
        this.steam_temp = temp;
        this.steam_state = state;
    }

    SteamPacket(FriendlyByteBuf buffer) {
        this.steam_mass = buffer.readDouble();
        this.steam_temp = buffer.readDouble();
        this.steam_state = buffer.readVarInt();
    }

    void encode(FriendlyByteBuf buffer) {
        buffer.writeDouble(steam_mass);
        buffer.writeDouble(steam_temp);
        buffer.writeVarInt(steam_state);
    }

    void handle(NetworkEvent.Context context)
    {
        context.enqueueWork(() -> {


        });
    }
}
