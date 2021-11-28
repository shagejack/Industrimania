package shagejack.minecraftology.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class EmptyStorage<T> implements Capability.IStorage<T> {

    private static final EmptyStorage<?> _instance = new EmptyStorage<>();
    public static <T> EmptyStorage<T> getInstance() {
        return (EmptyStorage<T>) _instance;
    }
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
        return null;
    }
    @Override
    public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {

    }
}
