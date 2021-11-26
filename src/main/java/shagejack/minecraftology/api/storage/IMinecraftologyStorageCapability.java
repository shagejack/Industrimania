package shagejack.minecraftology.api.storage;

import net.minecraftforge.common.util.INBTSerializable;
import net.minecraft.nbt.NBTTagCompound;
import shagejack.minecraftology.api.storage.io.IIputContext;
import shagejack.minecraftology.api.storage.io.IOutputContext;
import shagejack.minecraftology.api.storage.io.IReplyContext;

public interface IMinecraftologyStorageCapability<T,I extends IIputContext<T>, O extends IOutputContext<T>,R extends IReplyContext<T>> extends INBTSerializable<NBTTagCompound> {
    R tryAdd(I context);
    R tryGet(O context);

    default void sync() {
        // TODO
    }
}
