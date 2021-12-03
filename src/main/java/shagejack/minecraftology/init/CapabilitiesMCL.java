package shagejack.minecraftology.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import shagejack.minecraftology.api.internal.Storage;
import shagejack.minecraftology.api.matter.IMatterHandler;
import shagejack.minecraftology.data.tank.MCLFluidTank;

/**
 * @author shadowfacts
 */
public class CapabilitiesMCL {

    @CapabilityInject(IMatterHandler.class)
    public static Capability<IMatterHandler> MATTER_HANDLER;

    public static void init() {
        CapabilityManager.INSTANCE.register(IMatterHandler.class, new Storage<>(), () -> new MCLFluidTank(2000));
    }

}