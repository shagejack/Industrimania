package shagejack.shagecraft.util;

import net.minecraft.entity.player.InventoryPlayer;
import shagejack.shagecraft.container.ShageBaseContainer;
import shagejack.shagecraft.container.slot.SlotPlayerInventory;

public class ShageContainerHelper {
    public static void AddPlayerSlots(InventoryPlayer inventory, ShageBaseContainer container, int x, int y, boolean main, boolean hotbar) {
        if (main) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    container.addSlotToContainer(new SlotPlayerInventory(inventory, j + i * 9 + 9, x + j * 18, y + i * 18, false));
                }
            }
        }

        if (hotbar) {
            for (int i = 0; i < 9; i++) {
                container.addSlotToContainer(new SlotPlayerInventory(inventory, i, x + i * 18, y + 61, true));
            }
        }
    }
}
