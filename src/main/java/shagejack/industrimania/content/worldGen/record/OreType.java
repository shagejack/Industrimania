package shagejack.industrimania.content.worldGen.record;

import shagejack.industrimania.api.chemistry.ChemicalFormula;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public record OreType(String name, ChemicalFormula elements, @Nullable Color color) {
}
