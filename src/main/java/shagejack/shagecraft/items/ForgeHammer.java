package shagejack.shagecraft.items;

import shagejack.shagecraft.items.includes.ShageBaseItem;

public class ForgeHammer extends ShageBaseItem {

    public ForgeHammer(String name) {
        super(name);
        setMaxStackSize(1);
        this.setMaxDamage(1024);
    }

}
