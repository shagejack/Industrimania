/*
package shagejack.minecraftology.capability.io;

import java.util.HashMap;
import java.util.Map;

import com.sun.istack.internal.NotNull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import shagejack.shagecraft.api.LazyOptional;
import shagejack.shagecraft.api.storage.IShagecraftCapabilityProvide;
import shagejack.shagecraft.api.storage.IShagecraftStorageCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class ShagecraftCapabilityProvider implements IShagecraftCapabilityProvide {
    public final Map<Capability<? extends IMinecraftologyStorageCapability<?,?,?,?>>,LazyOptional<? extends IMinecraftologyStorageCapability<?,?,?,?>>> CAPABILITY_MAP = new HashMap<>();
    public final Map<ResourceLocation, LazyOptional<? extends IMinecraftologyStorageCapability<?, ?, ?, ?>>> CAPABILITIES = new HashMap<>();

    public <T extends IMinecraftologyStorageCapability<?, ?, ?, ?>> MinecraftologyCapabilityProvider add(ResourceLocation name, Capability<T> capability, LazyOptional<T> lazyOptional) {
        CAPABILITY_MAP.put(capability, lazyOptional);
        CAPABILITIES.put(name, lazyOptional);
        return this;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return false;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable EnumFacing side) {
        return CAPABILITY_MAP.get(cap) == null ? LazyOptional.empty() : CAPABILITY_MAP.get(cap).cast();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound result = new NBTTagCompound();
        CAPABILITIES.forEach((key, value) -> {
            result.put(key.toString(),value.resolve().get().serializeNBT());
        });
        return result;
    }
    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        CAPABILITIES.forEach((key, value) -> {
            value.resolve().get().deserializeNBT((NBTTagCompound) nbt.get(key.toString()));
        });
    }
}
*/
