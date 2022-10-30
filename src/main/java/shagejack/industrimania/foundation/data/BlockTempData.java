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

    public double getEnvironmentTempFactor() {
        return this.getDoubleOrDefault("environment_temp_factor", 0.5D);
    }

    public double getMinTemp() {
        return this.getDoubleOrDefault("min_temp", -273.15D);
    }

    public double getMaxTemp() {
        return this.getDoubleOrDefault("max_temp", 10000.0D);
    }

    public boolean sameAsEnvironment() {
        return this.getBooleanOrDefault("same_as_environment", true);
    }
}
