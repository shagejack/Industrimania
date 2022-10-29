package shagejack.industrimania.registries;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;
import shagejack.industrimania.content.world.gen.feature.*;
import shagejack.industrimania.content.world.gen.featureConfiguration.OreGenConfiguration;
import shagejack.industrimania.registries.block.AllBlocks;

import static shagejack.industrimania.registries.RegisterHandle.FEATURE_REGISTER;

public class AllFeatures {

    private static final RockLayersFeature ROCK_LAYERS_FEATURE = new FeatureBuilder<>(
            new RockLayersFeature(NoneFeatureConfiguration.CODEC)
    ).name("rock_layers").build();
    private static final OreRemovalFeature ORE_REMOVAL_FEATURE = new FeatureBuilder<>(
            new OreRemovalFeature(NoneFeatureConfiguration.CODEC)
    ).name("ore_removal").build();
    private static final OreGenFeature ORE_GEN_FEATURE = new FeatureBuilder<>(
            new OreGenFeature(NoneFeatureConfiguration.CODEC)
    ).name("ore_gen").build();
    private static final SandStoneReplaceFeature SAND_STONE_REPLACEMENT_FEATURE = new FeatureBuilder<>(
            new SandStoneReplaceFeature(NoneFeatureConfiguration.CODEC)
    ).name("sand_stone_replacement").build();
    private static final CobbleGenFeature COBBLE_GEN_FEATURE = new FeatureBuilder<>(
            new CobbleGenFeature(NoneFeatureConfiguration.CODEC)
    ).name("cobble_gen").build();

    public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> ROCK_LAYERS_CONFIGURED = FeatureUtils.register("rock_layers", ROCK_LAYERS_FEATURE);
    public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> ORE_REMOVAL_CONFIGURED = FeatureUtils.register("ore_removal", ORE_REMOVAL_FEATURE);

    public static final Holder<ConfiguredFeature<OreGenConfiguration, ?>> ORE_GEN_CONFIGURED = FeatureUtils.register("ore_gen", ORE_GEN_FEATURE,
            new OreGenConfiguration.OreGenConfigurationBuilder().build()
    );

    public static final Holder<ConfiguredFeature<OreGenConfiguration, ?>> ORE_GEN_FORCED = FeatureUtils.register("ore_gen_forced", ORE_GEN_FEATURE,
            new OreGenConfiguration.OreGenConfigurationBuilder().forceGen().build()
    );

    public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> SAND_STONE_REPLACEMENT_CONFIGURED = FeatureUtils.register("sand_stone_replacement", SAND_STONE_REPLACEMENT_FEATURE);
    public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> COBBLE_GEN_FEATURE_CONFIGURED = FeatureUtils.register("cobble_gen", COBBLE_GEN_FEATURE);

    public static final Holder<PlacedFeature> ROCK_LAYERS_PLACED = PlacementUtils.register(
            "rock_layers",
            ROCK_LAYERS_CONFIGURED,
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(320))
    );
    public static final Holder<PlacedFeature> ORE_REMOVAL_PLACED = PlacementUtils.register(
            "ore_removal",
            ORE_REMOVAL_CONFIGURED,
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(320))
    );
    public static final Holder<PlacedFeature> ORE_GEN_PLACED = PlacementUtils.register(
            "ore_gen",
            ORE_GEN_CONFIGURED,
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(320))
    );
    public static final Holder<PlacedFeature> ORE_GEN_FORCED_PLACED = PlacementUtils.register(
            "ore_gen_forced",
            ORE_GEN_FORCED,
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(320))
    );
    public static final Holder<PlacedFeature> SAND_STONE_REPLACEMENT_PLACED = PlacementUtils.register(
            "sand_stone_replacement",
            SAND_STONE_REPLACEMENT_CONFIGURED,
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(320))
    );
    public static final Holder<PlacedFeature> COBBLE_GEN_FEATURE_PLACED = PlacementUtils.register(
            "cobble_gen",
            COBBLE_GEN_FEATURE_CONFIGURED,
            PlacementUtils.countExtra(5, 0.5F, 10),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
            BiomeFilter.biome(),
            BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluid(Fluids.WATER, BlockPos.ZERO)),
            PlacementUtils.filteredByBlockSurvival(AllBlocks.nature_cobble.block().get())
    );

    // Built-in Features
    public static final Holder<ConfiguredFeature<TreeConfiguration, ?>> RUBBER_TREE = FeatureUtils.register("rubber_tree", Feature.TREE,
            new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(AllBlocks.nature_rubber_tree_log.block().get()),
                    new StraightTrunkPlacer(5, 2, 0),
                    BlockStateProvider.simple(AllBlocks.nature_rubber_tree_leaves.block().get()),
                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                    new TwoLayersFeatureSize(1, 0, 1)
            ).ignoreVines().build());

    public static final Holder<ConfiguredFeature<TreeConfiguration, ?>> MULBERRY_TREE = FeatureUtils.register("mulberry_tree", Feature.TREE,
            new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(AllBlocks.nature_mulberry_tree_log.block().get()),
                    new StraightTrunkPlacer(4, 2, 0),
                    BlockStateProvider.simple(AllBlocks.nature_mulberry_tree_leaves.block().get()),
                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                    new TwoLayersFeatureSize(1, 0, 1)
            ).ignoreVines().build());

    // Built-in Features (Placed)
    public static final Holder<PlacedFeature> RUBBER_TREE_PLACED = PlacementUtils.register("rubber_tree", RUBBER_TREE,
            PlacementUtils.countExtra(0, 0.1F, 1),
            InSquarePlacement.spread(),
            SurfaceWaterDepthFilter.forMaxDepth(0),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            BiomeFilter.biome(),
            PlacementUtils.filteredByBlockSurvival(AllBlocks.nature_rubber_tree_sapling.block().get())
    );

    public static final Holder<PlacedFeature> MULBERRY_TREE_PLACED = PlacementUtils.register("mulberry_tree", MULBERRY_TREE,
            PlacementUtils.countExtra(0, 0.1F, 1),
            InSquarePlacement.spread(),
            SurfaceWaterDepthFilter.forMaxDepth(0),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            BiomeFilter.biome(),
            BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(AllBlocks.nature_mulberry_tree_sapling.block().get().defaultBlockState(), BlockPos.ZERO))
    );

    protected static class FeatureBuilder<T extends Feature<?>> {
        private String name;
        public T feature;

        FeatureBuilder<T> name(String name) {
            this.name = name;
            return this;
        }

        public FeatureBuilder(T feature) {
            this.feature = feature;
        }

        T build() {
            FEATURE_REGISTER.register(name, () -> feature);
            return feature;
        }
    }

    private static class ConfiguredBuilder<T extends ConfiguredFeature<FeatureConfiguration, ?>>{

    }
}
