package shagejack.industrimania.content.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import shagejack.industrimania.content.world.gen.Geology;

import javax.annotation.ParametersAreNonnullByDefault;
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

        return true;
    }


}
