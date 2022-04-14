package shagejack.industrimania.content.world.gen.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.ForgeRegistries;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.world.gen.OreRegistry;
import shagejack.industrimania.content.world.gen.record.Ore;
import shagejack.industrimania.foundation.utility.CrossChunkGenerationHelper;
import shagejack.industrimania.registers.block.AllBlocks;
import shagejack.industrimania.registers.block.grouped.AllOres;
import shagejack.industrimania.registers.block.grouped.AllRocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static shagejack.industrimania.foundation.utility.GeometryIterateUtils.isInCylinder;
import static shagejack.industrimania.foundation.utility.GeometryIterateUtils.isInEllipsoid;

public class OreGenFeature extends Feature<NoneFeatureConfiguration> {

    private static final ArrayList<Block> EXTRA_REPLACEABLE_BLOCK = Lists.newArrayList(
            Blocks.GRAVEL,
            Blocks.CLAY,
            Blocks.DIORITE,
            Blocks.ANDESITE,
            Blocks.GRANITE,
            Blocks.INFESTED_STONE,
            Blocks.INFESTED_DEEPSLATE,
            Blocks.ANCIENT_DEBRIS,
            Blocks.DIRT,
            Blocks.COARSE_DIRT
    );

    private static final ArrayList<Block> PLANT_TO_DECAY = Lists.newArrayList(
            Blocks.GRASS,
            Blocks.FERN,
            Blocks.TALL_GRASS,
            Blocks.LARGE_FERN,
            Blocks.ORANGE_TULIP,
            Blocks.PINK_TULIP,
            Blocks.RED_TULIP,
            Blocks.WHITE_TULIP,
            Blocks.OXEYE_DAISY,
            Blocks.SUNFLOWER,
            Blocks.LILY_PAD,
            Blocks.CACTUS,
            Blocks.BAMBOO_SAPLING,
            Blocks.ACACIA_SAPLING,
            Blocks.BIRCH_SAPLING,
            Blocks.DARK_OAK_SAPLING,
            Blocks.JUNGLE_SAPLING,
            Blocks.OAK_SAPLING,
            Blocks.SPRUCE_SAPLING,
            Blocks.BLUE_ORCHID,
            Blocks.POPPY,
            Blocks.CORNFLOWER,
            Blocks.LILY_OF_THE_VALLEY,
            Blocks.DANDELION,
            Blocks.ROSE_BUSH
    );


    //minY and maxY values are used in selecting ore type based on deposit center position
    public static final List<Ore> ORES = Lists.newArrayList(
            OreRegistry.hematite,
            OreRegistry.galena
    );

    private static final List<Block> igneousStones = AllRocks.igneousStones.stream().map((rock) -> rock.block().get()).toList();
    private static final List<Block> metamorphicStones = AllRocks.metamorphicStones.stream().map((rock) -> rock.block().get()).toList();
    private static final List<Block> sedimentaryStones = AllRocks.sedimentaryStones.stream().map((rock) -> rock.block().get()).toList();

    private static final List<Block> allOres = AllOres.ORES.values().stream().map(ore -> ore.block().get()).toList();

    private static final double DEPOSIT_GEN_PROBABILITY = 0.06;
    private static final double PLANT_DECAY_PROBABILITY = 0.75;
    private static final double PLANT_SIGN_GEN_PROBABILITY = 0.15;
    private static final double ORE_CAP_GEN_PROBABILITY = 0.01;
    private static final int MAX_DEPOSIT_SIZE = 16;
    private static final int MIN_DEPOSIT_SIZE = 4;
    private static final int MAX_ORE_BODY_COUNT = 16;
    private static final int MIN_ORE_BODY_COUNT = 4;
    private static final int MAX_ORE_BODY_SIZE = 8;
    private static final int MIN_ORE_BODY_SIZE = 2;
    private static final double MAX_NORMAL_MULTIPLIER = 0.9;
    private static final double MAX_RICH_MULTIPLIER = 0.5;

    public OreGenFeature(Codec codec) {
        super(codec);
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

    public static boolean isReplaceable(Block block) {
        if (igneousStones.contains(block) || metamorphicStones.contains(block) || sedimentaryStones.contains(block)) {
            return true;
        }

        if (block == AllBlocks.rock_sandstone.block().get()) {
            return true;
        }

        return EXTRA_REPLACEABLE_BLOCK.contains(block);
    }

    public static final BiFunction<WorldGenLevel, BlockPos, Boolean> rockGenFun = (level, pos) -> isReplaceable(level.getBlockState(pos).getBlock());
    public static final BiFunction<WorldGenLevel, BlockPos, Boolean> oreGenFun = (level, pos) -> !allOres.contains(level.getBlockState(pos).getBlock());
    public static final BiFunction<WorldGenLevel, BlockPos, Boolean> plantDecayFun = (level, pos) -> Math.random() < PLANT_DECAY_PROBABILITY && !PLANT_TO_DECAY.contains(level.getBlockState(pos).getBlock());
    public static final BiFunction<WorldGenLevel, BlockPos, Boolean> plantSignGenFun = (level, pos) -> Math.random() < PLANT_SIGN_GEN_PROBABILITY && level.isEmptyBlock(pos);
    public static final BiFunction<WorldGenLevel, BlockPos, Boolean> oreCapGenFun = (level, pos) -> Math.random() < ORE_CAP_GEN_PROBABILITY && level.isEmptyBlock(pos);

    public CrossChunkGenerationHelper helper = new CrossChunkGenerationHelper();

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> f) {
        if (f.chunkGenerator() instanceof FlatLevelSource) {
            return false;
        }

        WorldGenLevel level = f.level();
        ChunkPos cp = new ChunkPos(f.origin());

        helper.gen(level, cp);


        if (level.getRandom().nextDouble() < DEPOSIT_GEN_PROBABILITY) {
            //Deposit Gen
            ChunkAccess chunk = level.getChunk(cp.x, cp.z);

            int maxY = level.getMaxBuildHeight();

            for (int x = cp.getMinBlockX(); x <= cp.getMaxBlockX(); x++) {
                for (int z = cp.getMinBlockZ(); z <= cp.getMaxBlockZ(); z++) {
                    int y = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z);
                    if (y < maxY) {
                        maxY = y;
                    }
                }
            }

            int lx = cp.getMaxBlockX() - cp.getMinBlockX() + 1;
            int ly = maxY - level.getMinBuildHeight() + 1;
            int lz = cp.getMaxBlockZ() - cp.getMinBlockZ() + 1;

            BlockPos depositCenter = new BlockPos(cp.getMinBlockX() + level.getRandom().nextInt(lx), level.getMinBuildHeight() + level.getRandom().nextInt(ly), cp.getMinBlockZ() + level.getRandom().nextInt(lz));
            Block depositRock = level.getBlockState(depositCenter).getBlock();
            List<Ore> oresAvailable = new ArrayList<>();

            for (Ore ore : ORES) {
                if (depositCenter.getY() > ore.minY() && depositCenter.getY() < ore.maxY()) {
                    if (ore.availableRock() != null && ore.availableRock().contains(depositRock)) {
                        oresAvailable.add(ore);
                    }
                }
            }

            if (oresAvailable.isEmpty()) {
                return false;
            }

            Ore depositOre;

            depositOre = oresAvailable.get(level.getRandom().nextInt(oresAvailable.size()));

            /*
            Define Deposit Shape & Blocks
            Deposit Shape:
            0:Ellipsoid
            1:Cylinder
             */

            //TODO: optimize shape iteration by using GeometryIterateUtils#getPosInOtherQuadrant

            int shape = level.getRandom().nextInt(2);
            List<BlockPos> deposit = new ArrayList<>();

            int rx, ry, rz, rx2, ry2, rz2;

            switch (shape) {
                case 0 -> {
                    //Ellipsoid
                    rx = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1;
                    ry = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1;
                    rz = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1;
                    rx2 = (int) Math.pow(rx, 2);
                    ry2 = (int) Math.pow(ry, 2);
                    rz2 = (int) Math.pow(rz, 2);
                    for (int dx = depositCenter.getX() - rx; dx < depositCenter.getX() + rx; dx++) {
                        for (int dy = depositCenter.getY() - ry; dy < depositCenter.getY() + ry; dy++) {
                            for (int dz = depositCenter.getZ() - rz; dz < depositCenter.getZ() + rz; dz++) {
                                if (isInEllipsoid(dx, dy, dz, depositCenter, rx2, ry2, rz2)) {
                                    BlockPos blockPos = new BlockPos(dx, dy, dz);
                                    Block block = level.getBlockState(blockPos).getBlock();
                                    if (isReplaceable(block)) {
                                        setBlock(level, blockPos, depositRock.defaultBlockState());
                                        deposit.add(blockPos);
                                    }

                                    if (!helper.isInChunk(cp, blockPos)) {
                                        helper.offer(level, rockGenFun, blockPos, depositRock.defaultBlockState());
                                    }
                                }
                            }
                        }
                    }
                }
                case 1 -> {
                    //Cylinder
                    int height = 2 * (level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1);
                    rx = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1;
                    rz = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1;
                    rx2 = (int) Math.pow(rx, 2);
                    rz2 = (int) Math.pow(rz, 2);
                    for (int dx = depositCenter.getX() - rx; dx < depositCenter.getX() + rx; dx++) {
                        for (int dy = depositCenter.getY() - height / 2; dy < depositCenter.getY() + height / 2; dy++) {
                            for (int dz = depositCenter.getZ() - rz; dz < depositCenter.getZ() + rz; dz++) {
                                if (isInCylinder(dx, dy, dz, depositCenter, rx2, height, rz2)) {
                                    BlockPos blockPos = new BlockPos(dx, dy, dz);
                                    Block block = level.getBlockState(blockPos).getBlock();
                                    if (isReplaceable(block)) {
                                        setBlock(level, blockPos, depositRock.defaultBlockState());
                                        deposit.add(blockPos);
                                    }

                                    if (!helper.isInChunk(cp, blockPos)) {
                                        helper.offer(level, rockGenFun, blockPos, depositRock.defaultBlockState());
                                    }
                                }
                            }
                        }
                    }
                }
                default -> {
                    Industrimania.LOGGER.warn("Deposit generated wrongly. THIS SHOULDN'T HAPPEN!");
                    return false;
                }
            }

            /*
            Generating Ore Bodies
             */

            if (deposit.size() > 0) {

                List<Block> depositOreBlocks = new ArrayList<>();

                int oreBodyCount = level.getRandom().nextInt(MAX_ORE_BODY_COUNT - MIN_ORE_BODY_COUNT) + MIN_ORE_BODY_COUNT + 1;

                for (int i = 0; i < oreBodyCount; i++) {

                    Ore bodyOre = depositOre;

                    if (depositOre.paragenesis() != null) {
                        if (depositOre.chanceAsParagenesis() != 0) {
                            int index = level.getRandom().nextInt(depositOre.paragenesis().length);
                            if (Math.random() < depositOre.paragenesis()[index].chanceAsParagenesis()) {
                                bodyOre = depositOre.paragenesis()[index];
                            }
                        }
                    }

                    for (int j = 0; j <= 2; j++) {
                        Block oreBlock = getOreBlock(bodyOre, depositRock, j);
                        if (!depositOreBlocks.contains(oreBlock)) {
                            depositOreBlocks.add(oreBlock);
                        }
                    }

                    BlockPos oreBodyCenter = deposit.get(level.getRandom().nextInt(deposit.size()));

                    int rxb = level.getRandom().nextInt(MAX_ORE_BODY_SIZE - MIN_ORE_BODY_SIZE) + MIN_ORE_BODY_SIZE + 1;
                    int ryb = level.getRandom().nextInt(MAX_ORE_BODY_SIZE - MIN_ORE_BODY_SIZE) + MIN_ORE_BODY_SIZE + 1;
                    int rzb = level.getRandom().nextInt(MAX_ORE_BODY_SIZE - MIN_ORE_BODY_SIZE) + MIN_ORE_BODY_SIZE + 1;
                    int rxb2 = (int) Math.pow(rxb, 2);
                    int ryb2 = (int) Math.pow(ryb, 2);
                    int rzb2 = (int) Math.pow(rzb, 2);

                    double mNormal = level.getRandom().nextDouble() * MAX_NORMAL_MULTIPLIER;
                    double mRich = level.getRandom().nextDouble() * mNormal * MAX_RICH_MULTIPLIER;

                    double mNormal2 = Math.pow(mNormal, 2);
                    double mRich2 = Math.pow(mRich, 2);

                    //Ore Generation
                    for (int dx = oreBodyCenter.getX() - rxb; dx < oreBodyCenter.getX() + rxb; dx++) {
                        for (int dy = oreBodyCenter.getY() - ryb; dy < oreBodyCenter.getY() + ryb; dy++) {
                            for (int dz = oreBodyCenter.getZ() - rzb; dz < oreBodyCenter.getZ() + rzb; dz++) {
                                BlockPos pos;

                                //Rich Ore Generation
                                if (isInEllipsoid(dx, dy, dz, oreBodyCenter, (int) (rxb2 * mRich2), (int) (ryb2 * mRich2), (int) (rzb2 * mRich2))) {
                                    pos = new BlockPos(dx, dy, dz);

                                    if (!helper.isInChunk(cp, pos)) {
                                        helper.offer(level, oreGenFun, pos, getOreBlock(bodyOre, depositRock, 2).defaultBlockState());
                                    }

                                    if (!depositOreBlocks.contains(level.getBlockState(pos).getBlock())) {
                                        setBlock(level, pos, getOreBlock(bodyOre, depositRock, 2).defaultBlockState());
                                        continue;
                                    }
                                }
                                //Normal Ore Generation
                                if (isInEllipsoid(dx, dy, dz, oreBodyCenter, (int) (rxb2 * mNormal2), (int) (ryb2 * mNormal2), (int) (rzb2 * mNormal2))) {
                                    pos = new BlockPos(dx, dy, dz);

                                    if (!helper.isInChunk(cp, pos)) {
                                        helper.offer(level, oreGenFun, pos, getOreBlock(bodyOre, depositRock, 1).defaultBlockState());
                                    }

                                    if (!depositOreBlocks.contains(level.getBlockState(pos).getBlock())) {
                                        setBlock(level, pos, getOreBlock(bodyOre, depositRock, 1).defaultBlockState());
                                        continue;
                                    }
                                }
                                //Rich Ore Generation
                                if (isInEllipsoid(dx, dy, dz, oreBodyCenter, rxb2, ryb2, rzb2)) {
                                    pos = new BlockPos(dx, dy, dz);

                                    if (!helper.isInChunk(cp, pos)) {
                                        helper.offer(level, oreGenFun, pos, getOreBlock(bodyOre, depositRock, 0).defaultBlockState());
                                    }

                                    if (!depositOreBlocks.contains(level.getBlockState(pos).getBlock())) {
                                        setBlock(level, pos, getOreBlock(bodyOre, depositRock, 0).defaultBlockState());
                                    }
                                }
                            }
                        }
                    }

                    /*
                    ==================
                    Surface Decoration
                    ==================
                     */

                    //TODO: fix source generation

                    List<BlockPos> surface = new ArrayList<>();

                    for (int dx = depositCenter.getX() - rx; dx < depositCenter.getX() + rx; dx++) {
                        for (int dz = depositCenter.getZ() - rz; dz < depositCenter.getZ() + rz; dz++) {
                            if (Math.pow(dx, 2) / rx2 + Math.pow(dz, 2) / rz2 <= 1) {
                                BlockPos surfacePos = new BlockPos(dx, chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, dx, dz) + 1, dz);

                                while (surfacePos.getY() > 0 && level.isEmptyBlock(surfacePos.below())) {
                                    surfacePos = surfacePos.below();
                                }

                                surface.add(surfacePos);
                            }
                        }
                    }

                    //Surface Plant Modification
                    for (BlockPos pos : surface) {

                        //Plant Decay
                        helper.offer(level, plantDecayFun, pos, Blocks.AIR.defaultBlockState());

                        //Plant Sign Generation
                        if (depositOre.plantSign() != null) {
                            helper.offer(level, plantSignGenFun, pos, depositOre.plantSign().defaultBlockState());
                        }

                        //Ore Cap Generation
                        if (depositOre.oreCap() != null) {
                            helper.offer(level, oreCapGenFun, pos, depositOre.oreCap().defaultBlockState());
                        }
                    }

                }
                return true;
            }
        }

        return false;
    }


}
