package shagejack.industrimania.content.world.nature.rubberTree;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import shagejack.industrimania.registers.AllFluids;

public class RawRubberFluidBlock extends LiquidBlock {

    public RawRubberFluidBlock(Properties properties) {
        super(() -> (FlowingFluid) AllFluids.rawRubber.flowing().get(), properties);
    }

}
