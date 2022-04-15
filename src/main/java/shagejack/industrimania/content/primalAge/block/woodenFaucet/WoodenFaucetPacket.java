package shagejack.industrimania.content.primalAge.block.woodenFaucet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import shagejack.industrimania.foundation.network.packet.TileEntityDataPacket;

public class WoodenFaucetPacket extends TileEntityDataPacket<WoodenFaucetTileEntity> {
    private final boolean isPouring;

    public WoodenFaucetPacket(FriendlyByteBuf buffer) {
        super(buffer);
        this.isPouring = buffer.readBoolean();
    }

    public WoodenFaucetPacket(BlockPos pos, boolean isPouring) {
        super(pos);
        this.isPouring = isPouring;
    }

    @Override
    protected void writeData(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.isPouring);
    }

    @Override
    protected void handlePacket(WoodenFaucetTileEntity tile) {
        tile.handlePacket(this.isPouring);
    }

}
