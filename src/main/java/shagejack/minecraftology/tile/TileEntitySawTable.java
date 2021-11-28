package shagejack.minecraftology.tile;

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
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.data.Inventory;
import shagejack.minecraftology.data.inventory.Slot;
import shagejack.minecraftology.items.IronCluster;
import shagejack.minecraftology.items.MaterialWithMass;
import shagejack.minecraftology.machines.MachineNBTCategory;
import shagejack.minecraftology.machines.events.MachineEvent;

import java.util.EnumSet;

public class TileEntitySawTable extends MCLTileEntityContainer {

    public int file_slot;

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
                        case "minecraftology:wrought_iron_ingot":
                            newStack = new ItemStack(Minecraftology.ITEMS.cut_wrought_iron_ingot, amount);
                            break;
                        case "minecraftology:iron_ingot":
                            newStack = new ItemStack(Minecraftology.ITEMS.cut_iron_ingot, amount);
                            break;
                        case "minecraftology:pig_iron_ingot":newStack = new ItemStack(Minecraftology.ITEMS.cut_pig_iron_ingot, amount);
                            break;
                        default:
                            newStack = itemStack.copy();
                            flag = false;
                    }

                    ItemStack stackLeft = new ItemStack(Minecraftology.ITEMS.iron_rubbish);

                    if(flag) {
                        Minecraftology.ITEMS.iron_rubbish.setMass(stackLeft, massLeft);

                        EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.15, pos.getZ() + 0.5, newStack);

                        inventory.setInventorySlotContents(0, stackLeft);
                        world.spawnEntity(item);
                    } else {
                        Minecraftology.ITEMS.iron_rubbish.setMass(stackLeft, mass);

                        inventory.setInventorySlotContents(0, stackLeft);
                    }

                    player.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("block.anvil.place")), 1, 2);
                }
            }
        }
    }


}
