package shagejack.industrimania.content.world.gen.record;

import net.minecraft.client.resources.language.I18n;
import shagejack.industrimania.foundation.chemistry.ChemicalFormula;
import shagejack.industrimania.content.world.gen.OreTypeRegistry;

import javax.annotation.Nullable;
import java.awt.*;

public record OreType(String name, ChemicalFormula elements, int harvestLevel, @Nullable Color color) {

    public OreType {
        OreTypeRegistry.oreTypeMap.put(name, this);
    }

    public String getLocalizedName() {
        return I18n.get("industrimania.oretype." + this.name());
    }

}
