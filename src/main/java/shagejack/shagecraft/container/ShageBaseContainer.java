package shagejack.shagecraft.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import shagejack.shagecraft.container.slot.ShageSlot;

import javax.annotation.Nonnull;

public abstract class ShageBaseContainer extends Container {
    public ShageBaseContainer() {
    }

    public ShageBaseContainer(InventoryPlayer inventoryPlayer) {
    }

    @Nonnull
    public Slot addSlotToContainer(Slot slot) {
        return super.addSlotToContainer(slot);
    }

    public ShageSlot getSlotAt(int id) {
        return (ShageSlot) inventorySlots.get(id);
    }
}
