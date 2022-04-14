package shagejack.industrimania.foundation.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import shagejack.industrimania.foundation.tileEntity.SyncedTileEntity;

public class FluidUpdatePacket extends TileEntityDataPacket<SyncedTileEntity> {

    private final FluidStack fluid;

    public FluidUpdatePacket(FriendlyByteBuf buffer) {
        super(buffer);
        this.fluid = buffer.readFluidStack();
    }

    public FluidUpdatePacket(BlockPos pos, FluidStack fluid) {
        super(pos);
        this.fluid = fluid;
    }


    @Override
    protected void writeData(FriendlyByteBuf buffer) {
        buffer.writeFluidStack(fluid);
    }

    @Override
    protected void handlePacket(SyncedTileEntity tile) {
        if (tile instanceof IFluidPacketReceiver) {
            ((IFluidPacketReceiver) tile).updateFluidTo(this.fluid);
        }
    }

    public interface IFluidPacketReceiver {
        void updateFluidTo(FluidStack fluid);
    }
}
