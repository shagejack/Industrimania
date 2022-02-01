package shagejack.industrimania.content.primalAge.block.nature.rubberTree;

import net.minecraft.world.level.block.SaplingBlock;

public class RubberTreeSapling extends SaplingBlock {

    public RubberTreeSapling(Properties properties) {
        super(new RubberTreeGrower(), properties);
    }

}
