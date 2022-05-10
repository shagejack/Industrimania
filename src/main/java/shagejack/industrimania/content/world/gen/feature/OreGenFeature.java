package shagejack.industrimania.content.world.gen.feature;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraftforge.registries.ForgeRegistries;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.world.gen.OreGrade;
import shagejack.industrimania.content.world.gen.OreRegistry;
import shagejack.industrimania.content.world.gen.featureConfiguration.OreGenConfiguration;
import shagejack.industrimania.content.world.gen.record.Ore;
import shagejack.industrimania.foundation.utility.CrossChunkGenerationHelper;
import shagejack.industrimania.registers.block.AllBlocks;
import shagejack.industrimania.registers.block.grouped.AllOres;
import shagejack.industrimania.registers.block.grouped.AllRocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
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

    // optimize later, for test only
    private static final Set<Block> PLANT_TO_DECAY = Stream.of(
            ForgeRegistries.BLOCKS.tags().getTag(BlockTags.REPLACEABLE_PLANTS).stream(),
            ForgeRegistries.BLOCKS.tags().getTag(BlockTags.TALL_FLOWERS).stream(),
            ForgeRegistries.BLOCKS.tags().getTag(BlockTags.SMALL_FLOWERS).stream()
    ).flatMap(s -> s).collect(Collectors.toSet());


    // minY and maxY values are used in selecting ore type based on deposit center position
    public static final Set<Ore> ORES = Sets.newHashSet(
            OreRegistry.hematite,
            OreRegistry.galena
    );

    private static final List<Block> igneousStones = AllRocks.igneousStones.stream().map((rock) -> rock.block().get()).toList();
    private static final List<Block> metamorphicStones = AllRocks.metamorphicStones.stream().map((rock) -> rock.block().get()).toList();
    private static final List<Block> sedimentaryStones = AllRocks.sedimentaryStones.stream().map((rock) -> rock.block().get()).toList();

    private static final List<Block> allOres = AllOres.ORES.values().stream().map(ore -> ore.block().get()).toList();

    // private static final double DEPOSIT_GEN_PROBABILITY = 0.06;
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

    public static boolean isReplaceable(Block block) {
        if (igneousStones.contains(block) || metamorphicStones.contains(block) || sedimentaryStones.contains(block) || block == AllBlocks.rock_sandstone.block().get()) {
            return true;
        }

        return EXTRA_REPLACEABLE_BLOCK.contains(block);
    }

    public static final BiFunction<WorldGenLevel, BlockPos, Boolean> rockGenFun = (level, pos) -> isReplaceable(level.getBlockState(pos).getBlock());
    public static final BiFunction<WorldGenLevel, BlockPos, Boolean> oreGenFun = (level, pos) -> !allOres.contains(level.getBlockState(pos).getBlock());
    public static final BiFunction<WorldGenLevel, BlockPos, Boolean> plantDecayFun = (level, pos) -> Math.random() < PLANT_DECAY_PROBABILITY && PLANT_TO_DECAY.contains(level.getBlockState(pos).getBlock());
    public static final BiFunction<WorldGenLevel, BlockPos, Boolean> plantSignGenFun = (level, pos) -> Math.random() < PLANT_SIGN_GEN_PROBABILITY && (level.isEmptyBlock(pos) || level.getBlockState(pos).is(BlockTags.REPLACEABLE_PLANTS));
    public static final BiFunction<WorldGenLevel, BlockPos, Boolean> oreCapGenFun = (level, pos) -> Math.random() < ORE_CAP_GEN_PROBABILITY && (level.isEmptyBlock(pos) || level.getBlockState(pos).is(BlockTags.REPLACEABLE_PLANTS));

    public final CrossChunkGenerationHelper helper = new CrossChunkGenerationHelper();

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
          Deposit Shape:
          0:Ellipsoid
          1:Cylinder
        =======================================
         */

        int shape = level.getRandom().nextInt(2);
        Stream<BlockPos> deposit;

        int rx, ry, rz;

        switch (shape) {
            case 0 -> {
                //Ellipsoid
                rx = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1;
                ry = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1;
                rz = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1;

                deposit = getEllipsoidStream(depositCenter, rx, ry, rz)
                        .filter(pos -> isReplaceable(level.getBlockState(pos).getBlock()));

                deposit.forEach(pos -> {
                    setBlock(level, pos, depositRock.defaultBlockState());
                    if (!helper.isInChunk(cp, pos))
                        helper.offer(level, rockGenFun, pos, depositRock.defaultBlockState());
                });
            }
            case 1 -> {
                //Cylinder
                int height = 2 * (level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1);
                rx = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1;
                rz = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1;

                deposit = getCylinderStream(depositCenter, rx, height, rz)
                        .filter(pos -> isReplaceable(level.getBlockState(pos).getBlock()));

                deposit.forEach(pos -> {
                    setBlock(level, pos, depositRock.defaultBlockState());
                    if (!helper.isInChunk(cp, pos)) {
                        helper.offer(level, rockGenFun, pos, depositRock.defaultBlockState());
                    }
                });
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

        if (deposit.findAny().isEmpty())
            return false;

        List<Block> depositOreBlocks = new ArrayList<>();

        int oreBodyCount = level.getRandom().nextInt(MAX_ORE_BODY_COUNT - MIN_ORE_BODY_COUNT + 1) + MIN_ORE_BODY_COUNT;

        // gen multiple ore bodies
        for (int times = 0; times < oreBodyCount; times++) {
            Ore bodyOre = depositOre;

            // check if generate body as paragenesis
            if (depositOre.paragenesis() != null) {
                if (depositOre.chanceAsParagenesis() != 0) {
                    int index = level.getRandom().nextInt(depositOre.paragenesis().length);
                    if (Math.random() < depositOre.paragenesis()[index].chanceAsParagenesis()) {
                        bodyOre = depositOre.paragenesis()[index];
                    }
                }
            }

            // add ore blocks of different level to deposit ore block list
            getAllGradeOre(bodyOre, depositRock).forEach(oreBlock -> {
                if (!depositOreBlocks.contains(oreBlock))
                    depositOreBlocks.add(oreBlock);
            });

            BlockPos oreBodyCenter = deposit.findAny().get();

            int rxb = level.getRandom().nextInt(MAX_ORE_BODY_SIZE - MIN_ORE_BODY_SIZE) + MIN_ORE_BODY_SIZE + 1;
            int ryb = level.getRandom().nextInt(MAX_ORE_BODY_SIZE - MIN_ORE_BODY_SIZE) + MIN_ORE_BODY_SIZE + 1;
            int rzb = level.getRandom().nextInt(MAX_ORE_BODY_SIZE - MIN_ORE_BODY_SIZE) + MIN_ORE_BODY_SIZE + 1;
            double rxb2 = Math.pow(rxb, 2);
            double ryb2 = Math.pow(ryb, 2);
            double rzb2 = Math.pow(rzb, 2);

            double mNormal = level.getRandom().nextDouble() * MAX_NORMAL_MULTIPLIER;
            double mRich = level.getRandom().nextDouble() * mNormal * MAX_RICH_MULTIPLIER;

            double mNormalRightValue = Math.pow(mNormal, 2);
            double mRichRightValue = Math.pow(mRich, 2);

            List<BlockPos>
                    richPos = new ArrayList<>(),
                    normalPos = new ArrayList<>(),
                    poorPos = new ArrayList<>();

            // Ore Generation
            BlockPos.betweenClosedStream(oreBodyCenter, oreBodyCenter.offset(rxb, ryb, rzb)).parallel().forEach(pos -> {
                double leftValue = Math.pow(pos.getX() - oreBodyCenter.getX(), 2) / rxb2 + Math.pow(pos.getY() - oreBodyCenter.getY(), 2) / ryb2 + Math.pow(pos.getZ() - oreBodyCenter.getZ(), 2) / rzb2;
                // add one quadrant of rich ore generation pos to list
                if (leftValue <= mRichRightValue) {
                    richPos.add(pos);
                    return;
                }
                // add one quadrant of normal ore generation pos to list
                if (leftValue <= mNormalRightValue) {
                    normalPos.add(pos);
                    return;
                }
                // add one quadrant of poor ore generation pos to list
                if (leftValue <= 1.0d) {
                    poorPos.add(pos);
                }
            });


            final Ore finalBodyOre = bodyOre;

            // generate rich ores
            richPos.stream().parallel().flatMap(pos -> getSymmetricPosStream(pos, oreBodyCenter)).forEach(pos -> {
                if (!depositOreBlocks.contains(level.getBlockState(pos).getBlock()))
                    setBlock(level, pos, getOreBlock(finalBodyOre, depositRock, OreGrade.RICH).defaultBlockState());
                if (!helper.isInChunk(cp, pos))
                    helper.offer(level, oreGenFun, pos, getOreBlock(finalBodyOre, depositRock, OreGrade.RICH).defaultBlockState());
            });
            // generate normal ores
            normalPos.stream().parallel().flatMap(pos -> getSymmetricPosStream(pos, oreBodyCenter)).forEach(pos -> {
                if (!depositOreBlocks.contains(level.getBlockState(pos).getBlock()))
                    setBlock(level, pos, getOreBlock(finalBodyOre, depositRock, OreGrade.NORMAL).defaultBlockState());
                if (!helper.isInChunk(cp, pos))
                    helper.offer(level, oreGenFun, pos, getOreBlock(finalBodyOre, depositRock, OreGrade.NORMAL).defaultBlockState());
            });
            // generate poor ores
            poorPos.stream().parallel().flatMap(pos -> getSymmetricPosStream(pos, oreBodyCenter)).forEach(pos -> {
                if (!depositOreBlocks.contains(level.getBlockState(pos).getBlock()))
                    setBlock(level, pos, getOreBlock(finalBodyOre, depositRock, OreGrade.POOR).defaultBlockState());
                if (!helper.isInChunk(cp, pos))
                    helper.offer(level, oreGenFun, pos, getOreBlock(finalBodyOre, depositRock, OreGrade.POOR).defaultBlockState());
            });
        }

        /*
        ======================
          Surface Decoration
        ======================
         */

        //TODO: fix surface generation

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
            if (plantDecayFun.apply(level, pos))
                level.removeBlock(pos, true);

            if (!helper.isInChunk(cp, pos))
                helper.offer(level, plantDecayFun, pos, Blocks.AIR.defaultBlockState());

            // Plant Sign Generation
            if (depositOre.plantSign() != null) {
                if (plantSignGenFun.apply(level, pos))
                    level.setBlock(pos, depositOre.plantSign().defaultBlockState(), 0);

                if (!helper.isInChunk(cp, pos))
                    helper.offer(level, plantSignGenFun, pos, depositOre.plantSign().defaultBlockState());
            }

            // Ore Cap Generation
            if (depositOre.oreCap() != null) {
                if (oreCapGenFun.apply(level, pos))
                    level.setBlock(pos, depositOre.oreCap().defaultBlockState(), 0);

                if (!helper.isInChunk(cp, pos))
                    helper.offer(level, oreCapGenFun, pos, depositOre.oreCap().defaultBlockState());
            }
        });

        return true;
    }
}
