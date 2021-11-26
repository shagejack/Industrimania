package shagejack.minecraftology.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import shagejack.minecraftology.Minecraftology;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.DecimalFormat;

public class MCLStringHelper {
    public static final String MORE_INFO = TextFormatting.RESET.toString() + TextFormatting.GRAY + "Hold " + TextFormatting.ITALIC + TextFormatting.YELLOW + "Shift" + TextFormatting.RESET.toString() + TextFormatting.GRAY + " for Details.";

    private static String[] suffix = new String[]{"", "K", "M", "B", "T"};
    private static int MAX_LENGTH = 4;

    public static String formatNumber(double number) {
        return formatNumber(number, "0.00");
    }

    public static String formatNumber(double number, String decialFormat) {
        if (number > 1000000000000000D) {
            return new DecimalFormat(decialFormat + "Q").format((number / 1000000000000000.00D));
        }
        if (number > 1000000000000D) {
            return new DecimalFormat(decialFormat + "T").format((number / 1000000000000.00D));
        } else if (number > 1000000000D) {
            return new DecimalFormat(decialFormat + "B").format((number / 1000000000.00D));
        } else if (number > 1000000D) {
            return new DecimalFormat(decialFormat + "M").format((number / 1000000.00D));
        } else if (number > 1000D) {
            return new DecimalFormat(decialFormat + "K").format((number / 1000.00D));
        } else {
            return new DecimalFormat(decialFormat).format(number);
        }
    }

    public static String formatRemainingTime(float seccounds) {
        return formatRemainingTime(seccounds, false);
    }

    public static String formatRemainingTime(float seccounds, boolean shotSufix) {
        if (seccounds > 3600) {
            return String.format("%s%s", String.valueOf(Math.round(seccounds / 3600)), shotSufix ? "h" : " hr");
        } else if (seccounds > 60 && seccounds < 60 * 60) {
            return String.format("%s%s", String.valueOf(Math.round(seccounds / 60)), shotSufix ? "m" : " min");
        } else {
            return String.format("%s%s", String.valueOf(Math.round(seccounds)), shotSufix ? "s" : " sec");
        }
    }

    public static String typingAnimation(String message, int time, int maxTime) {
        float percent = ((float) time / (float) maxTime);
        int messageCount = message.length();
        return message.substring(0, MathHelper.clamp(Math.round(messageCount * percent), 0, messageCount));
    }

    public static boolean hasTranslation(String key) {
        return Minecraftology.PROXY.hasTranslation(key);
    }

    public static String translateToLocal(String key, Object... params) {
        return Minecraftology.PROXY.translateToLocal(key, params);
    }

    public static String readTextFile(ResourceLocation location) {
        StringBuilder text = new StringBuilder();
        try {
            String path = "/assets/" + location.getNamespace() + "/" + location.getPath();
            InputStream descriptionStream = MCLStringHelper.class.getResourceAsStream(path);
            if (descriptionStream == null)
                return text.toString();
            LineNumberReader descriptionReader = new LineNumberReader(new InputStreamReader(descriptionStream));
            String line;

            while ((line = descriptionReader.readLine()) != null) {
                text.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }

    public static String addPrefix(String name, String prefix) {
        if (prefix.endsWith("-")) {
            return prefix.substring(0, prefix.length() - 2) + Character.toLowerCase(name.charAt(0)) + name.substring(1);
        } else {
            return prefix + " " + name;
        }
    }

    public static String addSuffix(String name, String suffix) {
        if (suffix.startsWith("-")) {
            return name + suffix.substring(1);
        } else {
            return name + " " + suffix;
        }
    }

    public static String[] formatVariations(String unlocalizedName, String unlocalizedSuffix, int count) {
        String[] variations = new String[count];
        for (int i = 0; i < count; i++) {
            variations[i] = unlocalizedName + "." + i + "." + unlocalizedSuffix;
        }
        return variations;
    }

    public static boolean isControlKeyDown() {
        return Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
    }

    public static boolean isAltKeyDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
    }
}