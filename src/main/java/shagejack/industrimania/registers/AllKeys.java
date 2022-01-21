package shagejack.industrimania.registers;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ClientRegistry;
import shagejack.industrimania.Industrimania;

public enum AllKeys {

    TOOL_MENU("toolmenu", GLFW.GLFW_KEY_LEFT_ALT),
    ACTIVATE_TOOL("", GLFW.GLFW_KEY_LEFT_CONTROL),
    TOOLBELT("toolbelt", GLFW.GLFW_KEY_LEFT_ALT),

    ;

    private KeyMapping keybind;
    private String description;
    private int key;
    private boolean modifiable;

    private AllKeys(String description, int defaultKey) {
        this.description = Industrimania.MOD_ID + ".keyinfo." + description;
        this.key = defaultKey;
        this.modifiable = !description.isEmpty();
    }

    public static void register() {
        for (AllKeys key : values()) {
            key.keybind = new KeyMapping(key.description, key.key, Industrimania.MOD_NAME);
            if (!key.modifiable)
                continue;

            ClientRegistry.registerKeyBinding(key.keybind);
        }
    }

    public KeyMapping getKeybind() {
        return keybind;
    }

    public boolean isPressed() {
        if (!modifiable)
            return isKeyDown(key);
        return keybind.isDown();
    }

    public String getBoundKey() {
        return keybind.getTranslatedKeyMessage()
                .getString()
                .toUpperCase();
    }

    public int getBoundCode() {
        return keybind.getKey()
                .getValue();
    }

    public static boolean isKeyDown(int key) {
        return GLFW.glfwGetKey(Minecraft.getInstance()
                .getWindow()
                .getWindow(), key) != 0;
    }

    public static boolean ctrlDown() {
        return Screen.hasControlDown();
    }

    public static boolean shiftDown() {
        return Screen.hasShiftDown();
    }

    public static boolean altDown() {
        return Screen.hasAltDown();
    }

}
