package shagejack.industrimania;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shagejack.industrimania.client.handler.BlockColorHandler;
import shagejack.industrimania.client.handler.ItemColorHandler;
import shagejack.industrimania.registers.AllRecipeTypes;
import shagejack.industrimania.registers.RegisterHandle;
import shagejack.industrimania.registers.setup.ModSetup;

@Mod(Industrimania.MOD_ID)
public class Industrimania {
    public static final String MOD_ID = "industrimania";
    public static final String MOD_NAME = "Industrimania";
    public static final Logger LOGGER = LogManager.getFormatterLogger(Industrimania.MOD_NAME);
    public static final boolean isDataGen = FMLLoader.getLaunchHandler().isData();

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public Industrimania() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        try {
            LOGGER.info("Registering...");
            RegisterHandle.RegRegisters();

            LOGGER.info("Initializing...");
            RegisterHandle.init();

            LOGGER.info("Setting up event listeners...");
            ModSetup.setup();

            bus.addListener(BlockColorHandler::registerBlockColors);
            bus.addListener(ItemColorHandler::registerItemColors);
            bus.addGenericListener(RecipeSerializer.class, AllRecipeTypes::register);

        } catch (Exception e) {
            LOGGER.error(e);
            throw new RuntimeException();
        }

    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}
