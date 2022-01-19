package shagejack.industrimania.content.pollution.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BlockAshesLayers extends SnowLayerBlock {

    public BlockAshesLayers(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState p_56602_, LevelReader p_56603_, BlockPos p_56604_) {
        BlockState blockstate = p_56603_.getBlockState(p_56604_.below());
        if (!blockstate.is(Blocks.BARRIER)) {
            if (!blockstate.is(Blocks.HONEY_BLOCK) && !blockstate.is(Blocks.SOUL_SAND)) {
                return Block.isFaceFull(blockstate.getCollisionShape(p_56603_, p_56604_.below()), Direction.UP) || blockstate.is(this) && blockstate.getValue(LAYERS) == 8;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

}
