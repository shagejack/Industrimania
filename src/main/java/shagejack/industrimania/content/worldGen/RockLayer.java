package shagejack.industrimania.content.worldGen;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class RockLayer {

    public static Block rock;
    public static int minY, maxY;
    public static int thickness;
    public static List<Biome.BiomeCategory> biomes;
    public static boolean whitelisted;

    public RockLayer(Block rock) {
        this(rock, -64);
    }

    public RockLayer(Block rock, int minY) {
        this(rock, minY, 320);
    }

    public RockLayer(Block rock, int minY, int maxY) {
        this(rock, minY, maxY, 8);
    }

    public RockLayer(Block rock, int minY, int maxY, int thickness) {
        this(rock, minY, maxY, thickness, null);
    }

    public RockLayer(Block rock, int minY, int maxY, int thickness, List<Biome.BiomeCategory> biomes) {
        this(rock, minY, maxY, thickness, biomes, true);
    }

    public RockLayer(Block rock, int minY, int maxY, int thickness, List<Biome.BiomeCategory> biomes, boolean whitelisted) {
        this.rock = rock;
        this.minY = minY;
        this.maxY = maxY;
        this.thickness = thickness;
        this.biomes = biomes;
        this.whitelisted = whitelisted;
    }

    public Block getRock() {
        return this.rock;
    }

    public int getMinY() {
        return this.minY;
    }

    public int getMaxY() {
        return this.maxY;
    }

    public int getThickness() {
        return this.thickness;
    }

    public List<Biome.BiomeCategory> getBiomes() {
        return this.biomes;
    }

    public boolean isWhitelisted() {
        return whitelisted;
    }


}
