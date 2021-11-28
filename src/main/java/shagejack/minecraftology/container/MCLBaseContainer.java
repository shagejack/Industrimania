package shagejack.minecraftology.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import shagejack.minecraftology.container.slot.MCLSlot;

import javax.annotation.Nonnull;

public abstract class MCLBaseContainer extends Container {
    public MCLBaseContainer() {
    }

    public MCLBaseContainer(InventoryPlayer inventoryPlayer) {
    }

    @Nonnull
    public Slot addSlotToContainer(Slot slot) {
        return super.addSlotToContainer(slot);
    }

    public MCLSlot getSlotAt(int id) {
        return (MCLSlot) inventorySlots.get(id);
    }
}
