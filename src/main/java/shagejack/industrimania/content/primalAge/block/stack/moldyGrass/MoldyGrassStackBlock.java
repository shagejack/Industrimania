package shagejack.industrimania.content.primalAge.block.stack.moldyGrass;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.content.contraptions.blockBase.BlockDirectionalBase;
import shagejack.industrimania.registers.block.AllBlocks;

import java.util.Random;

public class MoldyGrassStackBlock extends BlockDirectionalBase {

    public MoldyGrassStackBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        super.randomTick(state, level, pos, random);
        if (level.isRainingAt(pos.above()) && random.nextDouble() < 0.3) {
            level.setBlock(pos, AllBlocks.building_rotten_grass_stack.block().get().defaultBlockState(), 2 | 16);
        }
    }
}
