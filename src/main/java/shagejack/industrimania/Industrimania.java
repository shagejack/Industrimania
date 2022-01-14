package shagejack.industrimania;

import cpw.mods.modlauncher.Launcher;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shagejack.industrimania.content.worldGen.GenerationRegistry;
import shagejack.industrimania.content.worldGen.IndustrimaniaFeatures;
import shagejack.industrimania.registers.RegisterHandle;
import shagejack.industrimania.registers.setup.ModSetup;

import static cpw.mods.modlauncher.api.IEnvironment.Keys.LAUNCHTARGET;

@Mod(Industrimania.MOD_ID)
public class Industrimania {
    public static final String MOD_ID = "industrimania";
    public static final String MOD_NAME = "Industrimania";
    public static final Logger LOGGER = LogManager.getFormatterLogger(Industrimania.MOD_NAME);
    public static final boolean isDataGen = Launcher.INSTANCE.environment()
            .getProperty(LAUNCHTARGET.get()).orElseThrow().equals("forgedatauserdev");

    public Industrimania() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        try {
            RegisterHandle.RegRegisters();
            RegisterHandle.init();

            ModSetup.setup();

            bus.addListener(this::setup);
            bus.addListener(this::clientSetup);

            MinecraftForge.EVENT_BUS.register(this);

        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
    }

    public void setup(final FMLCommonSetupEvent event) {
        IndustrimaniaFeatures.createRegistry().register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
