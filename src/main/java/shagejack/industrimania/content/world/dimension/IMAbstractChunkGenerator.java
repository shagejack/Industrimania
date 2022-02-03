package shagejack.industrimania.content.world.dimension;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class IMAbstractChunkGenerator extends ChunkGenerator {

    public IMAbstractChunkGenerator(BiomeSource biomeSource, StructureSettings structureSettings) {
        super(biomeSource, structureSettings);
    }

    public IMAbstractChunkGenerator(BiomeSource biomeSource, BiomeSource runtimeBiomeSource, StructureSettings structureSettings, long seed) {
        super(biomeSource, runtimeBiomeSource, structureSettings, seed);
    }

    @Override
    protected abstract Codec<? extends ChunkGenerator> codec();

    @Override
    public abstract ChunkGenerator withSeed(long p_62156_);

    @Override
    public abstract Climate.Sampler climateSampler();

    @Override
    public abstract void applyCarvers(WorldGenRegion p_187691_, long p_187692_, BiomeManager p_187693_, StructureFeatureManager p_187694_, ChunkAccess p_187695_, GenerationStep.Carving p_187696_);

    @Override
    public abstract void buildSurface(WorldGenRegion p_187697_, StructureFeatureManager p_187698_, ChunkAccess p_187699_);

    @Override
    public abstract void spawnOriginalMobs(WorldGenRegion p_62167_);

    @Override
    public abstract int getGenDepth();

    @Override
    public abstract CompletableFuture<ChunkAccess> fillFromNoise(Executor p_187748_, Blender p_187749_, StructureFeatureManager p_187750_, ChunkAccess p_187751_);

    @Override
    public abstract int getSeaLevel();

    @Override
    public abstract int getMinY();

    @Override
    public abstract int getBaseHeight(int p_156153_, int p_156154_, Heightmap.Types p_156155_, LevelHeightAccessor p_156156_);

    @Override
    public abstract NoiseColumn getBaseColumn(int p_156150_, int p_156151_, LevelHeightAccessor p_156152_);
}
