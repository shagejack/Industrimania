package shagejack.industrimania.content.worldGen.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.registries.ForgeRegistries;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.worldGen.OreRegistry;
import shagejack.industrimania.content.worldGen.record.Ore;
import shagejack.industrimania.content.worldGen.RockRegistry;
import shagejack.industrimania.registers.AllBlocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OreGenFeature extends Feature<NoneFeatureConfiguration> {

    //TODO: FIX CROSS-CHUNK GENERATION ERROR

    private final double DEPOSIT_GEN_PROBABILITY = 0.05;
    private final double PLANT_DECAY_PROBABILITY = 0.75;
    private final double PLANT_SIGN_GEN_PROBABILITY = 0.15;
    private final double ORE_CAP_GEN_PROBABILITY = 0.01;
    private final int MAX_DEPOSIT_SIZE = 16;
    private final int MIN_DEPOSIT_SIZE = 4;
    private final int MAX_ORE_BODY_COUNT = 16;
    private final int MIN_ORE_BODY_COUNT = 4;
    private final int MAX_ORE_BODY_SIZE = 8;
    private final int MIN_ORE_BODY_SIZE = 2;
    private final double MAX_NORMAL_MULTIPLIER = 0.9;
    private final double MAX_RICH_MULTIPLIER = 0.5;

    private final ArrayList<Block> EXTRA_REPLACEABLE_BLOCK = Lists.newArrayList(
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

    private final ArrayList<Block> PLANT_TO_DECAY = Lists.newArrayList(
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
    public final List<Ore> ORES = Lists.newArrayList(
            OreRegistry.hematite,
            OreRegistry.galena
    );

    public OreGenFeature(Codec p_65786_) {
        super(p_65786_);
    }

    /*
    Ore Grade:
    0: Poor
    1: Normal
    2: Rich
     */
    public Block getOreBlock(Ore ore, Block rock, int grade) {
        String rockName = rock.getRegistryName().toString().split(":")[1];
        ResourceLocation oreBlock = new ResourceLocation(Industrimania.MOD_ID, rockName + "_" + ore.oreType().name() + "_" + grade);
        return ForgeRegistries.BLOCKS.getValue(oreBlock);
    }

    public boolean isReplaceable(Block block) {
        if (RockRegistry.igneousStones.contains(block) || RockRegistry.metamorphicStones.contains(block) || RockRegistry.sedimentaryStones.contains(block)) {
            return true;
        }

        if (block == AllBlocks.rock_sandstone.block().get()) {
            return true;
        }

        if (EXTRA_REPLACEABLE_BLOCK.contains(block)) {
            return true;
        }

        return false;
    }

    public boolean isInEllipsoid(int dx, int dy, int dz, BlockPos center, int rx2, int ry2, int rz2) {
        if (Math.pow(dx - center.getX(), 2) / rx2 + Math.pow(dy - center.getY(), 2) / ry2 + Math.pow(dz - center.getZ(), 2) / rz2 <= 1) {
            return true;
        }
        return false;
    }

    public boolean isInCylinder(int dx, int dy, int dz, BlockPos center, int rx2, int height, int rz2) {
        if (Math.pow(dx - center.getX(), 2) / rx2 + Math.pow(dz - center.getZ(), 2) / rz2 <= 1 && dy > center.getY() - height / 2 && dy < center.getY() + height / 2) {
            return true;
        }
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> f) {
        if (f.chunkGenerator() instanceof FlatLevelSource) {
            return false;
        }

        if (Math.random() < DEPOSIT_GEN_PROBABILITY) {
            //Deposit Gen
            WorldGenLevel level = f.level();
            ChunkPos cp = new ChunkPos(f.origin());
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

            for(Ore ore : ORES) {
                if(depositCenter.getY() > ore.minY() && depositCenter.getY() < ore.maxY()) {
                    if (ore.availableRock() != null && ore.availableRock().contains(depositRock)) {
                        oresAvailable.add(ore);
                    }
                }
            }

            if(oresAvailable.isEmpty()) {
                return false;
            }

            Ore depositOre;

            try {
                depositOre = oresAvailable.get(level.getRandom().nextInt(oresAvailable.size()));
            } catch (IllegalArgumentException e) {
                throw(e);
            }

            /*
            Define Deposit Shape & Blocks
            Deposit Shape:
            0:Ellipsoid
            1:Cylinder
             */

            int shape = level.getRandom().nextInt(2);
            List<BlockPos> deposit = new ArrayList<>();

            int rx, ry, rz, rx2, ry2, rz2;

            switch(shape) {
                case 0:
                    //Ellipsoid
                    rx = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1;
                    ry = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1;
                    rz = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1;
                    rx2 = (int) Math.pow(rx, 2);
                    ry2 = (int) Math.pow(ry, 2);
                    rz2 = (int) Math.pow(rz, 2);

                    for(int dx = depositCenter.getX() - rx; dx < depositCenter.getX() + rx; dx++) {
                        for(int dy = depositCenter.getY() - ry; dy < depositCenter.getY() + ry; dy++) {
                            for(int dz = depositCenter.getZ() - rz; dz < depositCenter.getZ() + rz; dz++) {
                                if (isInEllipsoid(dx, dy, dz, depositCenter, rx2, ry2, rz2)) {
                                    BlockPos blockPos = new BlockPos(dx, dy, dz);
                                    Block block = level.getBlockState(blockPos).getBlock();
                                    if (isReplaceable(block)) {
                                        setBlock(level, blockPos, depositRock.defaultBlockState());
                                        deposit.add(blockPos);
                                    }
                                }
                            }
                        }
                    }

                    break;
                case 1:
                    //Cylinder
                    int height = 2 * (level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1);
                    rx = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1;
                    rz = level.getRandom().nextInt(MAX_DEPOSIT_SIZE - MIN_DEPOSIT_SIZE) + MIN_DEPOSIT_SIZE + 1;
                    rx2 = (int) Math.pow(rx, 2);
                    rz2 = (int) Math.pow(rz, 2);

                    for(int dx = depositCenter.getX() - rx; dx < depositCenter.getX() + rx; dx++) {
                        for(int dy = depositCenter.getY() - height / 2; dy < depositCenter.getY() + height / 2; dy++) {
                            for(int dz = depositCenter.getZ() - rz; dz < depositCenter.getZ() + rz; dz++) {
                                if (isInCylinder(dx, dy, dz, depositCenter, rx2, height, rz2)) {
                                    BlockPos blockPos = new BlockPos(dx, dy, dz);
                                    Block block = level.getBlockState(blockPos).getBlock();
                                    if (isReplaceable(block)) {
                                        setBlock(level, blockPos, depositRock.defaultBlockState());
                                        deposit.add(blockPos);
                                    }
                                }
                            }
                        }
                    }

                    break;
                default:
                    Industrimania.LOGGER.warn("Deposit generated wrongly. THIS SHOULDN'T HAPPEN!");
                    return false;
            }

            /*
            Generating Ore Bodies
             */

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

                for(int j = 0; j <= 2; j++) {
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
                for(int dx = oreBodyCenter.getX() - rxb; dx < oreBodyCenter.getX() + rxb; dx++) {
                    for(int dy = oreBodyCenter.getY() - ryb; dy < oreBodyCenter.getY() + ryb; dy++) {
                        for(int dz = oreBodyCenter.getZ() - rzb; dz < oreBodyCenter.getZ() + rzb; dz++) {
                            //Rich Ore Generation
                            if (isInEllipsoid(dx, dy, dz, oreBodyCenter, (int) (rxb2 * mRich2), (int) (ryb2 * mRich2), (int) (rzb2 * mRich2))) {
                                BlockPos pos = new BlockPos(dx, dy, dz);
                                if (!depositOreBlocks.contains(level.getBlockState(pos).getBlock())) {
                                    setBlock(level, pos, getOreBlock(bodyOre, depositRock, 2).defaultBlockState());

                                    continue;
                                }
                            }
                            //Normal Ore Generation
                            if (isInEllipsoid(dx, dy, dz, oreBodyCenter, (int) (rxb2 * mNormal2), (int) (ryb2 * mNormal2), (int) (rzb2 * mNormal2))) {
                                BlockPos pos = new BlockPos(dx, dy, dz);
                                if (!depositOreBlocks.contains(level.getBlockState(pos).getBlock())) {
                                    setBlock(level, pos, getOreBlock(bodyOre, depositRock, 1).defaultBlockState());
                                    continue;
                                }
                            }
                            //Rich Ore Generation
                            if (isInEllipsoid(dx, dy, dz, oreBodyCenter, rxb2, ryb2, rzb2)) {
                                BlockPos pos = new BlockPos(dx, dy, dz);
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

                List<BlockPos> surface = new ArrayList<>();

                for(int dx = depositCenter.getX() - rx; dx < depositCenter.getX() + rx; dx++) {
                    for(int dz = depositCenter.getZ() - rz; dz < depositCenter.getZ() + rz; dz++) {
                        if (Math.pow(dx, 2) / rx2 + Math.pow(dz, 2) / rz2 <= 1) {
                            surface.add(new BlockPos(dx, chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, dx, dz), dz));
                        }
                    }
                }

                //Surface Plant Modification
                for(BlockPos pos : surface) {
                    //Plant Decay
                    if (PLANT_TO_DECAY.contains(level.getBlockState(pos).getBlock())) {
                        if (Math.random() < PLANT_DECAY_PROBABILITY) {
                            setBlock(level, pos, Blocks.AIR.defaultBlockState());
                        }
                    }

                    //Plant Sign Generation
                    if (depositOre.plantSign() != null) {
                        if (Math.random() < PLANT_SIGN_GEN_PROBABILITY) {
                            setBlock(level, pos, depositOre.plantSign().defaultBlockState());
                        }
                    }

                    //Ore Cap Generation
                    if (depositOre.oreCap() != null) {
                        if (Math.random() < ORE_CAP_GEN_PROBABILITY) {
                            setBlock(level, pos, depositOre.oreCap().defaultBlockState());
                        }
                    }
                }

            }
            return true;
        }

        return false;
    }


}
