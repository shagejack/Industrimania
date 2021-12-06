package shagejack.shagecraft.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import shagejack.shagecraft.api.internal.Storage;
import shagejack.shagecraft.api.matter.IMatterHandler;
import shagejack.shagecraft.data.tank.ShageFluidTank;

/**
 * @author shadowfacts
 */
public class ShagecraftCapabilities {

    @CapabilityInject(IMatterHandler.class)
    public static Capability<IMatterHandler> MATTER_HANDLER;

    public static void init() {
        CapabilityManager.INSTANCE.register(IMatterHandler.class, new Storage<>(), () -> new ShageFluidTank(2000));
    }

}