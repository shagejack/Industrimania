package shagejack.industrimania.content.primalAge.block.hotGravel;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class HotGravelBlock extends GravelBlock {

    public HotGravelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {

        if (random.nextDouble() < 0.5)
            level.setBlock(pos, Blocks.GRAVEL.defaultBlockState(), 3);

        super.tick(state, level, pos, random);
    }
}
