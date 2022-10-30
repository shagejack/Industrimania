package shagejack.industrimania.foundation.data;

import com.google.gson.JsonObject;

public class BlockTempData extends JsonDataHolder {

    public BlockTempData(JsonObject data) {
        super(data);
    }

    public double getConvectiveTemp() {
        return this.getDoubleOrDefault("convectiveTemperature", 0.0D);
    }

    public double getCloserTemp() {
        return this.getDoubleOrDefault("closerTemperature", 0.0D);
    }

    public double getDirectTemp() {
        return this.getDoubleOrDefault("directTemperature", 0.0D);
    }

    public double getRange() {
        return Math.max(this.getDoubleOrDefault("range", 1.0D), 1.0D);
    }

    public boolean mustLit() {
        return this.getBooleanOrDefault("must_lit", false);
    }

    public boolean hasLevel() {
        return this.getBooleanOrDefault("has_level", false);
    }

    public boolean isSource() {
        return this.getBooleanOrDefault("is_source", false);
    }

    public boolean isAir() {
        return this.getBooleanOrDefault("is_air", true);
    }
}
