package shagejack.industrimania.foundation.data;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import shagejack.industrimania.foundation.utility.JsonHelper;

public class JsonDataHolder {
    protected JsonObject data;

    public JsonDataHolder(JsonObject data) {
        this.data = data;
    }

    public ResourceLocation getId() {
        return this.getResourceLocation("id");
    }

    public JsonObject getData() {
        return data;
    }

    public Integer getIntOrDefault(String member, Integer def) {
        return JsonHelper.getIntOrDefault(data, member, def);
    }

    public Boolean getBooleanOrDefault(String member, Boolean def) {
        return JsonHelper.getBooleanOrDefault(data, member, def);
    }

    public Double getDoubleOrDefault(String member, Double def) {
        return JsonHelper.getDoubleOrDefault(data, member, def);
    }

    public Float getFloatOrDefault(String member, Float def) {
        return JsonHelper.getFloatOrDefault(data, member, def);
    }

    public String getStringOrDefault(String member, String def) {
        return JsonHelper.getStringOrDefault(data, member, def);
    }

    public Integer getInt(String member) {
        return JsonHelper.getInt(data, member);
    }

    public Boolean getBoolean(String member) {
        return JsonHelper.getBoolean(data, member);
    }

    public Double getDouble(String member) {
        return JsonHelper.getDouble(data, member);
    }

    public Float getFloat(String member) {
        return JsonHelper.getFloat(data, member);
    }

    public String getString(String member) {
        return JsonHelper.getString(data, member);
    }

    public ResourceLocation getResourceLocation(String member) {
        return JsonHelper.getResourceLocation(data, member);
    }
}
