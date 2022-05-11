package shagejack.industrimania.content.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import shagejack.industrimania.content.world.nature.cobble.Cobble;
import shagejack.industrimania.registers.block.AllBlocks;

public class CobbleGenFeature extends Feature<NoneFeatureConfiguration> {

    public CobbleGenFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (context.chunkGenerator() instanceof FlatLevelSource) {
            return false;
        }

        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();

        // it's actually redundant to check the block state below as it should be checked using Cobble#canSurvive when the placed feature got position stream on generation, but who cares?
        if (level.getBlockState(pos).is(Blocks.WATER) && !level.getBlockState(pos.below()).isAir() && !level.getBlockState(pos.below()).is(Blocks.WATER)) {
            level.setBlock(pos, AllBlocks.nature_cobble.block().get().defaultBlockState().setValue(Cobble.WATERLOGGED, true), 3);
            return true;
        }

        return false;
    }
}
