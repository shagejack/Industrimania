package shagejack.industrimania.api.worldGen;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;

public abstract class AbstractWorldGenObject {

    public static String name;
    public static List<Biome.BiomeCategory> biomes;
    public static boolean whitelisted;

    public AbstractWorldGenObject(String name, List<Biome.BiomeCategory> biomes) {
        this.name = name;
        this.biomes = biomes;
        this.whitelisted = true;
    }
    public AbstractWorldGenObject(String name, List<Biome.BiomeCategory> biomes, boolean whitelisted) {
        this.name = name;
        this.biomes = biomes;
        this.whitelisted = whitelisted;
    }


    public static void register() {

    }

    public static void onBiomeLoading(BiomeLoadingEvent event) {

    }

}
