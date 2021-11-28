package shagejack.minecraftology.tile;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
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
import shagejack.minecraftology.machines.MachineNBTCategory;
import shagejack.minecraftology.machines.events.MachineEvent;
import net.minecraft.util.SoundEvent;

import java.util.EnumSet;

public class TileEntityForge extends MCLTileEntityContainer {

    public int forge_slot;

    @Override
    protected void RegisterSlots(Inventory inventory) {
        forge_slot = inventory.AddSlot(new Slot(true));
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

    public void forge(EntityPlayer player, ItemStack held){
        if(held.getItemDamage() > 0) {
            ItemStack itemStack = inventory.getSlot(0).getItem();
            if (itemStack.getItem() == Minecraftology.ITEMS.iron_cluster) {
                boolean heavyHit = player.isSneaking();
                double mass = Minecraftology.ITEMS.iron_cluster.getMass(itemStack);
                double carbon = Minecraftology.ITEMS.iron_cluster.getCarbon(itemStack);
                double impurities = Minecraftology.ITEMS.iron_cluster.getImpurities(itemStack);
                double temp = Minecraftology.ITEMS.iron_cluster.getTemp(itemStack);
                int[] shape = Minecraftology.ITEMS.iron_cluster.getShape(itemStack);

                if (heavyHit) {

                    mass -= 0.0005 * Math.random();
                    carbon -= 0.0005 * Math.random();
                    impurities -= 0.005 * Math.random();
                    temp += 0.1 * Math.random();

                    if (mass <= 0) itemStack.setCount(0);
                    if (carbon <= 0.0001) carbon = 0.0001;
                    if (impurities < 0) impurities = 0;

                    if(Math.random() < 0.1) {
                        if(shape[0] > 1 && shape[1] > 1) {
                            shape[0] += 1;
                            shape[1] -= 1;
                        }
                    }

                    player.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("block.anvil.land")), 1, 2);

                    if(held.getItemDamage() > 1) {
                        held.damageItem(2, player);
                    } else {
                        held.damageItem(1, player);
                    }

                } else {

                    mass -= 0.00001 * Math.random();
                    carbon -= 0.0001 * Math.random();
                    impurities -= 0.002 * Math.random();
                    temp += 0.05 * Math.random();

                    if (mass <= 0) itemStack.setCount(0);
                    if (carbon <= 0.0001) carbon = 0.0001;
                    if (impurities < 0) impurities = 0;

                    if(Math.random() < 0.1) {
                        if(shape[0] > 1 && shape[1] > 1) {
                            shape[1] += 1;
                            shape[0] -= 1;
                        }
                    }

                    held.damageItem(1, player);

                    player.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("block.anvil.place")), 1, 1);

                }

                Minecraftology.ITEMS.iron_cluster.setMass(itemStack, mass);
                Minecraftology.ITEMS.iron_cluster.setCarbon(itemStack, carbon);
                Minecraftology.ITEMS.iron_cluster.setImpurities(itemStack, impurities);
                Minecraftology.ITEMS.iron_cluster.setTemp(itemStack, temp);
                Minecraftology.ITEMS.iron_cluster.setShape(itemStack, shape);

            }
        }
    }


}
