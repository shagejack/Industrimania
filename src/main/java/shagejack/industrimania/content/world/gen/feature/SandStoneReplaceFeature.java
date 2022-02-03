package shagejack.industrimania.content.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import shagejack.industrimania.registers.block.AllBlocks;

import javax.annotation.ParametersAreNonnullByDefault;

//Code from Geolosy

public class SandStoneReplaceFeature extends Feature<NoneFeatureConfiguration> {

    public SandStoneReplaceFeature(Codec<NoneFeatureConfiguration> p_i231976_1_) {
        super(p_i231976_1_);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> f) {
        if (f.chunkGenerator() instanceof FlatLevelSource) {
            return false;
        }

        WorldGenLevel level = f.level();
        ChunkPos cp = new ChunkPos(f.origin());

        for (int x = cp.getMinBlockX(); x <= cp.getMaxBlockX(); x++) {
            for (int z = cp.getMinBlockZ(); z <= cp.getMaxBlockZ(); z++) {
                for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
                    BlockPos p = new BlockPos(x, y, z);
                    BlockState state = level.getBlockState(p);
                    if (state.getBlock() == Blocks.SANDSTONE) {
                        level.setBlock(p, AllBlocks.rock_sandstone.block().get().defaultBlockState(), 2 | 16);
                    }
                }
            }
        }

        return true;
    }
}
