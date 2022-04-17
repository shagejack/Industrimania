package shagejack.industrimania.registers.setup;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import shagejack.industrimania.content.electricity.Electricity;
import shagejack.industrimania.content.pollution.PollutionEventHandler;
import shagejack.industrimania.content.world.gen.GenerationRegistry;
import shagejack.industrimania.content.dynamicLights.DynamicLights;
import shagejack.industrimania.foundation.handler.MultiBlockEventHandler;
import shagejack.industrimania.foundation.handler.DynamicLightsItemJoinWorldHandler;
import shagejack.industrimania.registers.AllCommands;

public class ModSetup {

    public static void setup() {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(GenerationRegistry::onBiomesLoaded);
        bus.addListener(AllCommands::registerCommand);
        bus.addListener(PollutionEventHandler::tickPollution);
        bus.addListener(PollutionEventHandler::chunkLoad);
        bus.addListener(PollutionEventHandler::chunkSave);
        bus.addListener(PollutionEventHandler::chunkUnload);
        bus.addListener(PollutionEventHandler::onFogColorRender);
        bus.addListener(PollutionEventHandler::onFogDensityRender);
        bus.addListener(DynamicLights::serverWorldTick);
        bus.addListener(Electricity::serverWorldTick);
        bus.addListener(MultiBlockEventHandler::serverWorldTick);
        bus.addListener(MultiBlockEventHandler::blockBreak);
        bus.addListener(DynamicLightsItemJoinWorldHandler::onEntityJoinWorld);
    }

}
