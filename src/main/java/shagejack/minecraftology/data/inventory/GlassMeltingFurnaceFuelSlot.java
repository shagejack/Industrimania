package shagejack.minecraftology.data.inventory;

import net.minecraft.item.ItemStack;
import shagejack.minecraftology.tile.TileEntityGlassMeltingFurnace;
import shagejack.minecraftology.tile.TileEntityGlassMeltingFurnaceInput;

public class GlassMeltingFurnaceFuelSlot extends Slot {

    public GlassMeltingFurnaceFuelSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    @Override
    public boolean isValidForSlot(ItemStack stack) {
        return TileEntityGlassMeltingFurnaceInput.isValidFuel(stack);
    }

}