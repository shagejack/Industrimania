package shagejack.shagecraft.data.tank;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import shagejack.shagecraft.api.steam.ISteamHandler;
import shagejack.shagecraft.init.ShagecraftFluids;

public class GlassMeltingFurnaceTank extends ShageFluidTank {

    private int maxExtract;
    private int maxReceive;

    public GlassMeltingFurnaceTank(int capacity) {
        super(capacity);
    }

    public GlassMeltingFurnaceTank(FluidStack fluidStack, int capacity) {
        super(fluidStack, capacity);
    }

    public GlassMeltingFurnaceTank(Fluid fluid, int amount, int capacity) {
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
        return fluid != null && fluid.getFluid() == ShagecraftFluids.halfMoltenGlass;
    }

    @Override
    public boolean canDrainFluidType(FluidStack fluid) {
        return fluid != null && fluid.getFluid() == ShagecraftFluids.halfMoltenGlass;
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

