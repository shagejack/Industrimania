package shagejack.industrimania.content.primalAge.block.stack.hay;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.registers.block.AllBlocks;

import java.util.Random;

public class HayStackBlock extends Block {

    public HayStackBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        super.randomTick(state, level, pos, random);
        if (level.isRainingAt(pos.above()) && random.nextDouble() < 0.2) {
            level.setBlock(pos, AllBlocks.mechanic_moldy_grass_stack.block().get().defaultBlockState(), 2 | 16);
        }
    }
}
