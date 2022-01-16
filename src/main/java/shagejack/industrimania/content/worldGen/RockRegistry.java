package shagejack.industrimania.content.worldGen;

import com.google.common.collect.Lists;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import shagejack.industrimania.registers.AllBlocks;

import java.util.List;

public class RockRegistry {
    // stone block replacements that are Sedimentary
    public static final List<Block> sedimentaryStones = Lists.newArrayList(
            AllBlocks.rock_shale.block().get(),
            AllBlocks.rock_limestone.block().get(),
            AllBlocks.rock_conglomeratee.block().get(),
            AllBlocks.rock_chalk.block().get(),
            AllBlocks.rock_coal.block().get(),
            AllBlocks.rock_oilshale.block().get(),
            AllBlocks.rock_dolomite.block().get()
    );
    // stone block replacements that are Metamorphic
    public static final List<Block> metamorphicStones = Lists.newArrayList(
            Blocks.DEEPSLATE,
            AllBlocks.rock_marble.block().get(),
            AllBlocks.rock_gneiss.block().get(),
            AllBlocks.rock_phyllite.block().get(),
            AllBlocks.rock_quartzite.block().get(),
            AllBlocks.rock_greisen.block().get(),
            AllBlocks.rock_granulite.block().get()
    );
    // stone block replacements that are Igneous
    public static final List<Block> igneousStones = Lists.newArrayList(
            Blocks.ANDESITE,
            Blocks.GRANITE,
            Blocks.DIORITE,
            AllBlocks.rock_rhyolite.block().get(),
            AllBlocks.rock_trachyte.block().get(),
            AllBlocks.rock_basalt.block().get(),
            AllBlocks.rock_porphyry.block().get(),
            AllBlocks.rock_gabbro.block().get(),
            AllBlocks.rock_dacite.block().get()
    );
}
