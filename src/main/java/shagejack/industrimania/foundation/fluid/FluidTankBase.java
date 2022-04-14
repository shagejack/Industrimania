package shagejack.industrimania.foundation.fluid;

import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import shagejack.industrimania.foundation.network.AllPackets;
import shagejack.industrimania.foundation.network.packet.FluidUpdatePacket;
import shagejack.industrimania.foundation.tileEntity.SyncedTileEntity;

public class FluidTankBase<T extends SyncedTileEntity> extends FluidTank {

    protected T parent;

    public FluidTankBase(T parent, int capacity) {
        super(capacity);
        this.parent = parent;
    }

    @Override
    protected void onContentsChanged() {
        if (parent instanceof IFluidTankUpdater) {
            ((IFluidTankUpdater) parent).onTankContentsChanged();
        }

        parent.setChanged();
        Level level = parent.getLevel();
        if(level != null && !level.isClientSide()) {
            AllPackets.sendToNear(level, parent.getBlockPos(), new FluidUpdatePacket(parent.getBlockPos(), this.getFluid()));
        }
    }

    public void empty() {
        this.setFluid(FluidStack.EMPTY);
    }
}
