package shagejack.industrimania.content.world.gen.record;

import shagejack.industrimania.api.chemistry.ChemicalFormula;
import shagejack.industrimania.content.world.gen.OreTypeRegistry;

import javax.annotation.Nullable;
import java.awt.*;

public record OreType(String name, ChemicalFormula elements, int harvestLevel, @Nullable Color color) {

    public OreType {
        OreTypeRegistry.oreTypeMap.put(name, this);
    }

}
