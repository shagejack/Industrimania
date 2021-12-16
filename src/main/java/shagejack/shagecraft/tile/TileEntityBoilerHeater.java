package shagejack.shagecraft.tile;

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
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.data.Inventory;
import shagejack.shagecraft.data.inventory.FuelSlot;
import shagejack.shagecraft.machines.MachineNBTCategory;
import shagejack.shagecraft.machines.ShageTileEntityMachine;
import shagejack.shagecraft.machines.events.MachineEvent;

import java.util.EnumSet;

public class TileEntityBoilerHeater extends ShageTileEntityMachine implements IShageTickable, ITickable {

    public static int[] fuel_slots = new int[5];

    public int[] fuels = new int[5];
    public double temperature;

    @Override
    protected void RegisterSlots(Inventory inventory) {
        for (int i = 0; i < fuel_slots.length; i++) {
            fuel_slots[i] =inventory.AddSlot(new FuelSlot(false));
        }
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
            for (int i = 0; i < fuel_slots.length; i++) {
                manageBurn(i);
            }
            manageTemperature();
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
        for (int i = 0; i < fuels.length; i++) {
            fuels[i] = 0;
        }
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

        nbt.setIntArray("fuels", fuels);
        nbt.setDouble("temperature", temperature);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.INVENTORY)) {
            inventory.readFromNBT(nbt);
        }

        fuels = nbt.getIntArray("fuels");
        temperature = nbt.getDouble("temperature");
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

    public boolean isBurning(int slot_id) {
        return fuels[slot_id] > 0;
    }

    public void manageBurn(int slot_id) {
        if(fuels[slot_id] <= 0) {
            ItemStack fuelStack = inventory.getSlot(slot_id).getItem();
            if (TileEntityFurnace.isItemFuel(fuelStack)) {
                fuels[slot_id] += TileEntityFurnace.getItemBurnTime(fuelStack) / 2;
                fuelStack.shrink(1);
            }
        } else {

            if (temperature < 900 + 100 * getBurningSlotsAmount()) {
                temperature += 2;
            }
            fuels[slot_id] --;
        }
    }

    public int getBurningSlotsAmount() {
        int counter = 0;
        for (int i = 0; i < fuel_slots.length; i++) {
            if (isBurning(i)) counter ++;
        }
        return counter;
    }

    public void manageTemperature() {
        if (temperature > 298.15) {
            temperature -= 0.01;
            if (world.getBlockState(pos.up()).getBlock() == Shagecraft.BLOCKS.steam_boiler) {
                TileEntityMachineBoiler te = (TileEntityMachineBoiler) world.getTileEntity(pos.up());
                if (te == null) return;

                if (te.getTemp() < temperature) {
                        te.setTemp(te.getTemp() + temperature / 10);
                        temperature -= temperature / 150;
                }
            }
        }
    }



}
