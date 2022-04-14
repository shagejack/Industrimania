package shagejack.industrimania.content.steamAge.steam;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import shagejack.industrimania.foundation.network.packet.TileEntityDataPacket;
import shagejack.industrimania.foundation.tileEntity.SyncedTileEntity;

public class SteamUpdatePacket extends TileEntityDataPacket<SyncedTileEntity> {

    protected SteamStack steam;

    public SteamUpdatePacket(FriendlyByteBuf buffer) {
        super(buffer);
        this.steam = SteamStack.readFromPacket(buffer);
    }

    public SteamUpdatePacket(BlockPos pos, SteamStack steam) {
        super(pos);
        this.steam = steam;
    }

    @Override
    protected void writeData(FriendlyByteBuf buffer) {
        steam.writeToPacket(buffer);
    }

    @Override
    protected void handlePacket(SyncedTileEntity tile) {
        if (tile instanceof ISteamPacketReceiver) {
            ((ISteamPacketReceiver) tile).updateSteamTo(this.steam);
        }
    }

    public interface ISteamPacketReceiver {
        void updateSteamTo(SteamStack stack);
    }

}
