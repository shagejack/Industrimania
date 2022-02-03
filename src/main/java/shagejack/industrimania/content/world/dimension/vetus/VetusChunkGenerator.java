package shagejack.industrimania.content.world.dimension.vetus;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.material.MaterialRuleList;
import net.minecraft.world.level.levelgen.material.WorldGenMaterialRule;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import shagejack.industrimania.content.world.dimension.IMAbstractChunkGenerator;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class VetusChunkGenerator extends IMAbstractChunkGenerator {

    public static final Codec<VetusChunkGenerator> CODEC = RecordCodecBuilder.create((p_188643_) -> {
        return p_188643_.group(RegistryLookupCodec.create(Registry.NOISE_REGISTRY).forGetter((p_188716_) -> {
            return p_188716_.noises;
        }), BiomeSource.CODEC.fieldOf("biome_source").forGetter((p_188711_) -> {
            return p_188711_.biomeSource;
        }), Codec.LONG.fieldOf("seed").stable().forGetter((p_188690_) -> {
            return p_188690_.seed;
        }), NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter((p_188652_) -> {
            return p_188652_.settings;
        })).apply(p_188643_, p_188643_.stable(VetusChunkGenerator::new));
    });

    private static final BlockState AIR = Blocks.AIR.defaultBlockState();
    private static final BlockState[] EMPTY_COLUMN = new BlockState[0];
    protected final BlockState defaultBlock;
    private final Registry<NormalNoise.NoiseParameters> noises;
    protected final long seed;
    protected final Supplier<NoiseGeneratorSettings> settings;
    protected final NoiseSampler sampler;
    private final SurfaceSystem surfaceSystem;
    private final WorldGenMaterialRule materialRule;
    private final Aquifer.FluidPicker globalFluidPicker;

    public VetusChunkGenerator(Registry<NormalNoise.NoiseParameters> noises, BiomeSource biomeSource, long seed, Supplier<NoiseGeneratorSettings> noiseGeneratorSettingsSupplier) {
        this(noises, biomeSource, biomeSource, seed, noiseGeneratorSettingsSupplier);
    }

    public VetusChunkGenerator(Registry<NormalNoise.NoiseParameters> noises, BiomeSource biomeSource, BiomeSource runtimeBiomeSource, long seed, Supplier<NoiseGeneratorSettings> noiseGeneratorSettingsSupplier) {
        super(biomeSource, runtimeBiomeSource, noiseGeneratorSettingsSupplier.get().structureSettings(), seed);
        this.noises = noises;
        this.seed= seed;
        this.settings = noiseGeneratorSettingsSupplier;
        NoiseGeneratorSettings noiseGeneratorSettings = this.settings.get();
        this.defaultBlock = noiseGeneratorSettings.getDefaultBlock();
        NoiseSettings noiseSettings = noiseGeneratorSettings.noiseSettings();
        this.sampler = new NoiseSampler(noiseSettings, noiseGeneratorSettings.isNoiseCavesEnabled(), seed, noises, noiseGeneratorSettings.getRandomSource());
        ImmutableList.Builder<WorldGenMaterialRule> builder = ImmutableList.builder();
        this.materialRule = new MaterialRuleList(builder.build());
        Aquifer.FluidStatus aquifer$fluidStatus = new Aquifer.FluidStatus(-54, Blocks.LAVA.defaultBlockState());
        int seaLevel = noiseGeneratorSettings.seaLevel();
        Aquifer.FluidStatus aquifer$fluidStatus1 = new Aquifer.FluidStatus(seaLevel, noiseGeneratorSettings.getDefaultFluid());
        Aquifer.FluidStatus aquifer$fluidStatus2 = new Aquifer.FluidStatus(noiseSettings.minY() - 1, Blocks.AIR.defaultBlockState());
        this.globalFluidPicker = (p_198228_, p_198229_, p_198230_) -> {
            return p_198229_ < Math.min(-54, seaLevel) ? aquifer$fluidStatus : aquifer$fluidStatus1;
        };
        this.surfaceSystem = new SurfaceSystem(noises, this.defaultBlock, seaLevel, seed, noiseGeneratorSettings.getRandomSource());
    }


    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return null;
    }

    @Override
    public ChunkGenerator withSeed(long p_62156_) {
        return null;
    }

    @Override
    public Climate.Sampler climateSampler() {
        return null;
    }

    @Override
    public void applyCarvers(WorldGenRegion p_187691_, long p_187692_, BiomeManager p_187693_, StructureFeatureManager p_187694_, ChunkAccess p_187695_, GenerationStep.Carving p_187696_) {

    }

    @Override
    public void buildSurface(WorldGenRegion p_187697_, StructureFeatureManager p_187698_, ChunkAccess p_187699_) {

    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion p_62167_) {

    }

    @Override
    public int getGenDepth() {
        return 0;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor p_187748_, Blender p_187749_, StructureFeatureManager p_187750_, ChunkAccess p_187751_) {
        return null;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getBaseHeight(int p_156153_, int p_156154_, Heightmap.Types p_156155_, LevelHeightAccessor p_156156_) {
        return 0;
    }

    @Override
    public NoiseColumn getBaseColumn(int p_156150_, int p_156151_, LevelHeightAccessor p_156152_) {
        return null;
    }
}
