package shagejack.minecraftology.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.includes.BlockStateHelper;
import shagejack.minecraftology.data.Inventory;
import shagejack.minecraftology.data.TileEntityInventory;
import shagejack.minecraftology.data.inventory.Slot;
import shagejack.minecraftology.machines.MachineNBTCategory;
import shagejack.minecraftology.machines.events.MachineEvent;
import shagejack.minecraftology.util.LogMCL;
import shagejack.minecraftology.util.MCLStringHelper;

import java.util.EnumSet;

public class TileEntityForgeFurnace extends MCLTileEntityContainer implements IMCLTickable, ITickable {

    public static int fuel_slot;
    public static int input_slot;

    private double temperature;
    private double fuel;
    private double furnaceTemp;

    private String[] fuelItemList = {
            "minecraft:coal",
            "minecraft:charcoal"
    };

    public TileEntityForgeFurnace(){
        super();
    }

    @Override
    protected void RegisterSlots(Inventory inventory) {
        fuel_slot = inventory.AddSlot(new Slot(false));
        input_slot = inventory.AddSlot(new Slot(true));
        super.RegisterSlots(inventory);
    }

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
        furnaceTemp = 298.15;
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

    @Override
    public void onChunkUnload() {
    }

    @Override
    protected void onAwake(Side side) {
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        if (categories.contains(MachineNBTCategory.DATA)) {
            nbt.setDouble("temperature", temperature);
            nbt.setDouble("fuel", fuel);
            nbt.setDouble("furnaceTemp", furnaceTemp);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.DATA)) {
            temperature = nbt.getDouble("temperature");
            fuel = nbt.getDouble("fuel");
            furnaceTemp = nbt.getDouble("furnaceTemp");
        }
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {
    }

    @Override
    public void onServerTick(TickEvent.Phase phase, World world) {
        if (world == null) {
            return;
        }

        if (phase.equals(TickEvent.Phase.END)) {
            consumeFuel();
            heatUp();
        }
    }

    public void consumeFuel(){
        ItemStack fuelStack = inventory.getSlot(fuel_slot).getItem();
        if (fuel <= 0) {
            for (String item : fuelItemList) {
                if (fuelStack.getItem().getRegistryName().toString().equalsIgnoreCase(item)) {
                    fuelStack.setCount(fuelStack.getCount() - 1);
                    fuel += 1200;
                    break;
                }
            }
            if(furnaceTemp > 298.15) {
                furnaceTemp -= 0.25;
            } else {
                BlockStateHelper.setState(false, world, pos);
            }
        } else {
            BlockStateHelper.setState(true, world, pos);
            fuel -= 1;
            if(furnaceTemp < 1173.15){
                furnaceTemp += 0.25;
            }
        }
    }

    public void heatUp(){
        ItemStack inputStack = inventory.getSlot(input_slot).getItem();
            if (inputStack.getItem() == Minecraftology.ITEMS.iron_cluster) {
                double temp = Minecraftology.ITEMS.iron_cluster.getTemp(inputStack);
                if (temp < furnaceTemp) {
                   temp += 0.1 * Math.pow((furnaceTemp - temp), 0.5);
                }
                Minecraftology.ITEMS.iron_cluster.setTemp(inputStack, temp);
            }
    }


}
