package shagejack.shagecraft.data.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class FuelSlot extends Slot {

    public FuelSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    @Override
    public boolean isValidForSlot(ItemStack stack) {
        return TileEntityFurnace.isItemFuel(stack);
    }

}