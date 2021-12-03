package shagejack.minecraftology.init;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import shagejack.minecraftology.fluids.FluidHalfMoltenGlass;

public class FluidsMCL {

    public static FluidHalfMoltenGlass halfMoltenGlass;

    public static void init(FMLPreInitializationEvent event) {
        halfMoltenGlass = new FluidHalfMoltenGlass("half_molten_glass");
        FluidRegistry.registerFluid(halfMoltenGlass);
        FluidRegistry.addBucketForFluid(halfMoltenGlass);

        registerFluidContainers();
    }

    @SuppressWarnings("deprecation")
    private static void registerFluidContainers() {
//		FluidContainerRegistry.registerFluidContainer(new FluidStack(matterPlasma, 32), new ItemStack(MatterOverdrive.items.matterContainerFull), new ItemStack(MatterOverdrive.items.matterContainer));
    }
}

