package shagejack.shagecraft.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import shagejack.shagecraft.container.slot.SlotInventory;
import shagejack.shagecraft.data.Inventory;
import shagejack.shagecraft.data.inventory.Slot;
import shagejack.shagecraft.tile.TileEntityForgeFurnace;
import shagejack.shagecraft.util.ShageContainerHelper;

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
        for (shagejack.shagecraft.data.inventory.Slot slot : machineInventory.getSlots()) {
                addSlotToContainer(new SlotInventory(machineInventory, slot, 0, 0));
        }
        ShageContainerHelper.AddPlayerSlots(inventory, this, 45, 89, true, true);
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
