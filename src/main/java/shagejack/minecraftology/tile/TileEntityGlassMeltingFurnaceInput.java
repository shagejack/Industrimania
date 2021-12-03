package shagejack.minecraftology.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import shagejack.minecraftology.data.Inventory;
import shagejack.minecraftology.data.inventory.FuelSlot;
import shagejack.minecraftology.data.inventory.GlassMeltingFurnaceFuelSlot;
import shagejack.minecraftology.data.inventory.Slot;
import shagejack.minecraftology.data.inventory.SlotOne;
import shagejack.minecraftology.machines.MCLTileEntityMachine;
import shagejack.minecraftology.machines.MachineNBTCategory;
import shagejack.minecraftology.machines.events.MachineEvent;

import java.util.EnumSet;

public class TileEntityGlassMeltingFurnaceInput extends MCLTileEntityMachine implements IMCLTickable, ITickable {

    public static int fuel_slot;

    public int fuel;

    @Override
    protected void RegisterSlots(Inventory inventory) {
        fuel_slot = inventory.AddSlot(new GlassMeltingFurnaceFuelSlot(false));
        super.RegisterSlots(inventory);
    }

    @Override
    public void update() {
    }

    @Override
    public void onServerTick(TickEvent.Phase phase, World world) {
        if (world == null) {
            return;
        }

        if (phase.equals(TickEvent.Phase.END)) {
            manageBurn();
        }
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return false;
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
        if (categories.contains(MachineNBTCategory.INVENTORY) && toDisk) {
            inventory.writeToNBT(nbt, true);
        }

        nbt.setInteger("fuel", fuel);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.INVENTORY)) {
            inventory.readFromNBT(nbt);
        }

        fuel = nbt.getInteger("fuel");
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
    }

    public static boolean isValidFuel(ItemStack stack){
        if (stack.getItem().getRegistryName().toString().equalsIgnoreCase("minecraft:coal")) return true;
        if (stack.getItem().getRegistryName().toString().equalsIgnoreCase("minecraft:charcoal")) return true;
        return false;
    }

    public boolean isBurning(){
        return fuel > 0;
    }

    public void manageBurn(){
        if(fuel <= 0) {
            ItemStack fuelStack = inventory.getSlot(fuel_slot).getItem();
            if (isValidFuel(fuelStack)) {
                fuel += TileEntityFurnace.getItemBurnTime(fuelStack);
                fuelStack.shrink(1);
            }
        } else {
            fuel --;
        }
    }




}
