package shagejack.shagecraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import shagejack.shagecraft.util.LogShage;
import shagejack.shagecraft.util.StackUtils;

import java.util.concurrent.Callable;

public class ShageTab extends CreativeTabs {
    private ItemStack itemstack = ItemStack.EMPTY;
    private Callable<ItemStack> stackCallable;

    public ShageTab(String label, Callable<ItemStack> stackCallable) {
        super(label);
        this.stackCallable = stackCallable;
    }

    @Override
    public ItemStack createIcon() {
        return null;
    }

    @Override
    public ItemStack getIcon() {
        if (StackUtils.isNullOrEmpty(itemstack)) {
            if (stackCallable != null) {
                try {
                    itemstack = stackCallable.call();
                } catch (Exception e) {
                    LogShage.error(e.getMessage(), e);
                }
            } else {
                itemstack = new ItemStack(Shagecraft.ITEMS.multimeter);
            }
        }
        return itemstack;
    }
}
