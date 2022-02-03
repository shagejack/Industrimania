package shagejack.industrimania.registers.record;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record DimensionPair(ResourceKey<Level> dimension, ResourceKey<DimensionType> dimensionType) {
    public DimensionPair dimension(Consumer<ResourceKey<Level>> consumer) {
        consumer.accept(dimension);
        return this;
    }

    public DimensionPair dimensionType(Consumer<ResourceKey<DimensionType>> consumer) {
        consumer.accept(dimensionType);
        return this;
    }

    public DimensionPair use(BiConsumer<ResourceKey<Level>, ResourceKey<DimensionType>> consumer) {
        consumer.accept(dimension, dimensionType);
        return this;
    }

}