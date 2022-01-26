package shagejack.industrimania.foundation.state;

import net.minecraft.util.StringRepresentable;

public enum ContainItems implements StringRepresentable {
    EMPTY("empty"),
    HAY("hay"),
    MUD("mud"),
    ROCK("rock");

    private final String name;

    ContainItems(String p_61339_) {
        this.name = p_61339_;
    }

    public String toString() {
        return this.name;
    }

    public String getSerializedName() {
        return this.name;
    }
}
