package shagejack.minecraftology.tile;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.includes.MCLBlock;
import shagejack.minecraftology.machines.MachineNBTCategory;
import shagejack.minecraftology.util.LogMCL;
import shagejack.minecraftology.util.MCLMultiBlockCheckHelper;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class TileEntityIronOreSlag extends MCLTileEntity implements IMCLTickable, ITickable {

    private double mol_Iron;
    private double mol_IronOxide;
    private double mol_Slag;
    private double temperature;
    private double mol_Impurities;

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        if (categories.contains(MachineNBTCategory.DATA)) {

            nbt.setDouble("mol_Iron", mol_Iron);
            nbt.setDouble("mol_IronOxide", mol_IronOxide);
            nbt.setDouble("mol_Slag", mol_Slag);
            nbt.setDouble("temperature", temperature);
            nbt.setDouble("mol_Impurities", mol_Impurities);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.DATA)) {
            mol_Iron = nbt.getDouble("mol_Iron");
            mol_IronOxide = nbt.getDouble("mol_IronOxide");
            mol_Slag = nbt.getDouble("mol_Slag");
            temperature = nbt.getDouble("temperature");
            mol_Impurities = nbt.getDouble("mol_Impurities");
        }
    }


    @Override
    public void onAdded(World world, BlockPos pos, IBlockState state) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed(World worldIn, BlockPos pos, IBlockState state) {
        if (temperature < 373.15) {
            ItemStack ironStack = getRealIronDrop();
            ItemStack slagStack = getRealSlagDrop();
            if (!ironStack.isEmpty()) {
                EntityItem itemIron = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ironStack);
                worldIn.spawnEntity(itemIron);
            }
            if (!slagStack.isEmpty()) {
                EntityItem itemSlag = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, slagStack);
                worldIn.spawnEntity(itemSlag);
            }
        } else {
            world.setBlockState(pos, Blocks.FIRE.getDefaultState());
        }
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

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound syncData = new NBTTagCompound();
        writeCustomNBT(syncData, MachineNBTCategory.ALL_OPTS, false);
        return new SPacketUpdateTileEntity(getPos(), 1, syncData);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound syncData = pkt.getNbtCompound();
        if (syncData != null) {
            readCustomNBT(syncData, MachineNBTCategory.ALL_OPTS);
        }
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
            if (temperature > 273.15) temperature -= 0.1;
            markDirty();
            }
    }

    public void writeData(double iron, double ironOxide, double slag, double impurities, double temp){
        mol_Iron = iron;
        mol_IronOxide = ironOxide;
        mol_Slag = slag;
        mol_Impurities = impurities;
        temperature = temp;
    }

    public ItemStack getRealIronDrop(){
        ItemStack ironStack;

        double fortune = world.rand.nextDouble();

        double perMass = mol_Iron * 56 / 10;

        int baseCount = (int) Math.round(getPurity() * Math.sqrt(fortune) * 9);
        int fortuneCount = baseCount * 2 / 3 + world.rand.nextInt(baseCount / 3 + 1);

        ironStack = new ItemStack(Minecraftology.ITEMS.iron_cluster);

        ironStack.getTagCompound().setDouble("mass", perMass * fortuneCount);

        return ironStack;
    }

    public ItemStack getRealSlagDrop(){
        ItemStack ironStack;

        double fortune = world.rand.nextDouble();

        int baseCount = (int) Math.round((1-getPurity()) * Math.sqrt(fortune) * 3 + 3 * mol_Slag);
        int fortuneCount = baseCount * 2 / 3 + world.rand.nextInt(baseCount / 3 + 1);

        ironStack = new ItemStack(Minecraftology.ITEMS.slag, fortuneCount);

        return ironStack;
    }

    public double getPurity(){
        return mol_Iron / (mol_Iron + mol_IronOxide * 2 + mol_Impurities);
    }

}
