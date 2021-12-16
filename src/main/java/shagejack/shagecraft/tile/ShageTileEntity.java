package shagejack.shagecraft.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.api.IShageTileEntity;
import shagejack.shagecraft.machines.MachineNBTCategory;
import shagejack.shagecraft.network.packet.server.PacketSendMachineNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

public abstract class ShageTileEntity extends TileEntity implements IShageTileEntity {
    public ShageTileEntity() {
        super();
    }

    public ShageTileEntity(World world, int meta) {
        super();
    }

    public static void playSound(World worldObj, SoundEvent soundName, double x, double y, double z, double volume, double pitch) {
        SPacketSoundEffect soundEffect = new SPacketSoundEffect(soundName, SoundCategory.BLOCKS, x, y, z, (float) volume, (float) pitch);

        for (int j = 0; j < worldObj.playerEntities.size(); ++j) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)worldObj.playerEntities.get(j);
            double d7 = x - entityplayermp.posX;
            double d8 = y - entityplayermp.posY;
            double d9 = z - entityplayermp.posZ;
            double d10 = d7 * d7 + d8 * d8 + d9 * d9;

            if (d10 <= 256.0D) {
                entityplayermp.connection.sendPacket(soundEffect);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        readCustomNBT(nbt, MachineNBTCategory.ALL_OPTS);
    }

    public boolean shouldRender() {
        return world.getBlockState(getPos()).getBlock() == getBlockType();
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        writeCustomNBT(nbt, MachineNBTCategory.ALL_OPTS, true);
        return nbt;
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.getPos(), 0, this.getUpdateTag());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState != newSate;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        PacketDispatcher.dispatchTEToNearbyPlayers(this);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    public abstract void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk);

    public abstract void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories);

    @SideOnly(Side.CLIENT)
    public void sendNBTToServer(EnumSet<MachineNBTCategory> categories, boolean forceUpdate, boolean sendDisk) {
        if (world.isRemote) {
            Shagecraft.NETWORK.sendToServer(new PacketSendMachineNBT(categories, this, forceUpdate, sendDisk));
        }
    }

    protected abstract void onAwake(Side side);
}