package shagejack.minecraftology.items;

import net.minecraft.item.ItemStack;
import shagejack.minecraftology.items.includes.MCLBaseItem;

import javax.annotation.Nonnull;

public class ForgeHammer extends MCLBaseItem {

    public ForgeHammer(String name) {
        super(name);
        setMaxStackSize(1);
        this.setMaxDamage(256);
    }

}
