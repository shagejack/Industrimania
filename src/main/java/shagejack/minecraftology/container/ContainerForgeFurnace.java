package shagejack.minecraftology.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import shagejack.minecraftology.container.slot.SlotInventory;
import shagejack.minecraftology.data.Inventory;
import shagejack.minecraftology.data.inventory.Slot;
import shagejack.minecraftology.tile.TileEntityForgeFurnace;
import shagejack.minecraftology.util.MCLContainerHelper;

public class ContainerForgeFurnace extends ContainerMachine<TileEntityForgeFurnace> {
    public ContainerForgeFurnace() {
        super();
    }

    public ContainerForgeFurnace(InventoryPlayer inventory, TileEntityForgeFurnace machine) {
        super(inventory, machine);
        onCraftMatrixChanged(machine);
    }

    @Override
    public void init(InventoryPlayer inventory) {
        Inventory machineInventory = machine.getInventoryContainer();
        for (shagejack.minecraftology.data.inventory.Slot slot : machineInventory.getSlots()) {
                addSlotToContainer(new SlotInventory(machineInventory, slot, 0, 0));
        }
        MCLContainerHelper.AddPlayerSlots(inventory, this, 45, 89, true, true);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        //machine.calculateRecipe();
    }

    private class InputSlot extends SlotInventory {
        public InputSlot(IInventory inventory, Slot slot, int x, int y) {
            super(inventory, slot, x, y);
        }

        @Override
        public void onSlotChanged() {
            //machine.calculateRecipe();
        }
    }
}
