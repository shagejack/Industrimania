package shagejack.shagecraft.items;

import shagejack.shagecraft.items.includes.ShageBaseItem;

public class Saw extends ShageBaseItem {

    public Saw(String name) {
        super(name);
        setMaxStackSize(1);
        this.setMaxDamage(1024);
    }

}
