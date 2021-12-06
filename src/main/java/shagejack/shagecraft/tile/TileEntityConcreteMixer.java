package shagejack.shagecraft.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import shagejack.shagecraft.data.Inventory;
import shagejack.shagecraft.data.tank.ShageFluidTank;
import shagejack.shagecraft.data.inventory.SlotOne;
import shagejack.shagecraft.machines.ShageTileEntityMachine;
import shagejack.shagecraft.machines.MachineNBTCategory;
import shagejack.shagecraft.machines.events.MachineEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.EnumSet;

public class TileEntityConcreteMixer extends ShageTileEntityMachine implements ITickable {

    public ShageFluidTank tank;

    private int prevStirProgress = 0;
    private int stirProgress = 0;
    private int stirCount = 0;
    private int itemPos = 0;
    private boolean mixing = false;
    private boolean countUp = true;

    public static int[] input_slot = new int [9];

    public TileEntityConcreteMixer() {
        super();
        this.tank = new ShageFluidTank(null, Fluid.BUCKET_VOLUME * 3);
        this.tank.setTileEntity(this);
    }

    @Override
    protected void RegisterSlots(Inventory inventory) {
        for (int i = 0; i < input_slot.length; i++) {
            input_slot[i] = inventory.AddSlot(new SlotOne(false));
        }
        super.RegisterSlots(inventory);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        tank.onContentsChanged();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
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

        tank.writeToNBT(nbt);

        //nbt.setInteger("stirProgress", stirProgress);
        nbt.setInteger("stirCount", stirCount);
        nbt.setBoolean("mixing", getMixing());
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.INVENTORY)) {
            inventory.readFromNBT(nbt);
        }

        tank.readFromNBT(nbt);

        //stirProgress = nbt.getInteger("stirProgress");
        stirCount = nbt.getInteger("stirCount");
        mixing = nbt.getBoolean("mixing");
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void update() {
            prevStirProgress = stirProgress;
            if (getWorld().isRemote) {

                if (countUp && itemPos <= 20) {
                    itemPos++;
                    if (itemPos == 20)
                        countUp = false;
                }
                if (!countUp && itemPos >= 0) {
                    itemPos--;
                    if (itemPos == 0)
                        countUp = true;
                }
            }

            if (getMixing() && stirProgress < 90)
                stirProgress += 2;
            if (stirProgress >= 90) {
                setMixing(false);
                stirProgress = 0;
                prevStirProgress = 0;
            }

            if (!getWorld().isRemote) {
                if (getStirCount() >= 20 && tank.getFluidAmount() >= Fluid.BUCKET_VOLUME && stirProgress >= 88) {
                    ItemStack output = ItemStack.EMPTY;

                    //I know this piece of code is really fucked up, but it works anyway
                    String[][] recipes = {
                            {"minecraft:sand:0", "minecraft:sand:0", "minecraft:gravel:0", "minecraft:gravel:0", "shagecraft:clinker:0", "shagecraft:clinker:0", "contenttweaker:dustslag:0", "primal:carbonate_slack:0", "gregtech:meta_item_1:4287"}
                    };

                    ItemStack[] recipesOutput = {
                            new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("tconstruct:soil")))
                    };

                    FluidStack[][] recipesFluidStack = {
                            {new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME),
                            new FluidStack(FluidRegistry.getFluid("watersurfacecontaminated"), Fluid.BUCKET_VOLUME),
                            new FluidStack(FluidRegistry.getFluid("watermineralcontaminated"), Fluid.BUCKET_VOLUME),
                            new FluidStack(FluidRegistry.getFluid("wateroceancontaminated"), Fluid.BUCKET_VOLUME)}
                    };

                    int recipe = -1;

                    for (int i = 0; i < recipes.length; i++) {
                        int index = 0;
                        int[] consumed_slots = new int[9];
                        int consumed = 0;
                        int j = 0;

                        int itemCount = 0;
                        for (int m = 0; m < input_slot.length; m++) {
                            if (!inventory.getSlot(input_slot[m]).getItem().isEmpty()) {
                                itemCount++;
                            }
                        }

                        //LogMCL.debug("item count is " + itemCount + " and current recipe item count is " + recipes[i].length);

                        if (itemCount != recipes[i].length) continue;

                        //LogMCL.debug("item count matched!");

                        while (consumed < input_slot.length) {

                            StringBuilder s = new StringBuilder();
                            for (int num : consumed_slots) s.append(num).append(",");

                            //LogMCL.debug("index=" + index + ",consumed=" + consumed + ", j=" + j + " while consumed slots are " + s);

                            if (index == recipes[i].length) {
                                break;
                            }

                            if (recipes[i][index] == null) {
                                break;
                            }

                            for (int k = 0; k < consumed_slots.length; k++) {
                                if (j == consumed_slots[k]) j++;
                            }

                            if (matches(inventory.getSlot(input_slot[j]).getItem(), recipes[i][index])) {
                                index++;
                                consumed_slots[consumed] = j;
                                j = 0;
                                consumed++;
                                continue;
                            }

                            if (j == 8) {
                                break;
                            } else {
                                j++;
                            }
                        }
                        if (index < recipes[i].length - 1) {
                            recipe = -1;
                        } else {
                            recipe = i;
                        }
                    }

                    int recipeFluid = -1;

                    if (tank.getFluid() == null) recipe = -1;

                    if(recipe != -1) {

                        for(int i = 0; i < recipesFluidStack[recipe].length; i++) {
                            if (tank.getFluid().isFluidEqual(recipesFluidStack[recipe][i]))
                                recipeFluid = i;
                        }

                    }

                    if(recipeFluid == -1) recipe = -1;

                    if(recipe != -1) {
                        if (tank.getFluidAmount() < recipesFluidStack[recipe][recipeFluid].amount)
                            recipe = -1;
                    }

                    if (recipe == -1) {
                        setStirCount(0);
                        return;
                    } else {
                        output = recipesOutput[recipe];
                        for (int i = 0; i < 9; i++)
                            if (!inventory.getSlot(input_slot[i]).getItem().isEmpty())
                                inventory.setInventorySlotContents(input_slot[i], ItemStack.EMPTY);
                        tank.drain(Fluid.BUCKET_VOLUME, true);
                        setStirCount(0);
                        spawnItemStack(getWorld(), pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D, output);
                    }
                }
            }
    }

    public static void spawnItemStack(World world, double x, double y, double z, ItemStack stack) {
        EntityItem entityitem = new EntityItem(world, x, y, z, stack);
        entityitem.motionX = 0D;
        entityitem.motionY = 0D;
        entityitem.motionZ = 0D;
        entityitem.setPickupDelay(20);
        world.spawnEntity(entityitem);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(getPos(), 0, tag);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) tank;
        return super.getCapability(capability, facing);
    }

    public void setStirCount(int stirs) {
        this.stirCount = stirs;
        markForUpdate();
    }

    public int getStirCount() {
        return stirCount;
    }

    public int getStirProgress() {
        return stirProgress;
    }
    public boolean getMixing() {
        return mixing;
    }

    public void setMixing(boolean mix) {
        mixing = mix;
        markForUpdate();
    }

    public int getItemPos() {
        return itemPos;
    }

    public void markForUpdate() {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 2);
    }

    public int getPrevStirProgress() {
        return prevStirProgress;
    }

    public boolean matches(ItemStack stack, String item){
        String[] temp = item.split(":");
        String name = null;
        int meta = 0;
        if (temp.length == 3) {
            name = temp[0] + ":" + temp[1];
            meta = Integer.parseInt(temp[2]);
        } else if (temp.length == 2) {
            name = temp[0] + ":" + temp[1];
        }

        if(name == null) return false;
        if (!stack.getItem().getRegistryName().toString().equalsIgnoreCase(name)) return false;
        if (stack.getItemDamage() != meta) return false;
        return true;
    }



}
