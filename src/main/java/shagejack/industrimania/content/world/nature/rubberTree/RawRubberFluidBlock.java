package shagejack.industrimania.content.world.nature.rubberTree;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;

public class RawRubberFluidBlock extends LiquidBlock {

    public RawRubberFluidBlock(java.util.function.Supplier<? extends FlowingFluid> fluid, BlockBehaviour.Properties properties) {
        super(fluid, properties);
    }

}
