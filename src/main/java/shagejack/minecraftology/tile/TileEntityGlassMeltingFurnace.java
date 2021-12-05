package shagejack.minecraftology.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.api.events.glassmeltingfurnace.MCLEventGlassMeltingFurnaceConsume;
import shagejack.minecraftology.blocks.includes.MCLBlock;
import shagejack.minecraftology.data.Inventory;
import shagejack.minecraftology.data.inventory.FuelSlot;
import shagejack.minecraftology.data.inventory.GlassMeltingFurnaceSlot;
import shagejack.minecraftology.data.inventory.Slot;
import shagejack.minecraftology.init.FluidsMCL;
import shagejack.minecraftology.machines.MCLTileEntityMachine;
import shagejack.minecraftology.machines.MachineNBTCategory;
import shagejack.minecraftology.machines.events.MachineEvent;
import shagejack.minecraftology.util.MCLMultiBlockCheckHelper;

import java.util.EnumSet;
import java.util.List;

public class TileEntityGlassMeltingFurnace extends MCLTileEntityMachine implements IMCLTickable, ITickable {

    public static int input_slot;

    public int complete;
    public int idle;
    public double temperature;
    public boolean completed;

    private final String brick = "minecraft:brick_block";
    private final String cauldron = "minecraft:cauldron";
    private final String input = "minecraftology:mechanic.glass_melting_furnace_input";
    private final String output = "minecraftology:mechanic.glass_melting_furnace_output";

    //Multi Blocks
    private String[] structure = {
            brick,brick,brick,
            brick,cauldron,brick,
            brick,brick,brick,

            brick,brick,brick,
            brick,      brick,
            brick,output,brick,

            brick,input,brick,
            brick,brick,brick,
            brick,brick,brick
    };

    private BlockPos[] posArr = {

            new BlockPos(-1, 1, 1),   new BlockPos(0, 1, 1),  new BlockPos(1, 1, 1),
            new BlockPos(-1, 1, 0),   new BlockPos(0, 1, 0),  new BlockPos(1, 1, 0),
            new BlockPos(-1, 1, -1),  new BlockPos(0, 1, -1),  new BlockPos(1, 1, -1),

            new BlockPos(-1, 0, 1),   new BlockPos(0, 0, 1),  new BlockPos(1, 0, 1),
            new BlockPos(-1, 0, 0),                                                           new BlockPos(1, 0, 0),
            new BlockPos(-1, 0, -1),  new BlockPos(0, 0, -1),  new BlockPos(1, 0, -1),

            new BlockPos(-1, -1, 1),   new BlockPos(0, -1, 1),  new BlockPos(1, -1, 1),
            new BlockPos(-1, -1, 0),   new BlockPos(0, -1, 0),  new BlockPos(1, -1, 0),
            new BlockPos(-1, -1, -1),  new BlockPos(0, -1, -1),  new BlockPos(1, -1, -1),
    };

    @Override
    protected void RegisterSlots(Inventory inventory) {
        input_slot = inventory.AddSlot(new GlassMeltingFurnaceSlot(false));
        super.RegisterSlots(inventory);
    }

    public static boolean isMaterial(ItemStack stack){
        if(stack.getItem() == Minecraftology.ITEMS.raw_glass_material) return true;
        return false;
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
            complete = checkComplete();
            if (complete != -1){
                if (!completed){
                    idle = 1200;
                    completed = true;
                }
                manageInput();
                manageBurn();
                manageOutput();
            } else {
                if (completed){
                    if (temperature > 473.15){
                        unsafeRemoved();
                    }
                }
            }

            markDirty();
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
        complete = -1;
        temperature = 298.15;
        completed = false;
    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed(World worldIn, BlockPos pos, IBlockState state) {
        if (temperature > 473.15) unsafeRemoved();
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

        nbt.setInteger("complete", complete);
        nbt.setInteger("idle", idle);
        nbt.setDouble("temperature", temperature);
        nbt.setBoolean("completed", completed);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.INVENTORY)) {
            inventory.readFromNBT(nbt);
        }

        complete = nbt.getInteger("complete");
        idle = nbt.getInteger("idle");
        temperature = nbt.getDouble("temperature");
        completed = nbt.getBoolean("completed");
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

    public int checkComplete(){
        int completeState;
        completeState = MCLMultiBlockCheckHelper.checkComplete(world, getPos(), structure, posArr);
        return completeState;
    }

    public boolean isBurning(){
       TileEntityGlassMeltingFurnaceInput tileEntity = (TileEntityGlassMeltingFurnaceInput) world.getTileEntity(pos.add(MCLMultiBlockCheckHelper.getRotatedPos(new BlockPos(0, -1, 1), complete)));
       if(tileEntity == null) return false;
       return tileEntity.isBurning();
    }

    public void manageInput(){

        double range = 1;
        AxisAlignedBB bb = new AxisAlignedBB(getPos().getX() - range, getPos().getY() + 1 - range, getPos().getZ() - range, getPos().getX() + range, getPos().getY() + 1 + range, getPos().getZ() + range);
        List entities = world.getEntitiesWithinAABB(Entity.class, bb);

        for (Object entityObject : entities) {
            if (entityObject instanceof Entity) {
                Entity entity = (Entity) entityObject;
                Vec3d entityPos = entity.getPositionVector();
                Vec3d blockPos = new Vec3d(getPos()).add(0.5, 1.5, 0.5);

                double distance = entityPos.distanceTo(blockPos);
                if (distance < 0.5) {
                    consume(entity);
                }
            }
        }
    }

    public void consume(Entity entity) {

        if (!entity.isDead && onEntityConsume(entity, true)) {

            boolean consumedFlag = false;

            if (entity instanceof EntityItem) {
                consumedFlag |= consumeEntityItem((EntityItem) entity);
            }

            if (consumedFlag) {
                onEntityConsume(entity, false);
            }
        }
    }

    private boolean consumeEntityItem(EntityItem entityItem) {
        ItemStack itemStack = entityItem.getItem();
        if (!itemStack.isEmpty()) {

            GlassMeltingFurnaceSlot slot = (GlassMeltingFurnaceSlot) inventory.getSlot(input_slot);
            if (slot == null) return false;
            if (itemStack.getItem() == Minecraftology.ITEMS.raw_glass_material) {
                if (slot.getItem().isEmpty()){
                    inventory.setInventorySlotContents(input_slot, itemStack);
                    entityItem.setDead();
                    world.removeEntity(entityItem);
                }
                if (slot.getItem().getItem() == Minecraftology.ITEMS.raw_glass_material){
                    if (slot.getMaxStackSize() >= slot.getItem().getCount() + itemStack.getCount()) {
                        ItemStack resultStack = slot.getItem().copy();
                        resultStack.setCount(resultStack.getCount() + itemStack.getCount());
                        inventory.setInventorySlotContents(input_slot, resultStack);
                        entityItem.setDead();
                        world.removeEntity(entityItem);
                    } else {
                        ItemStack resultStack = slot.getItem().copy();
                        entityItem.getItem().shrink(64 - resultStack.getCount());
                        resultStack.setCount(64);
                        inventory.setInventorySlotContents(input_slot, resultStack);
                    }
                }
            }

            return true;
        }
        return false;
    }


    private boolean onEntityConsume(Entity entity, boolean pre) {
        if (pre) {
            MinecraftForge.EVENT_BUS.post(new MCLEventGlassMeltingFurnaceConsume.Pre(entity, getPos()));
        } else {
            MinecraftForge.EVENT_BUS.post(new MCLEventGlassMeltingFurnaceConsume.Post(entity, getPos()));
        }

        return true;
    }

    public void manageBurn(){
        if(isBurning()){
            if(temperature < 1523.15){
                temperature += 0.025;
            }
        } else {
            if(temperature > 298.15){
                temperature -= 0.1;
            } else {
                temperature += 0.1;
            }
        }
    }

    public void manageOutput(){
        if (temperature > 1473.15) {
            TileEntityGlassMeltingFurnaceOutput tileEntity = (TileEntityGlassMeltingFurnaceOutput) world.getTileEntity(pos.add(MCLMultiBlockCheckHelper.getRotatedPos(new BlockPos(0, 0, -1), complete)));
            if (tileEntity == null) return;

            ItemStack inputStack = inventory.getStackInSlot(input_slot);
            if (inputStack.getItem() == Minecraftology.ITEMS.raw_glass_material){
                if (idle <= 0){
                    inputStack.shrink(1);
                    if (tileEntity.tank.canFillFluidType(new FluidStack(FluidsMCL.halfMoltenGlass, 100)) && tileEntity.tank.fill(new FluidStack(FluidsMCL.halfMoltenGlass, 100), false) == 100){
                        tileEntity.tank.fill(new FluidStack(FluidsMCL.halfMoltenGlass, 100), true);
                        idle = 1200;
                    }
                } else {
                    idle --;
                }
            }

        }
    }

    public void unsafeRemoved(){
        world.createExplosion(null, getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, 5.0F, true);
        world.setBlockToAir(pos);
    }

}
