package shagejack.industrimania.content.world.nature.mulberry.silkworm;

import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class SilkwormRearingBoxTank extends FluidTank {

    public SilkwormRearingBoxTank(int capacity) {
        super(capacity);
    }

    public boolean isFluidValid(FluidStack stack)
    {
        return stack.getFluid().isSame(Fluids.WATER);
    }

}
