package shagejack.shagecraft.data.tank;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import shagejack.shagecraft.api.steam.ISteamHandler;

public class ShageFluidTank extends FluidTank {

    private int maxExtract;
    private int maxReceive;

    public ShageFluidTank(int capacity) {
        super(capacity);
    }

    public ShageFluidTank(FluidStack fluidStack, int capacity) {
        super(fluidStack, capacity);
    }

    public ShageFluidTank(Fluid fluid, int amount, int capacity) {
        super(fluid, amount, capacity);
    }

    public int getMaxExtract() {
        return maxExtract;
    }

    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        return super.canFillFluidType(fluid);
    }

    @Override
    public void onContentsChanged() {
        if (this.tile != null && !tile.getWorld().isRemote) {
            final IBlockState state = this.tile.getWorld().getBlockState(this.tile.getPos());
            this.tile.getWorld().notifyBlockUpdate(this.tile.getPos(), state, state, 8);
            this.tile.markDirty();
        }
    }

}

