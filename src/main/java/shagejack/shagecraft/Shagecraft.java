package shagejack.shagecraft;

import cpw.mods.modlauncher.Launcher;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shagejack.shagecraft.registers.RegisterHandle;

import static cpw.mods.modlauncher.api.IEnvironment.Keys.LAUNCHTARGET;

@Mod(ShageCraft.MOD_ID)
public class ShageCraft {
    public static final String MOD_ID = "shage_craft";
    public static final String MOD_NAME = "ShageCraft";
    public static final Logger LOGGER = LogManager.getFormatterLogger(ShageCraft.MOD_NAME);
    public static final boolean isDataGen = Launcher.INSTANCE.environment()
            .getProperty(LAUNCHTARGET.get()).orElseThrow().equals("forgedatauserdev");

    public ShageCraft() {
        try {
            RegisterHandle.RegRegisters();
            RegisterHandle.init();
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

}