package shagejack.industrimania.content.worldGen;

import net.minecraft.world.level.block.Block;

public record RockLayer(Block rock, int minY, int maxY, int thickness) { }
