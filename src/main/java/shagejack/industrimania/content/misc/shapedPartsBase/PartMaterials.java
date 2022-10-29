package shagejack.industrimania.content.misc.shapedPartsBase;

import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public enum PartMaterials implements PartMaterial {
    EMPTY("EMPTY"),
    IRON("IRON")
    ;

    private final String name;

    /**
     * @param name should be same as the field name;
     */
    PartMaterials(String name) {
        this.name = name;
    }

    public Tag getNBT() {
        return StringTag.valueOf(getName());
    }

    public static PartMaterial getMaterialFromNBT(Tag nbt) {
        return PartMaterials.valueOf(nbt.getAsString());
    }

    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public String toString() {
        return this.getName();
    }
}
