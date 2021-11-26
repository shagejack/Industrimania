package shagejack.minecraftology.machines.components;

import net.minecraft.nbt.NBTTagCompound;
import shagejack.minecraftology.data.Inventory;
import shagejack.minecraftology.machines.MCLTileEntityMachine;
import shagejack.minecraftology.machines.MachineComponentAbstract;
import shagejack.minecraftology.machines.MachineNBTCategory;
import shagejack.minecraftology.machines.configs.IConfigProperty;
import shagejack.minecraftology.machines.configs.IConfigurable;
import shagejack.minecraftology.machines.events.MachineEvent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class ComponentConfigs extends MachineComponentAbstract<MCLTileEntityMachine> implements IConfigurable {
    private final Map<String, IConfigProperty> propertyMap;

    public ComponentConfigs(MCLTileEntityMachine machine) {
        super(machine);
        propertyMap = new HashMap<>();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.CONFIGS)) {
            for (IConfigProperty property : propertyMap.values()) {
                property.readFromNBT(nbt);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        if (categories.contains(MachineNBTCategory.CONFIGS)) {
            for (IConfigProperty property : propertyMap.values()) {
                property.writeToNBT(nbt);
            }
        }
    }

    @Override
    public void registerSlots(Inventory inventory) {

    }

    /*@Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return false;
    }*/

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void onMachineEvent(MachineEvent event) {

    }

    @Override
    public Map<String, IConfigProperty> getValues() {
        return propertyMap;
    }

    @Override
    public IConfigProperty getProperty(String name) {
        return propertyMap.get(name);
    }

    public void addProperty(IConfigProperty property) {
        propertyMap.put(property.getKey(), property);
    }

    public boolean getBoolean(String key, boolean def) {
        IConfigProperty property = propertyMap.get(key);
        if (property != null && property.getType() == Boolean.class) {
            return (Boolean) property.getValue();
        }
        return def;
    }

    public Integer getInteger(String key, int def) {
        IConfigProperty property = propertyMap.get(key);
        if (property != null && property.getType().equals(Integer.class)) {
            return (Integer) property.getValue();
        }
        return def;
    }

    public Integer getEnum(String key, int def) {
        IConfigProperty property = propertyMap.get(key);
        if (property != null && property.getType().equals(Enum.class)) {
            return (Integer) property.getValue();
        }
        return def;
    }

    public String getString(String key, String def) {
        IConfigProperty property = propertyMap.get(key);
        if (property != null && property.getType().equals(String.class)) {
            return (String) property.getValue();
        }
        return def;
    }
}
