package shagejack.minecraftology.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.minecraftology.data.inventory.Slot;

public class SlotInventory extends MCLSlot {

    Slot slot;

    public SlotInventory(IInventory inventory, Slot slot, int x, int y) {
        super(inventory, slot.getId(), x, y);
        this.slot = slot;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return slot.isValidForSlot(itemStack);
    }

    public int getSlotStackLimit() {
        return slot.getMaxStackSize();
    }

    public Slot getSlot() {
        return slot;
    }

    public String getUnlocalizedTooltip() {
        return slot.getUnlocalizedTooltip();
    }

    public void onSlotChanged() {
        super.onSlotChanged();
        slot.onSlotChanged();
    }
}

