package shagejack.industrimania.foundation.state;

import net.minecraft.util.StringRepresentable;

public enum ContainWood implements StringRepresentable {
    EMPTY("empty"),
    OAK("oak"),
    BIRCH("birch"),
    SPRUCE("spruce"),
    JUNGLE("jungle"),
    DARKOAK("darkoak"),
    ACACIA("acacia");

    private final String name;

    ContainWood(String p_61339_) {
        this.name = p_61339_;
    }

    public String toString() {
        return this.name;
    }

    public String getSerializedName() {
        return this.name;
    }
}
