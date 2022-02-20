package shagejack.industrimania.content.primalAge.block.nature.mulberry.tree;

import net.minecraft.world.level.block.SaplingBlock;
import shagejack.industrimania.content.primalAge.block.nature.rubberTree.RubberTreeGrower;

public class MulberryTreeSapling extends SaplingBlock {

    public MulberryTreeSapling(Properties properties) {
        super(new MulberryTreeGrower(), properties);
    }

}
