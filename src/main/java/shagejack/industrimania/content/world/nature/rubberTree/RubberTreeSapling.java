package shagejack.industrimania.content.world.nature.rubberTree;

import net.minecraft.world.level.block.SaplingBlock;

public class RubberTreeSapling extends SaplingBlock {

    public RubberTreeSapling(Properties properties) {
        super(new RubberTreeGrower(), properties);
    }

}
