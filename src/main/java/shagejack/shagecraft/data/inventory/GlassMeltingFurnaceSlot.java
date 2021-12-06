package shagejack.shagecraft.data.inventory;

import net.minecraft.item.ItemStack;
import shagejack.shagecraft.tile.TileEntityGlassMeltingFurnace;

public class GlassMeltingFurnaceSlot extends Slot {

    public GlassMeltingFurnaceSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    @Override
    public boolean isValidForSlot(ItemStack stack) {
        return TileEntityGlassMeltingFurnace.isMaterial(stack);
    }

}