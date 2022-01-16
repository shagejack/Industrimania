package shagejack.industrimania.content.worldGen;

import shagejack.industrimania.api.chemistry.ChemicalFormula;
import shagejack.industrimania.content.worldGen.record.OreType;

public class OreTypeRegistry {
    public static OreType hematite = new OreType("hematite", new ChemicalFormula("Fe2O3"));
    public static OreType galena = new OreType("galena", new ChemicalFormula("PbS"));
    public static OreType sphalerite = new OreType("sphalerite", new ChemicalFormula("ZnS"));
}
