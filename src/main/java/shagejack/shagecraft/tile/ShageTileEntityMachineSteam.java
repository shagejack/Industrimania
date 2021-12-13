package shagejack.shagecraft.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.data.MachineSteamStorage;
import shagejack.shagecraft.init.ShagecraftCapabilities;
import shagejack.shagecraft.machines.MachineNBTCategory;
import shagejack.shagecraft.machines.ShageTileEntityMachine;
import shagejack.shagecraft.network.packet.client.PacketSteamUpdate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

public abstract class ShageTileEntityMachineSteam extends ShageTileEntityMachine {
    protected MachineSteamStorage steamStorage;

    public ShageTileEntityMachineSteam() {
        steamStorage = new MachineSteamStorage<>(this, 2000);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        super.writeCustomNBT(nbt, categories, toDisk);
        if (categories.contains(MachineNBTCategory.DATA) && steamStorage != null) {
            steamStorage.writeToNBT(nbt);
        }

    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        super.readCustomNBT(nbt, categories);
        if (categories.contains(MachineNBTCategory.DATA) && steamStorage != null) {
            steamStorage.readFromNBT(nbt);
        }

    }

    public void updateClientSteam() {
        if (world != null) {
            Shagecraft.NETWORK.sendToAllAround(new PacketSteamUpdate(this), this, 64);
        }
    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {
        super.readFromPlaceItem(itemStack);

        if (itemStack != null && steamStorage != null) {
            if (itemStack.hasTagCompound()) {
                steamStorage.readFromNBT(itemStack.getTagCompound());
            }
        }
    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {
        super.writeToDropItem(itemStack);
    }

    public MachineSteamStorage getSteamStorage() {
        return steamStorage;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        steamStorage.onContentsChanged();
        super.onDataPacket(net, pkt);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == ShagecraftCapabilities.STEAM_HANDLER) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == ShagecraftCapabilities.STEAM_HANDLER) {
            return (T) steamStorage;
        }
        return super.getCapability(capability, facing);
    }
}
