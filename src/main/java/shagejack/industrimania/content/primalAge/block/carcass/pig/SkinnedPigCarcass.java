package shagejack.industrimania.content.primalAge.block.carcass.pig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import shagejack.industrimania.content.primalAge.block.carcass.AbstractSkinnedCarcass;

public class SkinnedPigCarcass extends AbstractSkinnedCarcass {

    public SkinnedPigCarcass(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack onCut(Level level, BlockPos pos) {
        return ItemStack.EMPTY;
    }

}
