package shagejack.industrimania.registers;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
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
import shagejack.industrimania.content.worldGen.feature.OreGenFeature;
import shagejack.industrimania.content.worldGen.feature.OreRemovalFeature;
import shagejack.industrimania.content.worldGen.feature.RockLayersFeature;
import shagejack.industrimania.content.worldGen.feature.SandStoneReplaceFeature;
import shagejack.industrimania.registers.block.AllBlocks;

import java.util.Objects;

import static shagejack.industrimania.registers.RegisterHandle.FEATURE_REGISTER;

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
    private static final SandStoneReplaceFeature SAND_STONE_REPLACE_FEATURE = new FeatureBuilder<>(
            new SandStoneReplaceFeature(NoneFeatureConfiguration.CODEC)
    ).name("sandstone_replace").build();

    public static final ConfiguredFeature<?, ?> ROCK_LAYERS_ALL = ROCK_LAYERS_FEATURE
            .configured(NoneFeatureConfiguration.NONE);
    public static final ConfiguredFeature<?, ?> ORE_REMOVAL_ALL = ORE_REMOVAL_FEATURE
            .configured(NoneFeatureConfiguration.NONE);
    public static final ConfiguredFeature<?, ?> ORE_GEN_ALL = ORE_GEN_FEATURE
            .configured(NoneFeatureConfiguration.NONE);
    public static final ConfiguredFeature<?, ?> SAND_STONE_REPLACE_ALL = SAND_STONE_REPLACE_FEATURE
            .configured(NoneFeatureConfiguration.NONE);

    public static final PlacedFeature ROCK_LAYERS_ALL_PLACED = ROCK_LAYERS_ALL.placed(
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(320)));
    public static final PlacedFeature ORE_REMOVAL_ALL_PLACED = ORE_REMOVAL_ALL.placed(
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(320)));
    public static final PlacedFeature ORE_GEN_ALL_PLACED = ORE_GEN_ALL.placed(
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(320)));
    public static final PlacedFeature SAND_STONE_REPLACE_PLACED = SAND_STONE_REPLACE_ALL.placed(
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(320)));

    //Built-in Features
    public static final ConfiguredFeature<TreeConfiguration, ?> RUBBER_TREE = FeatureUtils.register("rubber_tree", Feature.TREE.configured(
            new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(AllBlocks.nature_rubber_tree_log.block().get()),
                    new StraightTrunkPlacer(5, 2, 0),
                    BlockStateProvider.simple(AllBlocks.nature_rubber_tree_leaves.block().get()),
                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                    new TwoLayersFeatureSize(1, 0, 1)
            ).ignoreVines().build()));

    public static final PlacedFeature RUBBER_TREE_PLACED = RUBBER_TREE.placed(
            PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
            InSquarePlacement.spread(),
            PlacementUtils.countExtra(1, 0.05F, 2),
            SurfaceWaterDepthFilter.forMaxDepth(0),
            BiomeFilter.biome(),
            BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(AllBlocks.nature_rubber_tree_sapling.block().get().defaultBlockState(), BlockPos.ZERO))
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
