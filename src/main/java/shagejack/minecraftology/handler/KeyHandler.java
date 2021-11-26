package shagejack.minecraftology.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyHandler {
    public static final int MATTER_SCANNER_GUI = 0;
    public static final int ABILITY_USE_KEY = 1;
    public static final int ABILITY_SWITCH_KEY = 2;
    private static final String[] keyDesc = {"Open Matter Scanner GUI", "Android Ability key", "Android Switch Ability key"};
    private static final int[] keyValues = {Keyboard.KEY_C, Keyboard.KEY_X, Keyboard.KEY_R};
    private final KeyBinding[] keys;

    public KeyHandler() {
        keys = new KeyBinding[keyValues.length];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = new KeyBinding(keyDesc[i], keyValues[i], "Matter Overdrive");
            ClientRegistry.registerKeyBinding(keys[i]);
        }
    }

    @SubscribeEvent
    public void onClientTick(InputEvent.KeyInputEvent event) {
        if (Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().isGamePaused() || Minecraft.getMinecraft().currentScreen != null) {
            return;
        }
    }

    public KeyBinding getBinding(int id) {
        return keys[id];
    }
}
