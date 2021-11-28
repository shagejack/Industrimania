package shagejack.minecraftology.items;

import shagejack.minecraftology.items.includes.MCLBaseItem;

public class FlatFile extends MCLBaseItem {

    public FlatFile(String name) {
        super(name);
        setMaxStackSize(1);
        this.setMaxDamage(1024);
    }

}
