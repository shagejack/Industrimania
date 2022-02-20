package shagejack.industrimania.content.primalAge.item.handOilLamp;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import shagejack.industrimania.content.dynamicLights.DynamicLights;

import java.util.Random;

public class FakeAirLightBlock extends LightBlock {

    public FakeAirLightBlock(Properties properties) {
        super(properties);
    }

    public RenderShape getRenderShape(BlockState p_52986_) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState p_48760_, BlockGetter p_48761_, BlockPos p_48762_, CollisionContext p_48763_) {
        return Shapes.empty();
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (!DynamicLights.isLit(level, pos)) {
            level.removeBlock(pos, false);
        } else {
            if (random.nextBoolean())
                DynamicLights.removeLight(level, pos);
        }
    }

}
