package shagejack.industrimania.registers.setup;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.electricity.Electricity;
import shagejack.industrimania.content.pollution.PollutionEventHandler;
import shagejack.industrimania.content.world.gen.GenerationRegistry;
import shagejack.industrimania.content.dynamicLights.DynamicLights;
import shagejack.industrimania.foundation.handler.MultiBlockBreakEventHandler;
import shagejack.industrimania.foundation.handler.DynamicLightsItemJoinWorldHandler;
import shagejack.industrimania.registers.AllCommands;

@Mod.EventBusSubscriber(modid = Industrimania.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
        bus.addListener(MultiBlockBreakEventHandler::serverWorldTick);
        bus.addListener(MultiBlockBreakEventHandler::blockBreak);
        bus.addListener(DynamicLightsItemJoinWorldHandler::onEntityJoinWorld);
    }

}
