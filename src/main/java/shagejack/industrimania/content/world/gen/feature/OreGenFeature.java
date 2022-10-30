package shagejack.industrimania.content.world.gen.feature;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.world.gen.OreDepositShape;
import shagejack.industrimania.content.world.gen.OreGrade;
import shagejack.industrimania.content.world.gen.OreRegistry;
import shagejack.industrimania.content.world.gen.featureConfiguration.OreGenConfiguration;
import shagejack.industrimania.content.world.gen.record.Ore;
import shagejack.industrimania.content.world.nature.OreCapBlock;
import shagejack.industrimania.foundation.utility.CrossChunkGenerationHelper;
import shagejack.industrimania.registries.block.AllBlocks;
import shagejack.industrimania.registries.block.grouped.AllOres;
import shagejack.industrimania.registries.block.grouped.AllRocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static shagejack.industrimania.foundation.utility.GeometryIterationUtils.*;
import static shagejack.industrimania.foundation.utility.OreUtils.getAllGradeOre;
import static shagejack.industrimania.foundation.utility.OreUtils.getOreBlock;

public class OreGenFeature extends Feature<OreGenConfiguration> {

    private static final Set<Block> EXTRA_REPLACEABLE_BLOCK = Sets.newHashSet(
            Blocks.GRAVEL,
            Blocks.CLAY,
            Blocks.DIORITE,
            Blocks.ANDESITE,
            Blocks.GRANITE,
            Blocks.INFESTED_STONE,
            Blocks.INFESTED_DEEPSLATE,
            Blocks.ANCIENT_DEBRIS,
            Blocks.DIRT,
            Blocks.COARSE_DIRT,
            Blocks.STONE,
            Blocks.DEEPSLATE
    );

    // minY and maxY values are used in selecting ore type based on deposit center position
    public static final Set<Ore> ORES = new HashSet<>();

    public static void addOreGen(Ore... ore) {
        ORES.addAll(List.of(ore));
    }

    static {
        addOreGen(OreRegistry.hematite, OreRegistry.galena);
    }

    private static final List<Block> igneousStones = AllRocks.igneousStones.stream().map((rock) -> rock.block().get()).toList();
    private static final List<Block> metamorphicStones = AllRocks.metamorphicStones.stream().map((rock) -> rock.block().get()).toList();
    private static final List<Block> sedimentaryStones = AllRocks.sedimentaryStones.stream().map((rock) -> rock.block().get()).toList();

    private static final List<Block> allOres = AllOres.ORES.values().stream().map(ore -> ore.block().get()).toList();

    // private static final double DEPOSIT_GEN_PROBABILITY = 0.06;
    private static final double PLANT_DECAY_PROBABILITY = 0.75;
    private static final double PLANT_SIGN_GEN_PROBABILITY = 0.05;
    private static final double ORE_CAP_GEN_PROBABILITY = 0.02;
    private static final int MAX_DEPOSIT_SIZE = 14;
    private static final int MIN_DEPOSIT_SIZE = 6;
    private static final double MAX_ORE_BODY_CENTER_TO_DEPOSIT_CENTER_DISTANCE = 8.0;
    private static final int MAX_ORE_BODY_COUNT = 8;
    private static final int MIN_ORE_BODY_COUNT = 2;
    private static final int MAX_ORE_BODY_SIZE = 6;
    private static final int MIN_ORE_BODY_SIZE = 2;
    private static final double MAX_NORMAL_MULTIPLIER = 0.9;
    private static final double MAX_RICH_MULTIPLIER = 0.5;

    private static final double MAX_ORE_BODY_CENTER_TO_DEPOSIT_CENTER_DISTANCE_SQR = MAX_ORE_BODY_CENTER_TO_DEPOSIT_CENTER_DISTANCE * MAX_ORE_BODY_CENTER_TO_DEPOSIT_CENTER_DISTANCE;

    public OreGenFeature(Codec codec) {
        super(codec);
    }

    public static boolean isReplaceable(Block block) {
        if (igneousStones.contains(block) || metamorphicStones.contains(block) || sedimentaryStones.contains(block) || block == AllBlocks.rock_sandstone.block().get()) {
            return true;
        }

        return EXTRA_REPLACEABLE_BLOCK.contains(block);
    }

    public static final BiFunction<WorldGenLevel, BlockPos, Boolean> rockGenFun = (level, pos) -> isReplaceable(level.getBlockState(pos).getBlock());
    public static final BiFunction<WorldGenLevel, BlockPos, Boolean> oreGenFun = (level, pos) -> !allOres.contains(level.getBlockState(pos).getBlock()) && isReplaceable(level.getBlockState(pos).getBlock());
    public static final BiFunction<WorldGenLevel, BlockPos, Boolean> plantDecayFun = (level, pos) -> level.getRandom().nextDouble() < PLANT_DECAY_PROBABILITY && level.getBlockState(pos).is(BlockTags.REPLACEABLE_PLANTS) && level.getBlockState(pos).is(BlockTags.TALL_FLOWERS) && level.getBlockState(pos).is(BlockTags.SMALL_FLOWERS);
    public static final BiFunction<WorldGenLevel, BlockPos, Boolean> plantSignGenFun = (level, pos) -> level.getRandom().nextDouble() < PLANT_SIGN_GEN_PROBABILITY && Blocks.GRASS.defaultBlockState().canSurvive(level, pos) && (level.isEmptyBlock(pos) || level.getBlockState(pos).is(BlockTags.REPLACEABLE_PLANTS) || level.getBlockState(pos).is(Blocks.SNOW));
    public static final BiFunction<WorldGenLevel, BlockPos, Boolean> oreCapGenFun = (level, pos) -> level.getRandom().nextDouble() < ORE_CAP_GEN_PROBABILITY && OreCapBlock.canSurvive(level, pos) && (level.isEmptyBlock(pos) || level.getBlockState(pos).is(BlockTags.REPLACEABLE_PLANTS) || level.getBlockState(pos).is(Blocks.SNOW));

    private final CrossChunkGenerationHelper helper = new CrossChunkGenerationHelper();

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(FeaturePlaceContext<OreGenConfiguration> context) {
        if (context.chunkGenerator() instanceof FlatLevelSource) {
            return false;
        }

        double DEPOSIT_GEN_PROBABILITY = context.config().genChance;

        WorldGenLevel level = context.level();
        ChunkPos cp = new ChunkPos(context.origin());

        // do cross chunk generation
        helper.gen(level, cp);
        helper.tryForceGen();

        if (level.getRandom().nextDouble() >= DEPOSIT_GEN_PROBABILITY)
            return false;

        // Deposit Gen
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

        Ore depositOre = oresAvailable.get(level.getRandom().nextInt(oresAvailable.size()));

        /*
        =======================================
          Determining Deposit Shape & Blocks
        =======================================
         */

        List<BlockPos> depositOreBodyCenterSelection = new ArrayList<>();

        int rx, ry, rz, rx2, ry2, rz2;

        switch (OreDepositShape.getRandom(level.getRandom())) {
            case ELLIPSOID -> {
                rx = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE + 1) + MIN_DEPOSIT_SIZE;
                ry = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE + 1) + MIN_DEPOSIT_SIZE;
                rz = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE + 1) + MIN_DEPOSIT_SIZE;
                rx2 = (int) Math.pow(rx, 2);
                ry2 = (int) Math.pow(ry, 2);
                rz2 = (int) Math.pow(rz, 2);
                for (int dx = depositCenter.getX() - rx; dx < depositCenter.getX() + rx; dx++) {
                    for (int dy = depositCenter.getY() - ry; dy < depositCenter.getY() + ry; dy++) {
                        for (int dz = depositCenter.getZ() - rz; dz < depositCenter.getZ() + rz; dz++) {
                            if (isInEllipsoid(dx, dy, dz, depositCenter, rx2, ry2, rz2)) {
                                BlockPos blockPos = new BlockPos(dx, dy, dz);
                                if (level.hasChunk(SectionPos.blockToSectionCoord(dx), SectionPos.blockToSectionCoord(dz))) {
                                    Block block = level.getBlockState(blockPos).getBlock();
                                    if (isReplaceable(block)) {
                                        setBlock(level, blockPos, depositRock.defaultBlockState());
                                        if (blockPos.distSqr(depositCenter) < MAX_ORE_BODY_CENTER_TO_DEPOSIT_CENTER_DISTANCE_SQR) {
                                            depositOreBodyCenterSelection.add(blockPos);
                                        }
                                    }
                                }

                                if (!helper.isInChunk(cp, blockPos)) {
                                    helper.offer(level, rockGenFun, blockPos, depositRock.defaultBlockState());
                                }
                            }
                        }
                    }
                }
            }
            case CYLINDER -> {
                int height = 2 * (level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE + 1) + MIN_DEPOSIT_SIZE);
                rx = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE + 1) + MIN_DEPOSIT_SIZE;
                rz = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE + 1) + MIN_DEPOSIT_SIZE;
                rx2 = (int) Math.pow(rx, 2);
                rz2 = (int) Math.pow(rz, 2);
                for (int dx = depositCenter.getX() - rx; dx < depositCenter.getX() + rx; dx++) {
                    for (int dy = depositCenter.getY() - height / 2; dy < depositCenter.getY() + height / 2; dy++) {
                        for (int dz = depositCenter.getZ() - rz; dz < depositCenter.getZ() + rz; dz++) {
                            if (isInCylinder(dx, dy, dz, depositCenter, rx2, height, rz2)) {
                                BlockPos blockPos = new BlockPos(dx, dy, dz);
                                if (level.hasChunk(SectionPos.blockToSectionCoord(dx), SectionPos.blockToSectionCoord(dz))) {
                                    Block block = level.getBlockState(blockPos).getBlock();
                                    if (isReplaceable(block)) {
                                        setBlock(level, blockPos, depositRock.defaultBlockState());
                                        if (blockPos.distSqr(depositCenter) < MAX_ORE_BODY_CENTER_TO_DEPOSIT_CENTER_DISTANCE_SQR) {
                                            depositOreBodyCenterSelection.add(blockPos);
                                        }
                                    }
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
        =========================
          Generating Ore Bodies
        =========================
         */

        if (depositOreBodyCenterSelection.isEmpty())
            return false;

        List<Block> depositOreBlocks = new ArrayList<>();

        int oreBodyCount = level.getRandom().nextInt(MAX_ORE_BODY_COUNT - MIN_ORE_BODY_COUNT + 1) + MIN_ORE_BODY_COUNT;

        // gen multiple ore bodies
        for (int times = 0; times < oreBodyCount; times++) {
            Ore bodyOre = depositOre;

            // check if generate body as paragenesis
            if (depositOre.paragenesis() != null && depositOre.paragenesis().length > 0) {
                int index = level.getRandom().nextInt(depositOre.paragenesis().length);
                if (level.getRandom().nextDouble() < depositOre.paragenesis()[index].chanceAsParagenesis()) {
                    bodyOre = depositOre.paragenesis()[index];
                }
            }

            // add ore blocks of different level to deposit ore block list
            getAllGradeOre(bodyOre, depositRock).forEach(oreBlock -> {
                if (!depositOreBlocks.contains(oreBlock))
                    depositOreBlocks.add(oreBlock);
            });

            BlockPos oreBodyCenter = depositOreBodyCenterSelection.get(level.getRandom().nextInt(depositOreBodyCenterSelection.size()));

            int rxb = level.getRandom().nextInt(MAX_ORE_BODY_SIZE - MIN_ORE_BODY_SIZE + 1) + MIN_ORE_BODY_SIZE;
            int ryb = level.getRandom().nextInt(MAX_ORE_BODY_SIZE - MIN_ORE_BODY_SIZE + 1) + MIN_ORE_BODY_SIZE;
            int rzb = level.getRandom().nextInt(MAX_ORE_BODY_SIZE - MIN_ORE_BODY_SIZE + 1) + MIN_ORE_BODY_SIZE;
            double rxb2 = Math.pow(rxb, 2);
            double ryb2 = Math.pow(ryb, 2);
            double rzb2 = Math.pow(rzb, 2);

            double mNormal = level.getRandom().nextDouble() * MAX_NORMAL_MULTIPLIER;
            double mRich = level.getRandom().nextDouble() * mNormal * MAX_RICH_MULTIPLIER;

            double mNormalRightValue = Math.pow(mNormal, 2);
            double mRichRightValue = Math.pow(mRich, 2);

            // Ore Generation
            for (int dx = oreBodyCenter.getX() - rxb; dx < oreBodyCenter.getX() + rxb; dx++) {
                for (int dy = oreBodyCenter.getY() - ryb; dy < oreBodyCenter.getY() + ryb; dy++) {
                    for (int dz = oreBodyCenter.getZ() - rzb; dz < oreBodyCenter.getZ() + rzb; dz++) {
                        BlockPos pos;

                        double leftValue = Math.pow(dx - oreBodyCenter.getX(), 2) / rxb2 + Math.pow(dy - oreBodyCenter.getY(), 2) / ryb2 + Math.pow(dz - oreBodyCenter.getZ(), 2) / rzb2;
                        // Rich Ore Generation
                        if (leftValue <= mRichRightValue) {
                            pos = new BlockPos(dx, dy, dz);

                            if (!helper.isInChunk(cp, pos)) {
                                helper.offer(level, oreGenFun, pos, getOreBlock(bodyOre, depositRock, OreGrade.RICH).defaultBlockState());
                            }

                            if (level.hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()))) {
                                if (!depositOreBlocks.contains(level.getBlockState(pos).getBlock())) {
                                    setBlock(level, pos, getOreBlock(bodyOre, depositRock, OreGrade.RICH).defaultBlockState());
                                }
                            }
                            continue;
                        }
                        // Normal Ore Generation
                        if (leftValue <= mNormalRightValue) {
                            pos = new BlockPos(dx, dy, dz);

                            if (!helper.isInChunk(cp, pos)) {
                                helper.offer(level, oreGenFun, pos, getOreBlock(bodyOre, depositRock, OreGrade.NORMAL).defaultBlockState());
                            }

                            if (level.hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()))) {
                                if (!depositOreBlocks.contains(level.getBlockState(pos).getBlock())) {
                                    setBlock(level, pos, getOreBlock(bodyOre, depositRock, OreGrade.NORMAL).defaultBlockState());
                                }
                            }
                            continue;
                        }
                        // Poor Ore Generation
                        if (leftValue <= 1.0d) {
                            pos = new BlockPos(dx, dy, dz);

                            if (!helper.isInChunk(cp, pos)) {
                                helper.offer(level, oreGenFun, pos, getOreBlock(bodyOre, depositRock, OreGrade.POOR).defaultBlockState());
                            }

                            if (level.hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()))) {
                                if (!depositOreBlocks.contains(level.getBlockState(pos).getBlock())) {
                                    setBlock(level, pos, getOreBlock(bodyOre, depositRock, OreGrade.POOR).defaultBlockState());
                                }
                            }
                        }
                    }
                }
            }
        }

        /*
        ======================
          Surface Decoration
        ======================
         */

        //TODO: fix surface generation (it works sometimes now)

        Stream<BlockPos> surface = getEllipseStream(depositCenter, rx, rz)
                .map(pos -> {
                    BlockPos surfacePos = new BlockPos(pos.getX(), chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ()) + 1, pos.getZ());
                    while (surfacePos.getY() > 0 && level.isEmptyBlock(surfacePos.below())) {
                        surfacePos = surfacePos.below();
                    }
                    return surfacePos;
                });

        // Surface Plant Modification
        surface.forEach(pos -> {
            //Plant Decay
            if (level.hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()))) {
                if (plantDecayFun.apply(level, pos))
                    level.removeBlock(pos, false);
                if (!helper.isInChunk(cp, pos))
                    helper.offer(level, plantDecayFun, pos, Blocks.AIR.defaultBlockState());
            } else {
                helper.offer(level, plantDecayFun, pos, Blocks.AIR.defaultBlockState());
            }

            // Plant Sign Generation
            if (depositOre.plantSign() != null) {
                if (level.hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()))) {
                    if (plantSignGenFun.apply(level, pos))
                        level.setBlock(pos, depositOre.plantSign().defaultBlockState(), 0);

                    if (!helper.isInChunk(cp, pos))
                        helper.offer(level, plantSignGenFun, pos, depositOre.plantSign().defaultBlockState());
                } else {
                    helper.offer(level, plantDecayFun, pos, Blocks.AIR.defaultBlockState());
                }
            }

            // Ore Cap Generation
            if (depositOre.oreCap() != null) {
                if (level.hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()))) {
                    if (oreCapGenFun.apply(level, pos))
                        level.setBlock(pos, depositOre.oreCap().defaultBlockState(), 0);

                    if (!helper.isInChunk(cp, pos))
                        helper.offer(level, oreCapGenFun, pos, depositOre.oreCap().defaultBlockState());
                } else {
                    helper.offer(level, plantDecayFun, pos, Blocks.AIR.defaultBlockState());
                }
            }
        });

        return true;
    }
}
