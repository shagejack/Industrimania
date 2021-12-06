package shagejack.shagecraft.container;

import net.minecraft.entity.player.InventoryPlayer;
import shagejack.shagecraft.machines.ShageTileEntityMachine;
import shagejack.shagecraft.util.ShageContainerHelper;

public class ContainerFactory {
    public static <T extends ShageTileEntityMachine> ContainerMachine<T> createMachineContainer(T machine, InventoryPlayer inventoryPlayer) {
        ContainerMachine<T> containerMachine = new ContainerMachine<>(inventoryPlayer, machine);
        containerMachine.addAllSlotsFromInventory(machine.getInventoryContainer());
        ShageContainerHelper.AddPlayerSlots(inventoryPlayer, containerMachine, 45, 89, machine.hasPlayerSlotsMain(), machine.hasPlayerSlotsHotbar());
        return containerMachine;
    }
}

