package shagejack.industrimania.foundation.data;

import com.google.gson.JsonObject;

public class BiomeTempData extends JsonDataHolder {

    public BiomeTempData(JsonObject data) {
        super(data);
    }

    public double getTemp() {
        return this.getDoubleOrDefault("temperature", 0.0D);
    }

}
