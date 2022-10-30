package shagejack.industrimania.foundation.utility;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class JsonHelper {

    public static Integer getIntOrDefault(JsonObject json, String member, Integer def) {
        JsonElement element = json.get(member);
        if (element != null)
            return element.getAsInt();
        return def;
    }

    public static Boolean getBooleanOrDefault(JsonObject json, String member, Boolean def) {
        JsonElement element = json.get(member);
        if (element != null)
            return element.getAsBoolean();
        return def;
    }

    public static Double getDoubleOrDefault(JsonObject json, String member, Double def) {
        JsonElement element = json.get(member);
        if (element != null)
            return element.getAsDouble();
        return def;
    }

    public static Float getFloatOrDefault(JsonObject json, String member, Float def) {
        JsonElement element = json.get(member);
        if (element != null)
            return element.getAsFloat();
        return def;
    }

    public static String getStringOrDefault(JsonObject json, String member, String def) {
        JsonElement element = json.get(member);
        if (element != null)
            return element.getAsString();
        return def;
    }

    public static Integer getInt(JsonObject json, String member) {
        return getIntOrDefault(json, member, null);
    }

    public static Boolean getBoolean(JsonObject json, String member) {
        return getBooleanOrDefault(json, member, null);
    }

    public static Double getDouble(JsonObject json, String member) {
        return getDoubleOrDefault(json, member, null);
    }

    public static Float getFloat(JsonObject json, String member) {
        return getFloatOrDefault(json, member, null);
    }

    public static String getString(JsonObject json, String member) {
        return getStringOrDefault(json, member, null);
    }

    public static ResourceLocation getResourceLocation(JsonObject json, String member) {
        return new ResourceLocation(json.get(member).getAsString());
    }

}
