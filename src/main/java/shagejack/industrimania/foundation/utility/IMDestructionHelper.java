package shagejack.industrimania.foundation.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import shagejack.industrimania.registers.block.AllBlocks;
import shagejack.industrimania.registers.block.grouped.AllRocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IMDestructionHelper {

    public static boolean isInEllipsoid(int dx, int dy, int dz, BlockPos center, int rx2, int ry2, int rz2) {
        return Math.pow(dx - center.getX(), 2) / rx2 + Math.pow(dy - center.getY(), 2) / ry2 + Math.pow(dz - center.getZ(), 2) / rz2 <= 1;
    }

    public static boolean isInEllipse(int dx, int dz, BlockPos center, int rx2, int rz2) {
        return Math.pow(dx - center.getX(), 2) / rx2 + Math.pow(dz - center.getZ(), 2) / rz2 <= 1;
    }


    public static void eraseEllipsoid(Level level, BlockPos center, int rx, int ry, int rz) {
        int rx2 = (int) Math.pow(rx, 2);
        int ry2 = (int) Math.pow(ry, 2);
        int rz2 = (int) Math.pow(rz, 2);
        for (int dx = center.getX() - rx; dx < center.getX() + rx; dx++) {
            for (int dy = center.getY() - ry; dy < center.getY() + ry; dy++) {
                for (int dz = center.getZ() - rz; dz < center.getZ() + rz; dz++) {
                    if (isInEllipsoid(dx, dy, dz, center, rx2, ry2, rz2)) {
                        BlockPos blockPos = new BlockPos(dx, dy, dz);
                        level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2 | 16);
                    }
                }
            }
        }
    }

    public static void eraseEllipsoid(Level level, BlockPos center, int rx, int ry, int rz, boolean fire) {
        int rx2 = (int) Math.pow(rx, 2);
        int ry2 = (int) Math.pow(ry, 2);
        int rz2 = (int) Math.pow(rz, 2);
        for (int dx = center.getX() - rx; dx < center.getX() + rx; dx++) {
            for (int dy = center.getY() - ry; dy < center.getY() + ry; dy++) {
                for (int dz = center.getZ() - rz; dz < center.getZ() + rz; dz++) {
                    if (isInEllipsoid(dx, dy, dz, center, rx2, ry2, rz2)) {
                        BlockPos blockPos = new BlockPos(dx, dy, dz);
                        level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2 | 16);
                        if (fire && BaseFireBlock.canBePlacedAt(level, blockPos, Direction.UP)) {
                            level.setBlock(blockPos, Blocks.FIRE.defaultBlockState(), 2 | 16);
                        }
                    }
                }
            }
        }
    }

    public static void recoverNature(Level level, BlockPos center, int rx, int rz) {
        int rx2 = (int) Math.pow(rx, 2);
        int rz2 = (int) Math.pow(rz, 2);
        for (int dx = center.getX() - rx; dx < center.getX() + rx; dx++) {
            for (int dz = center.getZ() - rz; dz < center.getZ() + rz; dz++) {
                if (isInEllipse(dx, dz, center, rx2, rz2)) {
                    BlockPos pos = new BlockPos(dx, level.getMaxBuildHeight(), dz);
                    List<Block> stoneLayers = new ArrayList<>();

                    for (int m = 0; m < 4; m++) {
                        Block rock = AllBlocks.rock_limestone.block().get();
                        if (AllRocks.ROCKS.size() > 0) {
                            rock = AllRocks.ROCKS.get(level.getRandom().nextInt(AllRocks.ROCKS.size())).block().get();
                        }
                        for (int n = 0; n < 4; n++) {
                            stoneLayers.add(rock);
                        }
                    }

                    int i = 0;
                    while(i < 20 && !level.getBlockState(pos).is(Blocks.BEDROCK) && pos.getY() > level.getMinBuildHeight()) {
                        if (!level.getBlockState(pos).isAir()) {
                            if (i == 0) {
                                level.setBlock(pos, Blocks.GRASS_BLOCK.defaultBlockState(), 2 | 16);
                                if (level.getBlockState(pos).getBlock() instanceof GrassBlock) {
                                    ((GrassBlock) level.getBlockState(pos).getBlock()).performBonemeal(Objects.requireNonNull(Objects.requireNonNull(level.getServer()).getLevel(level.dimension())), level.getRandom(), pos, Blocks.GRASS_BLOCK.defaultBlockState());
                                }
                            }
                            if (i > 0 && i < 4) {
                                level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2 | 16);
                            }
                            if (AllRocks.ROCKS.size() > 0) {
                                if (i >= 4) level.setBlock(pos, stoneLayers.get(i - 4).defaultBlockState(), 2 | 16);
                            }
                            i++;
                        }
                        pos = pos.below();
                    }



                }
            }
        }
    }

}
