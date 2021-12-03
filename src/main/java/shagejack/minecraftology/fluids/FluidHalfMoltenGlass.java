package shagejack.minecraftology.fluids;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import shagejack.minecraftology.Reference;

public class FluidHalfMoltenGlass extends Fluid{
    public FluidHalfMoltenGlass(String fluidName) {
        super(fluidName, new ResourceLocation(Reference.MOD_ID, "fluids/half_molten_glass/still"), new ResourceLocation(Reference.MOD_ID, "fluids/half_molten_glass/flowing"));
        setViscosity(6000);
        setLuminosity(8);
        setTemperature(1200);
    }
}
