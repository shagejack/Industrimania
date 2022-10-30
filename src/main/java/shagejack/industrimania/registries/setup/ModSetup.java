package shagejack.industrimania.registries.setup;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import shagejack.industrimania.client.handler.BlockColorHandler;
import shagejack.industrimania.client.handler.ItemColorHandler;
import shagejack.industrimania.content.electricity.Electricity;
import shagejack.industrimania.content.modification.events.BottleEventHandler;
import shagejack.industrimania.content.pollution.PollutionEventHandler;
import shagejack.industrimania.content.steamAge.steam.CapabilitySteamHandler;
import shagejack.industrimania.content.world.gen.GenerationRegistry;
import shagejack.industrimania.content.dynamicLights.DynamicLights;
import shagejack.industrimania.foundation.data.IMDataReloadListener;
import shagejack.industrimania.foundation.data.ReloadListenerEventHandle;
import shagejack.industrimania.foundation.handler.MultiBlockEventHandler;
import shagejack.industrimania.foundation.handler.DynamicLightsItemJoinWorldHandler;
import shagejack.industrimania.registries.AllCommands;
import shagejack.industrimania.registries.AllRecipeTypes;
import shagejack.industrimania.registries.item.ItemPropertyOverridesRegistry;

public class ModSetup {

    public static void setup(IEventBus modEventBus, IEventBus forgeEventBus) {
        forgeEventBus.addListener(GenerationRegistry::onBiomesLoaded);
        forgeEventBus.addListener(AllCommands::registerCommand);
        forgeEventBus.addListener(PollutionEventHandler::tickPollution);
        forgeEventBus.addListener(PollutionEventHandler::chunkLoad);
        forgeEventBus.addListener(PollutionEventHandler::chunkSave);
        forgeEventBus.addListener(PollutionEventHandler::chunkUnload);
        forgeEventBus.addListener(PollutionEventHandler::onFogColorRender);
        forgeEventBus.addListener(PollutionEventHandler::onFogDensityRender);
        forgeEventBus.addListener(DynamicLights::serverWorldTick);
        forgeEventBus.addListener(Electricity::serverWorldTick);
        forgeEventBus.addListener(MultiBlockEventHandler::serverWorldTick);
        forgeEventBus.addListener(MultiBlockEventHandler::blockBreak);
        forgeEventBus.addListener(DynamicLightsItemJoinWorldHandler::onEntityJoinWorld);
        forgeEventBus.addListener(BottleEventHandler::onClick);
        forgeEventBus.addListener(ReloadListenerEventHandle::onReload);

        modEventBus.addListener(BlockColorHandler::registerBlockColors);
        modEventBus.addListener(ItemColorHandler::registerItemColors);
        modEventBus.addListener(ItemPropertyOverridesRegistry::propertyOverrideRegistry);
        modEventBus.addGenericListener(RecipeSerializer.class, AllRecipeTypes::register);
        modEventBus.addListener(CapabilitySteamHandler::register);
    }

}
