package shagejack.industrimania.content.pollution.record;

import net.minecraft.world.level.block.Block;

public record DecayReference(Block block, Boolean dropItem, long minPollution, double probability) {
}
