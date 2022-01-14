package shagejack.industrimania.content.worldGen;

import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.worldGen.feature.OreRemovalFeature;
import shagejack.industrimania.content.worldGen.feature.RockLayersFeature;

public class IndustrimaniaFeatures {
    private static final Feature<NoneFeatureConfiguration> ROCK_LAYERS_FEATURE = new RockLayersFeature(
            NoneFeatureConfiguration.CODEC);
    private static final Feature<NoneFeatureConfiguration> ORE_REMOVAL_FEATURE = new OreRemovalFeature(
            NoneFeatureConfiguration.CODEC);

    public static final ConfiguredFeature<?, ?> ROCK_LAYERS_ALL = ROCK_LAYERS_FEATURE
            .configured(NoneFeatureConfiguration.NONE);
    public static final ConfiguredFeature<?, ?> ORE_REMOVAL_ALL = ORE_REMOVAL_FEATURE
            .configured(NoneFeatureConfiguration.NONE);

    public static PlacedFeature ROCK_LAYERS_ALL_PLACED = ROCK_LAYERS_ALL.placed(
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(320)));
    public static PlacedFeature ORE_REMOVAL_ALL_PLACED =ORE_REMOVAL_ALL.placed(
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(320)));

    public static DeferredRegister<Feature<?>> createRegistry() {
        DeferredRegister<Feature<?>> registry = DeferredRegister.create(ForgeRegistries.FEATURES, Industrimania.MOD_ID);
        registry.register("rock_layers", () -> ROCK_LAYERS_FEATURE);
        registry.register("ore_removal", () -> ORE_REMOVAL_FEATURE);
        return registry;
    }
}
