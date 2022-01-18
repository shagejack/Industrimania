package shagejack.industrimania;

import cpw.mods.modlauncher.Launcher;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shagejack.industrimania.registers.AllBlocks;
import shagejack.industrimania.registers.AllTileEntities;
import shagejack.industrimania.registers.RegisterHandle;
import shagejack.industrimania.registers.setup.ModSetup;

import static cpw.mods.modlauncher.api.IEnvironment.Keys.LAUNCHTARGET;

@Mod(Industrimania.MOD_ID)
public class Industrimania {
    public static final String MOD_ID = "industrimania";
    public static final String MOD_NAME = "Industrimania";
    public static final Logger LOGGER = LogManager.getFormatterLogger(Industrimania.MOD_NAME);
    public static final boolean isDataGen = FMLLoader.getLaunchHandler().isData();

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
            throw new RuntimeException();
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        AllBlocks.ORES.forEach( (key, block) -> ItemBlockRenderTypes.setRenderLayer(block.block().get(), RenderType.cutoutMipped()));
    }

    public void setup(final FMLCommonSetupEvent event) {
    }

}
