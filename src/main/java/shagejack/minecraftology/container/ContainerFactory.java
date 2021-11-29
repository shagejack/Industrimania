package shagejack.minecraftology.container;

import net.minecraft.entity.player.InventoryPlayer;
import shagejack.minecraftology.machines.MCLTileEntityMachine;
import shagejack.minecraftology.util.MCLContainerHelper;

public class ContainerFactory {
    public static <T extends MCLTileEntityMachine> ContainerMachine<T> createMachineContainer(T machine, InventoryPlayer inventoryPlayer) {
        ContainerMachine<T> containerMachine = new ContainerMachine<>(inventoryPlayer, machine);
        containerMachine.addAllSlotsFromInventory(machine.getInventoryContainer());
        MCLContainerHelper.AddPlayerSlots(inventoryPlayer, containerMachine, 45, 89, machine.hasPlayerSlotsMain(), machine.hasPlayerSlotsHotbar());
        return containerMachine;
    }
}

