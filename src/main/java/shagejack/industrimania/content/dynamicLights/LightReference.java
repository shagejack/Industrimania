package shagejack.industrimania.content.dynamicLights;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

public record LightReference(BlockPos pos, int lightLevel, Entity entity) {
}
