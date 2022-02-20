package shagejack.industrimania.content.world.dimension.vetus;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import shagejack.industrimania.content.world.dimension.IMAbstractBiomeSource;
import shagejack.industrimania.registers.AllBiomes;

public class VetusBiomeSource extends IMAbstractBiomeSource implements BiomeManager.NoiseBiomeSource {

    public static final Codec<VetusBiomeSource> CODEC = RecordCodecBuilder.create((p_48644_) -> {
        return p_48644_.group(RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter((p_151890_) -> {
            return p_151890_.biomes;
        }), Codec.LONG.fieldOf("seed").stable().forGetter((p_151888_) -> {
            return p_151888_.seed;
        })).apply(p_48644_, p_48644_.stable(VetusBiomeSource::new));
    });

    private static final float ISLAND_THRESHOLD = -0.9F;
    public static final int ISLAND_CHUNK_DISTANCE = 64;
    private static final long ISLAND_CHUNK_DISTANCE_SQR = 4096L;
    private final SimplexNoise islandNoise;
    private final Registry<Biome> biomes;
    private final long seed;
    private final Biome vetus;

    public VetusBiomeSource(Registry<Biome> biomes, long seed) {
        this(biomes, seed, biomes.getOrThrow(AllBiomes.wastelands));
    }

    private VetusBiomeSource(Registry<Biome> biomes, long seed, Biome vetus) {
        super(ImmutableList.of(vetus));
        this.biomes = biomes;
        this.seed = seed;
        this.vetus = vetus;
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(seed));
        worldgenrandom.consumeCount(17292);
        this.islandNoise = new SimplexNoise(worldgenrandom);
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        int i = x >> 2;
        int j = z >> 2;
        if ((long)i * (long)i + (long)j * (long)j <= 4096L) {
            return this.vetus;
        } else {
            float f = getHeightValue(this.islandNoise, i * 2 + 1, j * 2 + 1);
            if (f > 40.0F) {
                return this.vetus;
            } else if (f >= 0.0F) {
                return this.vetus;
            } else {
                return this.vetus;
            }
        }
    }


    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public BiomeSource withSeed(long seed) {
        return new VetusBiomeSource(this.biomes, seed, this.vetus);
    }

    public static float getHeightValue(SimplexNoise noise, int p_48647_, int p_48648_) {
        int i = p_48647_ / 2;
        int j = p_48648_ / 2;
        int k = p_48647_ % 2;
        int l = p_48648_ % 2;
        float f = 100.0F - Mth.sqrt((float)(p_48647_ * p_48647_ + p_48648_ * p_48648_)) * 8.0F;
        f = Mth.clamp(f, -100.0F, 80.0F);

        for(int i1 = -12; i1 <= 12; ++i1) {
            for(int j1 = -12; j1 <= 12; ++j1) {
                long k1 = (long)(i + i1);
                long l1 = (long)(j + j1);
                if (k1 * k1 + l1 * l1 > 4096L && noise.getValue((double)k1, (double)l1) < (double)-0.9F) {
                    float f1 = (Mth.abs((float)k1) * 3439.0F + Mth.abs((float)l1) * 147.0F) % 13.0F + 9.0F;
                    float f2 = (float)(k - i1 * 2);
                    float f3 = (float)(l - j1 * 2);
                    float f4 = 100.0F - Mth.sqrt(f2 * f2 + f3 * f3) * f1;
                    f4 = Mth.clamp(f4, -100.0F, 80.0F);
                    f = Math.max(f, f4);
                }
            }
        }

        return f;
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        int i = x >> 2;
        int j = z >> 2;
        if ((long)i * (long)i + (long)j * (long)j <= 4096L) {
            return this.vetus;
        } else {
            float f = getHeightValue(this.islandNoise, i * 2 + 1, j * 2 + 1);
            if (f > 40.0F) {
                return this.vetus;
            } else if (f >= 0.0F) {
                return this.vetus;
            } else {
                return this.vetus;
            }
        }
    }
}
