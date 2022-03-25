package shagejack.industrimania.content.pollution.record;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.dimension.DimensionType;

public record ChunkReference(DimensionType dimension, ChunkPos pos) {
}
