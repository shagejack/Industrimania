package shagejack.industrimania.content.pollution.record;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.Objects;

public record ChunkReference(DimensionType dimension, ChunkPos pos) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkReference that = (ChunkReference) o;
        return Objects.equals(dimension, that.dimension) && Objects.equals(pos, that.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimension, pos);
    }
}
