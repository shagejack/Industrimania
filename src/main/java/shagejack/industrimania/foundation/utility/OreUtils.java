package shagejack.industrimania.foundation.utility;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.world.gen.OreGrade;
import shagejack.industrimania.content.world.gen.record.Ore;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OreUtils {

    private OreUtils() {
        throw new IllegalStateException(this.getClass().toString() + "should not be instantiated as it's a utility class.");
    }

    /*
    Ore Grade:
    0: Poor
    1: Normal
    2: Rich
     */
    public static Block getOreBlock(Ore ore, Block rock, int grade) {
        String rockName = Objects.requireNonNull(rock.getRegistryName()).toString().split(":")[1];
        ResourceLocation oreBlock = new ResourceLocation(Industrimania.MOD_ID, rockName + "_" + ore.oreType().name() + "_" + grade);
        return ForgeRegistries.BLOCKS.getValue(oreBlock);
    }

    public static Block getOreBlock(Ore ore, Block rock, OreGrade grade) {
        return getOreBlock(ore, rock, grade.getIndex());
    }

    public static List<Block> getAllGradeOre(Ore ore, Block rock) {
        return Arrays.stream(OreGrade.values()).map(grade -> getOreBlock(ore, rock, grade)).toList();
    }
}
