package shagejack.industrimania.content.steamAge.steam;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilitySteamHandler {
    public static Capability<ISteamHandler> STEAM_HANDLER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(ISteamHandler.class);
    }
}
