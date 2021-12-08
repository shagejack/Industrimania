package shagejack.shagecraft.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import shagejack.shagecraft.api.internal.Storage;
import shagejack.shagecraft.api.steam.ISteamHandler;
import shagejack.shagecraft.data.tank.ShageFluidTank;

/**
 * @author shadowfacts
 */
public class ShagecraftCapabilities {

    @CapabilityInject(ISteamHandler.class)
    public static Capability<ISteamHandler> STEAM_HANDLER;

    public static void init() {
        CapabilityManager.INSTANCE.register(ISteamHandler.class, new Storage<>(), () -> new ShageFluidTank(2000));
    }

}