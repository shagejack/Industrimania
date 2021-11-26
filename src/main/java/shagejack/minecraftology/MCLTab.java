package shagejack.minecraftology;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import shagejack.minecraftology.client.model.MCLModelLoader;
import shagejack.minecraftology.util.LogMCL;
import shagejack.minecraftology.util.StackUtils;

import java.util.concurrent.Callable;

public class MCLTab extends CreativeTabs {
    private ItemStack itemstack = ItemStack.EMPTY;
    private Callable<ItemStack> stackCallable;

    public MCLTab(String label, Callable<ItemStack> stackCallable) {
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
                    LogMCL.error(e.getMessage(), e);
                }
            } else {
                itemstack = new ItemStack(Minecraftology.ITEMS.multimeter);
            }
        }
        return itemstack;
    }
}
