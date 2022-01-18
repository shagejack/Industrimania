package shagejack.industrimania;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shagejack.industrimania.registers.RegisterHandle;
import shagejack.industrimania.registers.setup.ModSetup;

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

        } catch (Exception e) {
            LOGGER.error(e);
            throw new RuntimeException();
        }
    }

}
