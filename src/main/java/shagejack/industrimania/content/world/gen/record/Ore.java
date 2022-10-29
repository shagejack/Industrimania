package shagejack.industrimania.content.world.gen.record;

import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @param oreType ore type of the ore.
 * @param availableRock a list of rocks this ore could generate.
 * @param minY used in selecting ore type based on deposit center position.
 * @param maxY used in selecting ore type based on deposit center position.
 * @param chanceAsParagenesis chance of ore to generate when it's a paragenesis of other ores.
 * @param plantSign the plant sign of the ore, null if it doesn't exist.
 * @param oreCap the ore cap of the ore, null if it doesn't exist.
 * @param paragenesis paragenesis of the ore.
 */
public record Ore(OreType oreType, @Nullable List<Block> availableRock, int minY, int maxY, double chanceAsParagenesis, @Nullable Block plantSign, @Nullable Block oreCap, @Nullable Ore...paragenesis) {

    public static Ore asParagenesis(OreType oreType, double chanceAsParagenesis) {
        return new Ore(oreType, null, 0, 0, chanceAsParagenesis, null, null);
    }

}
