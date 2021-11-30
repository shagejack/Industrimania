package shagejack.minecraftology.init;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class FluidsMCL {

    public static void init(FMLPreInitializationEvent event) {

        registerFluidContainers();
    }

    @SuppressWarnings("deprecation")
    private static void registerFluidContainers() {
//		FluidContainerRegistry.registerFluidContainer(new FluidStack(matterPlasma, 32), new ItemStack(MatterOverdrive.items.matterContainerFull), new ItemStack(MatterOverdrive.items.matterContainer));
    }
}

