package shagejack.minecraftology.data.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import shagejack.minecraftology.tile.TileEntityGlassMeltingFurnace;

public class GlassMeltingFurnaceSlot extends Slot {

    public GlassMeltingFurnaceSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    @Override
    public boolean isValidForSlot(ItemStack stack) {
        return TileEntityGlassMeltingFurnace.isMaterial(stack);
    }

}