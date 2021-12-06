package shagejack.shagecraft.data.tank;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import shagejack.shagecraft.api.matter.IMatterHandler;

public class ShageFluidTank extends FluidTank implements IMatterHandler {

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
    public int modifyMatterStored(int amount) {
        int lastAmount = getFluid() == null ? 0 : getFluid().amount;
        int newAmount = lastAmount + amount;
        newAmount = MathHelper.clamp(newAmount, 0, getCapacity());
        setMatterStored(newAmount);
        return lastAmount - newAmount;
    }

    @Override
    public int getMatterStored() {
        return getFluidAmount();
    }

    @Override
    public void setMatterStored(int amount) {
        if (amount <= 0) {
            setFluid(null);
        } else {
            drainInternal(getFluidAmount(), true);
            //fillInternal(new FluidStack(OverdriveFluids.matterPlasma, amount), true);
        }
    }

    @Override
    public int receiveMatter(int amount, boolean simulate) {
        //return fill(new FluidStack(OverdriveFluids.matterPlasma, amount), !simulate);
        return 0;
    }

    @Override
    public int extractMatter(int amount, boolean simulate) {
        FluidStack drained = drain(amount, !simulate);
        if (drained == null) {
            return 0;
        } else {
            return drained.amount;
        }
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

