package shagejack.minecraftology.capability.io;

import net.minecraft.item.ItemStack;
import shagejack.minecraftology.api.storage.io.IReplyContext;

import java.util.Arrays;
import java.util.List;

public class ItemStackReplyContext implements IReplyContext<ItemStack> {
    boolean success;
    ItemStack[] stacks;

    public ItemStackReplyContext(boolean success,ItemStack... stacks) {
        this.success = success;
        this.stacks = stacks;
    }
    @Override
    public boolean success() {
        return success;
    }
    @Override
    public List<ItemStack> get() {
        return Arrays.asList(stacks);
    }
}
