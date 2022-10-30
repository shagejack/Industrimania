/*
 * https://github.com/TeamMoegMC/FrostedHeart/blob/master/src/main/java/com/teammoeg/frostedheart/climate/TemperatureSimulator.java
 *
 * Mainly modified the logic of TemperatureSimulator#getHeat.
 *
 * Copyright (c) 2022 TeamMoeg
 *
 * This file is part of Frosted Heart.
 *
 * Frosted Heart is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * Frosted Heart is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Frosted Heart. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package shagejack.industrimania.content.climate;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import shagejack.industrimania.foundation.data.BlockTempData;
import shagejack.industrimania.foundation.data.IMDataManager;
import shagejack.industrimania.foundation.utility.Vector3;
import shagejack.industrimania.foundation.utility.VoxelShapeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HeatSimulator {
    public LevelChunkSection[] sections = new LevelChunkSection[8];// sectors(xz): - -/- +/+ -/+ + and y -/+
    public static final int RANGE = 8;

    double environmentTemp;
    BlockPos source;
    BlockPos origin;
    ServerLevel level;
    Random rnd;

    private static final int n = 4168;
    private static final int rdiff = 10;
    private static final float v0 = .4f;
    private static final VoxelShape EMPTY= Shapes.empty();
    private static final VoxelShape FULL= Shapes.block();

    private double[] qx = new double[n], qy = new double[n], qz = new double[n];// Q pos, position of particle.
    private static float[] vx = new float[n], vy = new float[n], vz = new float[n];// Vp, speed vector list, which is considered a distributed ball mesh.
    private int[] vIndex = new int[n];// IDv, particle speed index in speed vector list, which lowers random cost.
    private static final int num_rounds = 20;

    // pre-generated particle normalized speed vector pool
    static {
        int o = 0;
        for (int i = -rdiff; i <= rdiff; ++i) {
            for (int j = -rdiff; j <= rdiff; ++j) {
                for (int k = -rdiff; k <= rdiff; ++k) {
                    if (i == 0 && j == 0 && k == 0)
                        continue; // ignore zero vector
                    float x = i * 1f / rdiff, y = j * 1f / rdiff, z = k * 1f / rdiff;
                    float r = Mth.sqrt(x * x + y * y + z * z);
                    if (r > 1)
                        continue; // ignore vectors out of the unit ball
                    vx[o] = x / r * v0; // normalized x
                    vy[o] = y / r * v0; // normalized y
                    vz[o] = z / r * v0; // normalized z
                    o++;
                }
            }
        }
    }

    public static void init() {
    }

    /**
     * Extract block data into shape and temperature, other data are disposed.
     */
    private static class CachedBlockInfo {
        VoxelShape shape;
        double convectiveTemp = 0.0D;
        double closerTemp = 0.0D;
        double directTemp = 0.0D;
        double range = 1.0D;
        double environmentTempFactor = 0.5D;
        double minTemp = -273.15D;
        double maxTemp = 10000.0D;
        boolean isSource = false;
        boolean sameAsEnvironment = true;

        public CachedBlockInfo(VoxelShape shape, double convectiveTemp, double closerTemp, double directTemp, double range, double environmentTempFactor, double minTemp, double maxTemp, boolean isSource, boolean sameAsEnvironment) {
            super();
            this.shape = shape;
            this.convectiveTemp = convectiveTemp;
            this.closerTemp = closerTemp;
            this.directTemp = directTemp;
            this.range = range;
            this.environmentTempFactor = environmentTempFactor;
            this.minTemp = minTemp;
            this.maxTemp = maxTemp;
            this.isSource = isSource;
            this.sameAsEnvironment = sameAsEnvironment;
        }

        public CachedBlockInfo(VoxelShape shape) {
            super();
            this.shape = shape;
        }
    }

    public Map<BlockState, CachedBlockInfo> info = new HashMap<>();// state to info cache
    public Map<BlockPos, CachedBlockInfo> posInfo = new HashMap<>();// position to info cache

    public HeatSimulator(ServerLevel level, BlockPos source, double environmentTemp) {
        this.level = level;
        this.source = source;
        this.environmentTemp = environmentTemp;
        this.rnd = new Random(source.asLong());;

        // these are block position offset
        int offsetN = source.getZ() - RANGE;
        int offsetW = source.getX() - RANGE;
        int offsetD = source.getY() - RANGE;

        // these are chunk position offset
        int chunkOffsetW = offsetW >> 4;
        int chunkOffsetN = offsetN >> 4;
        int chunkOffsetD = offsetD >> 4;

        // get origin point(center of 8 sections)
        origin = new BlockPos((chunkOffsetW + 1) << 4, (chunkOffsetD + 1) << 4, (chunkOffsetN + 1) << 4);

        // fetch all sections to lower calculation cost
        int i = 0;
        for (int x = chunkOffsetW; x <= chunkOffsetW + 1; x++) {
            for (int z = chunkOffsetN; z <= chunkOffsetN + 1; z++) {
                LevelChunkSection[] css = this.level.getChunk(x, z).getSections();
                for (LevelChunkSection chunkSection : css) {
                    if (chunkSection == null)
                        continue;
                    int ynum = chunkSection.bottomBlockY() >> 4;
                    if (ynum == chunkOffsetD) {
                        sections[i] = chunkSection;
                    }
                    if (ynum == chunkOffsetD + 1) {
                        sections[i + 1] = chunkSection;
                    }
                }
                i += 2;
            }
        }
    }

    public BlockState getBlock(int x, int y, int z) {
        int i = 0;

        if (x >= 0) {
            i += 4;
        } else {
            x += 16;
        }
        if (z >= 0) {
            i += 2;
        } else {
            z += 16;
        }
        if (y >= 0) {
            i += 1;
        } else {
            y += 16;
        }
        if (x >= 16 || y >= 16 || z >= 16 || x < 0 || y < 0 || z < 0) {// out of bounds
            return Blocks.AIR.defaultBlockState();
        }
        LevelChunkSection current = sections[i];
        if (current == null)
            return Blocks.AIR.defaultBlockState();
        try {
            return current.getBlockState(x, y, z);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to get block at" + x + "," + y + "," + z);
        }
    }

    /***
     * Since a position is highly possible to be fetched for multiple times, add
     * cache in normal fetch
     */
    private CachedBlockInfo getInfoCached(BlockPos pos) {
        return posInfo.computeIfAbsent(pos, this::getInfo);
    }

    /***
     * fetch without position cache, but with blockstate cache, blocks with the same
     * state should have same collider and heat.
     */
    private CachedBlockInfo getInfo(BlockPos pos) {
        BlockPos ofregion = pos.subtract(origin);
        BlockState blockState = getBlock(ofregion.getX(), ofregion.getY(), ofregion.getZ());
        return info.computeIfAbsent(blockState, s -> getInfo(pos, s));
    }

    /**
     * Just fetch block temperature and collision without cache.
     * Position is only for getCollisionShape method, to avoid some TE based shape.
     */
    private CachedBlockInfo getInfo(BlockPos pos, BlockState blockState) {
        BlockTempData blockTempData = IMDataManager.getBlockData(blockState.getBlock());

        if (blockTempData == null)
            return new CachedBlockInfo(blockState.getCollisionShape(level, pos));

        if (blockTempData.sameAsEnvironment()) {
            return new CachedBlockInfo(blockState.getCollisionShape(level, pos));
        }

        if (blockTempData.mustLit() && (!blockState.hasProperty(BlockStateProperties.LIT) || !blockState.getValue(BlockStateProperties.LIT))) {
            return new CachedBlockInfo(blockState.getCollisionShape(level, pos));
        }

        double blockConvectiveTempCache = blockTempData.getConvectiveTemp();
        double blockCloserTempCache = blockTempData.getCloserTemp();
        double blockDirectTempCache = blockTempData.getDirectTemp();

        if (blockTempData.hasLevel()) {
            double factor = 1.0D;

            if (blockState.hasProperty(BlockStateProperties.LEVEL)) {
                factor = (blockState.getValue(BlockStateProperties.LEVEL) + 1) / 16.0;
            } else if (blockState.hasProperty(BlockStateProperties.LAYERS)) {
                factor = (blockState.getValue(BlockStateProperties.LAYERS) + 1) / 9.0;
            } else if (blockState.hasProperty(BlockStateProperties.LEVEL_FLOWING)) {
                factor = (blockState.getValue(BlockStateProperties.LEVEL_FLOWING)) / 8.0;
            } else if (blockState.hasProperty(BlockStateProperties.LEVEL_CAULDRON)) {
                factor = (blockState.getValue(BlockStateProperties.LEVEL_CAULDRON) + 1) / 4.0;
            }

            blockConvectiveTempCache *= factor;
            blockCloserTempCache *= factor;
            blockDirectTempCache *= factor;
        }

        return new CachedBlockInfo(blockState.getCollisionShape(level, pos), blockConvectiveTempCache, blockCloserTempCache, blockDirectTempCache, blockTempData.getRange(), blockTempData.getEnvironmentTempFactor(), blockTempData.getMinTemp(), blockTempData.getMaxTemp(), blockTempData.isSource(), false);
    }

    /**
     * Check if this location collides with block.
     */
    private boolean isBlockade(double x, double y, double z) {
        CachedBlockInfo info = getInfoCached(new BlockPos(x, y, z));
        if (info.shape == FULL)
            return true;
        if (info.shape == EMPTY)
            return false;

        return VoxelShapeUtils.contains(info.shape, Mth.frac(x), Mth.frac(y), Mth.frac(z));
    }

    /**
     * Get location heat up
     */
    private double getHeat(double currentTemp, double affectedX, double affectedY, double affectedZ, double x, double y, double z) {
        CachedBlockInfo cachedBlockInfo = getInfoCached(new BlockPos(x, y, z));

        if (cachedBlockInfo.sameAsEnvironment) // no convection
            return 0.0D;

        double closerRange = cachedBlockInfo.range;
        double dx = x - affectedX;
        double dy = y - affectedY;
        double dz = z - affectedZ;
        double dSqr = dx * dx + dy * dy + dz * dz;
        double heatIncrease = 0.0D;

        double actualDirectTemp = cachedBlockInfo.directTemp;

        if (!cachedBlockInfo.isSource) {
            actualDirectTemp += cachedBlockInfo.environmentTempFactor * (environmentTemp - actualDirectTemp);
            actualDirectTemp = Math.max(cachedBlockInfo.minTemp, actualDirectTemp);
            actualDirectTemp = Math.min(cachedBlockInfo.maxTemp, actualDirectTemp);
        }

        if ((actualDirectTemp > environmentTemp && currentTemp < actualDirectTemp) || (actualDirectTemp < environmentTemp && currentTemp > actualDirectTemp)) {
            heatIncrease += cachedBlockInfo.convectiveTemp + cachedBlockInfo.closerTemp * Math.max(0.0D, 1.0D - dSqr / (closerRange * closerRange));
        }

        if (VoxelShapeUtils.contains(cachedBlockInfo.shape, affectedX, affectedY, affectedZ) || dSqr < 0.25) {
            heatIncrease += 0.8 * (actualDirectTemp - (currentTemp + heatIncrease));
        }

        return heatIncrease;
    }

    public double getBlockTemperature(Vector3 pos) {
        return getBlockTemperature(pos.x(), pos.y(), pos.z());
    }

    public double getBlockTemperature(double qx0, double qy0, double qz0) {
        for (int i = 0; i < n; ++i) // initialize particles at the position and speed vector indexes
        {
            qx[i] = qx0;
            qy[i] = qy0;
            qz[i] = qz0;
            vIndex[i] = i;
        }
        float heat = 0;
        for (int round = 0; round < num_rounds; ++round) // time-to-live for each particle
        {
            for (int i = 0; i < n; ++i) // for all particles:
            {
                if (isBlockade(qx[i] + vx[vIndex[i]], qy[i] + vy[vIndex[i]], qz[i] + vz[vIndex[i]])) // if running into a block
                {
                    vIndex[i] = rnd.nextInt(n); // re-choose a speed direction randomly (from the pre-generated pool)
                }

                qx[i] += vx[vIndex[i]]; // move x
                qy[i] += vy[vIndex[i]]; // move y
                qz[i] += vz[vIndex[i]]; // move z
                heat += getHeat(environmentTemp + heat / n, qx0, qy0, qz0, qx[i], qy[i], qz[i]) * Mth.lerp(Mth.clamp(vy[vIndex[i]], 0, 0.4) * 2.5, 1, 0.5); // add heat
            }
        }
        return environmentTemp + heat / n;
    }

}
