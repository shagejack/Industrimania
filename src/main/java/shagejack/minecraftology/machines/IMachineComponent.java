package shagejack.minecraftology.machines;

import net.minecraft.nbt.NBTTagCompound;
import shagejack.minecraftology.data.Inventory;
import shagejack.minecraftology.machines.events.MachineEvent;

import java.util.EnumSet;

public interface IMachineComponent {
    void readFromNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories);

    void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk);

    void registerSlots(Inventory inventory);

    //boolean isAffectedByUpgrade(UpgradeTypes type);

    boolean isActive();

    void onMachineEvent(MachineEvent event);
}
