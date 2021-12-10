package shagejack.shagecraft.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import shagejack.shagecraft.blocks.BlockForgeFurnace;
import shagejack.shagecraft.data.Inventory;
import shagejack.shagecraft.data.inventory.FuelSlot;
import shagejack.shagecraft.data.inventory.Slot;
import shagejack.shagecraft.machines.ShageTileEntityMachine;
import shagejack.shagecraft.machines.MachineNBTCategory;
import shagejack.shagecraft.machines.events.MachineEvent;

import java.util.EnumSet;

public class TileEntityForgeFurnace extends ShageTileEntityMachine implements IShageTickable, ITickable {

    public static int FUEL_SLOT_ID;
    public static int INPUT_SLOT_ID;

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
        FUEL_SLOT_ID = inventory.AddSlot(new FuelSlot(false));
        INPUT_SLOT_ID = inventory.AddSlot(new Slot(true));
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
        super.writeCustomNBT(nbt, categories, toDisk);
        if (categories.contains(MachineNBTCategory.DATA)) {
            nbt.setDouble("fuel", fuel);
            nbt.setDouble("furnaceTemp", furnaceTemp);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        super.readCustomNBT(nbt, categories);
        if (categories.contains(MachineNBTCategory.DATA)) {
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

        //LogMCL.debug("[0]" + (isLit() ? "true" : "false"));

        boolean flag = isLit();
        boolean flag1 = true;
        ItemStack fuelStack = inventory.getSlot(FUEL_SLOT_ID).getItem();
        if (fuel <= 0) {

            if(furnaceTemp > 298.15) {
                furnaceTemp -= 0.25;
            } else {
                flag1 = false;
            }

            if(!fuelStack.isEmpty()) {
                int burnTime = TileEntityFurnace.getItemBurnTime(fuelStack);
                    if(burnTime > 0) {
                        fuelStack.shrink(1);
                        fuel += burnTime;
                        flag1 = true;
                    }
            }

        } else {
            this.fuel --;
            if(furnaceTemp < 1173.15){
                furnaceTemp += 0.25;
            }
        }

        if (flag != isLit()) {
            BlockForgeFurnace.setState(isLit(), world, pos);
            //LogMCL.debug("State Changed!");
        }

        if (flag1) markDirty();

        //LogMCL.debug("[1]" + (isLit() ? "true" : "false"));

    }

    public void heatUp(){
        ItemStack inputStack = inventory.getSlot(INPUT_SLOT_ID).getItem();
            if (inputStack.getItem() == Shagecraft.ITEMS.iron_cluster) {
                double temp = Shagecraft.ITEMS.iron_cluster.getTemp(inputStack);
                if (temp < furnaceTemp) {
                   temp += 0.01 * Math.pow((furnaceTemp - temp), 0.5);
                }
                furnaceTemp -= 0.2;
                Shagecraft.ITEMS.iron_cluster.setTemp(inputStack, temp);
            } else if(inputStack.getItem() == Item.getItemFromBlock(Blocks.STONE_SLAB) && inputStack.getCount() == 1){
                if (furnaceTemp > 1073.15) {
                    if(Math.random() < 0.0001){
                        inputStack.shrink(1);
                        inventory.setInventorySlotContents(INPUT_SLOT_ID, new ItemStack(Shagecraft.ITEMS.burnt_stone_slab));
                    }
                    furnaceTemp -= 0.2;
                }
            }
    }

    public boolean isLit(){
        if(fuel > 0 || furnaceTemp > 498.15){
            return true;
        } else {
            return false;
        }
    }


}
