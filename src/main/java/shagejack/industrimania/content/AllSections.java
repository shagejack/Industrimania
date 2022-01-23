package shagejack.industrimania.content;

import shagejack.industrimania.foundation.item.ItemDescription.Palette;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public enum AllSections {

    /** Industrimania Mechanics */
    MECHANICS(Palette.Red),

    /** Item transport and other Utility */
    LOGISTICS(Palette.Yellow),

    /** Base materials, ingredients and tools */
    MATERIALS(Palette.Green),

    ;

    private Palette tooltipPalette;

    AllSections(Palette tooltipPalette) {
        this.tooltipPalette = tooltipPalette;
    }

    public Palette getTooltipPalette() {
        return tooltipPalette;
    }

    public static AllSections of(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof BlockItem)
            return ofBlock(((BlockItem) item).getBlock());
        return ofItem(item);
    }


    //TODO: return section
    static AllSections ofItem(Item item) {
        return AllSections.MATERIALS;
    }

    static AllSections ofBlock(Block block) {
        return AllSections.MECHANICS;
    }

}
