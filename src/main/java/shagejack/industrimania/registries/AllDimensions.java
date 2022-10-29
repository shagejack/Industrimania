package shagejack.industrimania.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import shagejack.industrimania.registries.record.DimensionPair;

public class AllDimensions {

    public static final DimensionPair VETUS = register("vetus");

    private static DimensionPair register(String name) {
        ResourceKey<DimensionType> dimensionType = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(name));
        ResourceKey<Level> dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(name));
        return new DimensionPair(dimension, dimensionType);
    }

}
