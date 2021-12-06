package shagejack.shagecraft.items;

import shagejack.shagecraft.items.includes.ShageBaseItem;

public class FlatFile extends ShageBaseItem {

    public FlatFile(String name) {
        super(name);
        setMaxStackSize(1);
        this.setMaxDamage(1024);
    }

}
