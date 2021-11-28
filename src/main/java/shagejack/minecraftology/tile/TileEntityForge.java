package shagejack.minecraftology.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.machines.MachineNBTCategory;
import shagejack.minecraftology.machines.events.MachineEvent;

import java.util.EnumSet;

public class TileEntityForge extends MCLTileEntityContainer {

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return true;
    }

    @Override
    public boolean getServerActive() {
        return true;
    }

    @Override
    public float soundVolume() {
        return 0.3f;
    }

    @Override
    public void onAdded(World world, BlockPos pos, IBlockState state) {
    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed(World worldIn, BlockPos pos, IBlockState state) {
    }

    @Override
    public void onNeighborBlockChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock) {

    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {

    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {

    }

    public void onChunkUnload() {
    }

    @Override
    protected void onAwake(Side side) {
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        if (categories.contains(MachineNBTCategory.DATA)) {

        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.DATA)) {

        }
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {
    }



    public void forge(EntityPlayer player, ItemStack held){
        if(held.getItemDamage() > 0){
            if(inventory.getSlot(0).getItem().getItem() == Minecraftology.ITEMS.iron_cluster) {
                held.damageItem(1, player);
            }
        }
    }


}
