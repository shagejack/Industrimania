package shagejack.industrimania;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shagejack.industrimania.registries.RegisterHandle;
import shagejack.industrimania.registries.setup.ModSetup;

@Mod(Industrimania.MOD_ID)
public class Industrimania {
    public static final String MOD_ID = "industrimania";
    public static final String MOD_NAME = "Industrimania";
    public static final Logger LOGGER = LogManager.getLogger(Industrimania.MOD_NAME);
    public static final boolean isDataGen = FMLLoader.getLaunchHandler().isData();

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public Industrimania() {
        onIndustrimania();
    }

    public static void onIndustrimania() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        try {
            LOGGER.info("Registering...");
            RegisterHandle.RegRegisters();

            LOGGER.info("Initializing...");
            RegisterHandle.init();

            LOGGER.info("Setting up event listeners...");
            ModSetup.setup(modEventBus, forgeEventBus);

        } catch (Exception e) {
            LOGGER.error(e);
            throw new RuntimeException();
        }

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> IndustrimaniaClient.onClient(modEventBus, forgeEventBus));
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}
