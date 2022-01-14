package shagejack.industrimania.content.worldGen;

import com.google.common.collect.Lists;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import shagejack.industrimania.registers.AllBlocks;

import java.util.ArrayList;
import java.util.List;

public class RockRegistry {
    // stone block replacements that are Sedimentary
    public static final List<Block> sedimentaryStones = Lists.newArrayList(
            AllBlocks.rock_shale.block().get(),
            AllBlocks.rock_limestone.block().get(),
            AllBlocks.rock_conglomeratee.block().get()
    );
    // stone block replacements that are Metamorphic
    public static final List<Block> metamorphicStones = Lists.newArrayList(
            Blocks.DEEPSLATE,
            AllBlocks.rock_marble.block().get(),
            AllBlocks.rock_gneiss.block().get()
    );
    // stone block replacements that are Igneous
    public static final List<Block> igneousStones = Lists.newArrayList(
            Blocks.ANDESITE,
            Blocks.GRANITE,
            Blocks.DIORITE
    );
}
