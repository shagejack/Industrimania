package shagejack.shagecraft.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.data.Inventory;
import shagejack.shagecraft.data.inventory.Slot;
import shagejack.shagecraft.items.MaterialWithMass;
import shagejack.shagecraft.machines.ShageTileEntityMachine;
import shagejack.shagecraft.machines.MachineNBTCategory;
import shagejack.shagecraft.machines.events.MachineEvent;

import java.util.EnumSet;

public class TileEntitySawTable extends ShageTileEntityMachine {

    public static int file_slot;

    public TileEntitySawTable() {
        super();
    }

    @Override
    protected void RegisterSlots(Inventory inventory) {
        file_slot = inventory.AddSlot(new Slot(true));
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
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.INVENTORY)) {
            inventory.readFromNBT(nbt);
        }
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {
    }

    public void saw(EntityPlayer player, ItemStack held){

        if(held.getItemDamage() <= held.getMaxDamage()) {


            ItemStack itemStack = inventory.getSlot(0).getItem();
            if (itemStack.getItem() instanceof MaterialWithMass) {
                double mass = ((MaterialWithMass) itemStack.getItem()).getMass(itemStack);
                int cut = ((MaterialWithMass) itemStack.getItem()).getCut(itemStack);
                if (cut < 4) {
                    if (Math.random() < 0.1) {
                        cut += 1;
                        player.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("block.gravel.break")), 2, 2);
                    } else {
                        player.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("block.sand.break")), 2, 2);
                    }

                    held.damageItem(1, player);

                    player.getCooldownTracker().setCooldown(held.getItem(), 5);

                    ((MaterialWithMass) itemStack.getItem()).setMass(itemStack, mass);
                    ((MaterialWithMass) itemStack.getItem()).setCut(itemStack, cut);
                } else {
                    held.damageItem(1, player);
                    ItemStack newStack;

                    int amount = (int) Math.floor(mass / 40);
                    double massLeft = mass - amount * 40;
                    boolean flag = true;

                    switch(itemStack.getItem().getRegistryName().toString()){
                        case "shagecraft:wrought_iron_ingot":
                            newStack = new ItemStack(Shagecraft.ITEMS.cut_wrought_iron_ingot, amount);
                            break;
                        case "shagecraft:iron_ingot":
                            newStack = new ItemStack(Shagecraft.ITEMS.cut_iron_ingot, amount);
                            break;
                        case "shagecraft:pig_iron_ingot":
                            newStack = new ItemStack(Shagecraft.ITEMS.cut_pig_iron_ingot, amount);
                            break;
                        case "shagecraft:iron_rubbish":
                            newStack = new ItemStack(Shagecraft.ITEMS.cut_iron_rubbish, amount);
                        default:
                            newStack = itemStack.copy();
                            flag = false;
                    }

                    ItemStack stackLeft = new ItemStack(Shagecraft.ITEMS.iron_rubbish);

                    if(flag) {
                        Shagecraft.ITEMS.iron_rubbish.setMass(stackLeft, massLeft);

                        EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.15, pos.getZ() + 0.5, newStack);

                        inventory.setInventorySlotContents(0, stackLeft);
                        world.spawnEntity(item);
                    } else {
                        Shagecraft.ITEMS.iron_rubbish.setMass(stackLeft, mass);

                        inventory.setInventorySlotContents(0, stackLeft);
                    }

                    player.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("block.anvil.place")), 1, 2);
                }
            }
        }
    }


}
