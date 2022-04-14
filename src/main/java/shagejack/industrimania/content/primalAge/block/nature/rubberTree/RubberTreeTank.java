package shagejack.industrimania.content.primalAge.block.nature.rubberTree;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
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
