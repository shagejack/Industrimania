package shagejack.industrimania.content;

import net.minecraft.world.item.CreativeModeTab;
import shagejack.industrimania.foundation.item.ItemDescription.Palette;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import shagejack.industrimania.registries.AllTabs;

public enum AllSections {

    /** Industrimania Mechanics */
    MECHANIC(AllTabs.tabMain, Palette.Red),

    NATURE(AllTabs.tabNature, Palette.Green),

    TOOL(AllTabs.tabTool, Palette.Blue),

    EQUIPMENT(AllTabs.tabEquipment, Palette.Yellow),

    MATERIAL(AllTabs.tabMaterial, Palette.Purple),

    MISC(AllTabs.tabMisc, Palette.Gray)



    ;

    private CreativeModeTab tab;
    private Palette tooltipPalette;

    AllSections(CreativeModeTab tab, Palette tooltipPalette) {
        this.tooltipPalette = tooltipPalette;
    }

    public Palette getTooltipPalette() {
        return this.tooltipPalette;
    }

    public CreativeModeTab getTab() {
        return this.tab;
    }

    public static AllSections of(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof BlockItem)
            return ofBlock(((BlockItem) item).getBlock());
        return ofItem(item);
    }

    //TODO: return section
    static AllSections ofItem(Item item) {
        return null;
    }

    static AllSections ofBlock(Block block) {
        return null;
    }

}
