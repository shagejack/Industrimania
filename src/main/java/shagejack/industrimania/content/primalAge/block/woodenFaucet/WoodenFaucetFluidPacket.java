package shagejack.industrimania.content.primalAge.block.woodenFaucet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import shagejack.industrimania.foundation.network.packet.TileEntityDataPacket;

public class WoodenFaucetFluidPacket extends TileEntityDataPacket<WoodenFaucetTileEntity> {

    private final FluidStack renderFluid;
    private final boolean isPouring;

    public WoodenFaucetFluidPacket(FriendlyByteBuf buffer) {
        super(buffer);
        this.renderFluid = buffer.readFluidStack();
        this.isPouring = buffer.readBoolean();
    }

    public WoodenFaucetFluidPacket(BlockPos pos, FluidStack renderFluid, boolean isPouring) {
        super(pos);
        this.renderFluid = renderFluid;
        this.isPouring = isPouring;
    }

    @Override
    protected void writeData(FriendlyByteBuf buffer) {
        buffer.writeFluidStack(renderFluid);
        buffer.writeBoolean(isPouring);
    }

    @Override
    protected void handlePacket(WoodenFaucetTileEntity tile) {
        tile.handlePacket(this.renderFluid, this.isPouring);
    }

}
