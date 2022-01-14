package shagejack.industrimania.content.worldGen.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import shagejack.industrimania.content.worldGen.Geology;
import shagejack.industrimania.registers.AllBlocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RockLayersFeature extends Feature<NoneFeatureConfiguration> {

    public RockLayersFeature(Codec p_65786_) {
        super(p_65786_);
    }

    private Geology geom = null;

    private final Lock glock = new ReentrantLock();

    /** is thread-safe */
    final Geology getGeology(WorldGenLevel level) {
        if (geom == null) {
            glock.lock();
            try {
                if (geom == null) {
                    geom = new Geology(level.getSeed(), 100, 32);
                }
            } finally {
                glock.unlock();
            }
        }
        return geom;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> f) {
        if (f.chunkGenerator() instanceof FlatLevelSource) {
            return false;
        }

        WorldGenLevel level = f.level();
        ChunkPos cp = new ChunkPos(f.origin());

        getGeology(level).replaceStoneInChunk(cp.x, cp.z, level);

        /*
        final OpenSimplexNoise noiseMap = new OpenSimplexNoise(level.getSeed());

        for (int x = cp.getMinBlockX(); x <= cp.getMaxBlockX(); x++) {
            for (int z = cp.getMinBlockZ(); z <= cp.getMaxBlockZ(); z++) {

                double FEATURE_SIZE = 4096;

                double value = noiseMap.eval(x / FEATURE_SIZE, z / FEATURE_SIZE, 0.0);

                ArrayList<RockLayer> LAYERS = new ArrayList<RockLayer>();

                Random random = new Random((long) ((value + 1) * 64));

                for (int i = 0; i < 24; i ++) {
                    RockLayer layerTemp = ROCK_LAYERS.get(random.nextInt(ROCK_LAYERS.size()));
                    if (ROCK_LAYERS.size() > 24) {
                        if (!LAYERS.contains(layerTemp)) {
                            LAYERS.add(layerTemp);
                        } else {
                            i--;
                        }
                    } else {
                        LAYERS.add(layerTemp);
                    }
                }


                for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {

                    for (int j = 0; j < LAYERS.size(); j++) {
                        Block rock = LAYERS.get(j).rock();
                        int minY = LAYERS.get(j).minY();
                        int maxY = LAYERS.get(j).maxY();
                        int thicknessH = LAYERS.get(j).thickness() / 2;

                        BlockPos p = new BlockPos(x, y, z);
                        BlockState state = level.getBlockState(p);

                        Random rnd = new Random((long) (value + 1 + j) * 64);
                        int center = minY + rnd.nextInt(maxY - minY);


                            if (y > center - thicknessH && y < center + thicknessH) {
                                if (VANILLA_STONE.contains(state.getBlock())) {
                                    level.setBlock(p, rock.defaultBlockState(), 2 | 16);
                                }
                            }
                    }
                }
            }
        }

        //Replace Remaining Vanilla Stone
        for (int x = cp.getMinBlockX(); x <= cp.getMaxBlockX(); x++) {
            for (int z = cp.getMinBlockZ(); z <= cp.getMaxBlockZ(); z++) {
                for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {

                    BlockPos p = new BlockPos(x, y, z);
                    BlockState state = level.getBlockState(p);

                    if (state.getBlock() == Blocks.STONE) {
                        level.setBlock(p, ROCK_LAYERS.get(0).rock().defaultBlockState(), 2 | 16);
                    }
                }
            }
        }

         */

        return true;
    }


}
