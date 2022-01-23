package shagejack.industrimania.content.worldGen;

import shagejack.industrimania.api.chemistry.ChemicalFormula;
import shagejack.industrimania.content.worldGen.record.OreType;

import java.util.HashMap;
import java.util.Map;

public class OreTypeRegistry {
    public static final Map<String, OreType> oreTypeMap = new HashMap<>();

    //OreTypes
    public static OreType hematite = new OreType("hematite", new ChemicalFormula("Fe2O3"), 1, null);
    public static OreType galena = new OreType("galena", new ChemicalFormula("PbS"), 1, null);
    public static OreType sphalerite = new OreType("sphalerite", new ChemicalFormula("ZnS"), 1, null);

}
