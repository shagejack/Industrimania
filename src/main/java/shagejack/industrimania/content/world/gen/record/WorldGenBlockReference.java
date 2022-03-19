package shagejack.industrimania.content.world.gen.record;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public record WorldGenBlockReference(BiFunction<WorldGenLevel, BlockPos, Boolean> genFun, BlockPos pos, BlockState state) {
}
