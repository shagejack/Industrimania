package shagejack.industrimania.content.worldGen;

import com.google.common.collect.Lists;
import shagejack.industrimania.api.chemistry.ChemicalFormula;
import shagejack.industrimania.content.worldGen.record.Ore;
import shagejack.industrimania.content.worldGen.record.OreType;

import java.util.List;

public class OreTypeRegistry {
    public static OreType hematite = new OreType("hematite", new ChemicalFormula("Fe2O3"), 1, null);
    public static OreType galena = new OreType("galena", new ChemicalFormula("PbS"), 1, null);
    public static OreType sphalerite = new OreType("sphalerite", new ChemicalFormula("ZnS"), 1, null);

    public static final List<OreType> oreTypeList = Lists.newArrayList(
            hematite,
            galena,
            sphalerite
    );

}
