package shagejack.industrimania.content.world.nature.rubberTree;

import net.minecraftforge.fluids.FluidStack;
import shagejack.industrimania.foundation.fluid.FluidTankBase;
import shagejack.industrimania.registers.AllFluids;

public class RubberTreeTank extends FluidTankBase<RubberTreeLogTileEntity> {

    public RubberTreeTank(RubberTreeLogTileEntity te, int capacity) {
        super(te, capacity);
    }

    public boolean isFluidValid(FluidStack stack)
    {
        return stack.getFluid().isSame(AllFluids.rawRubber.still().get());
    }

}
