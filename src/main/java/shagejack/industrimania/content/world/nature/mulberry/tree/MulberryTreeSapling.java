package shagejack.industrimania.content.world.nature.mulberry.tree;

import net.minecraft.world.level.block.SaplingBlock;

public class MulberryTreeSapling extends SaplingBlock {

    public MulberryTreeSapling(Properties properties) {
        super(new MulberryTreeGrower(), properties);
    }

}
