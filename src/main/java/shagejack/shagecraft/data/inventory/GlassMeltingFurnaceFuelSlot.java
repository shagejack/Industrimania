package shagejack.shagecraft.data.inventory;

import net.minecraft.item.ItemStack;
import shagejack.shagecraft.tile.TileEntityGlassMeltingFurnaceInput;

public class GlassMeltingFurnaceFuelSlot extends Slot {

    public GlassMeltingFurnaceFuelSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    @Override
    public boolean isValidForSlot(ItemStack stack) {
        return TileEntityGlassMeltingFurnaceInput.isValidFuel(stack);
    }

}