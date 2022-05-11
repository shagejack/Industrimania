package shagejack.industrimania.content.world.gen.record;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;

import java.util.Objects;

public record WorldGenChunkReference(WorldGenLevel level, ChunkPos pos) {

}
