package shagejack.shagecraft.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.data.Inventory;
import shagejack.shagecraft.data.inventory.SlotOne;
import shagejack.shagecraft.data.tank.GlassMeltingFurnaceTank;
import shagejack.shagecraft.init.ShagecraftFluids;
import shagejack.shagecraft.machines.ShageTileEntityMachine;
import shagejack.shagecraft.machines.MachineNBTCategory;
import shagejack.shagecraft.machines.events.MachineEvent;

import java.util.EnumSet;

public class TileEntityGlassMould extends ShageTileEntityMachine implements ITickable {

    public GlassMeltingFurnaceTank tank;

    public static int[] input_slot = new int [1];

    private int prevBlowProgress = 0;
    private int blowProgress = 0;
    private int blowCount = 0;
    private boolean blowing = false;

    public TileEntityGlassMould() {
        super();
        this.tank = new GlassMeltingFurnaceTank(null, Fluid.BUCKET_VOLUME * 1);
        this.tank.setTileEntity(this);
    }

    @Override
    protected void RegisterSlots(Inventory inventory) {
        input_slot[0] = inventory.AddSlot(new SlotOne(false));
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

        nbt.setInteger("blowCount", blowCount);
        nbt.setBoolean("blowing", getBlowing());

    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.INVENTORY)) {
            inventory.readFromNBT(nbt);
        }

        tank.readFromNBT(nbt);

        blowCount = nbt.getInteger("blowCount");
        blowing = nbt.getBoolean("blowing");

    }

    public boolean getBlowing() {
        return blowing;
    }

    public void setBlowing(boolean blow) {
        blowing = blow;
        markForUpdate();
    }

    public void setBlowCount(int blows) {
        this.blowCount = blows;
        markForUpdate();
    }

    public int getBlowCount() {
        return blowCount;
    }

    public int getBlowProgress() {
        return blowProgress;
    }

    public int getPrevBlowProgress() {
        return prevBlowProgress;
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
        prevBlowProgress = blowProgress;

        if (getBlowing() && blowProgress < 90)
            blowProgress += 2;
        if (blowProgress >= 90) {
            setBlowing(false);
            blowProgress = 0;
            prevBlowProgress = 0;
        }

        if (!getWorld().isRemote) {
            if (getBlowCount() >= 20 && tank.getFluidAmount() >= Fluid.BUCKET_VOLUME) {
                ItemStack output = ItemStack.EMPTY;

                //I know this piece of code is really fucked up, but it works anyway
                ItemStack recipes[] = {
                        new ItemStack(Shagecraft.ITEMS.glass_mould_block)
                };

                ItemStack recipesOutput[] = {
                        new ItemStack(Item.getItemFromBlock(Blocks.GLASS))
                };

                int recipe = -1;

                for(int i = 0; i < recipes.length; i++){
                    if (inventory.getSlot(input_slot[0]).getItem().getItem() == recipes[i].getItem()){
                        recipe = i;
                    }
                }

                if (tank.getFluid() == null) recipe = -1;

                if(recipe != -1) {
                    if (!tank.getFluid().isFluidEqual(new FluidStack(ShagecraftFluids.halfMoltenGlass, Fluid.BUCKET_VOLUME))) recipe = -1;
                }

                if(recipe != -1) {
                    if (tank.getFluidAmount() < Fluid.BUCKET_VOLUME) recipe = -1;
                }

                if (recipe == -1) {
                    tank.drain(Fluid.BUCKET_VOLUME, true);
                    setBlowCount(0);
                    return;
                } else {
                    output = recipesOutput[recipe];
                    tank.drain(Fluid.BUCKET_VOLUME, true);
                    setBlowCount(0);
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


    public void markForUpdate() {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 2);
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
