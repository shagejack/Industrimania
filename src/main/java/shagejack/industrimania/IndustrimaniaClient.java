package shagejack.industrimania;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import shagejack.industrimania.foundation.utility.ModelSwapper;

public class IndustrimaniaClient {

    public static final ModelSwapper MODEL_SWAPPER = new ModelSwapper();

    public static void onClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        Industrimania.LOGGER.info("Initializing Client...");

        modEventBus.addListener(IndustrimaniaClient::clientInit);

        MODEL_SWAPPER.registerListeners(modEventBus);

    }

    public static void clientInit(final FMLClientSetupEvent event) {

    }

}
