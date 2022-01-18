package shagejack.industrimania.content.metallurgyAge.block.smeltery.ironOreSlag;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.content.metallurgyAge.item.smeltery.cluster.IronCluster;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.registers.item.AllItems;
import shagejack.industrimania.registers.AllTileEntities;

import java.util.List;

public class IronOreSlagTileEntity extends SmartTileEntity {

    public IronOreSlagTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.iron_ore_slag.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {}

    private double mol_Iron;
    private double mol_IronOxide;
    private double mol_Slag;
    private double temperature;
    private double mol_Impurities;

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {

        nbt.putDouble("mol_Iron", mol_Iron);
        nbt.putDouble("mol_IronOxide", mol_IronOxide);
        nbt.putDouble("mol_Slag", mol_Slag);
        nbt.putDouble("temperature", temperature);
        nbt.putDouble("mol_Impurities", mol_Impurities);

    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {

        mol_Iron = nbt.getDouble("mol_Iron");
        mol_IronOxide = nbt.getDouble("mol_IronOxide");
        mol_Slag = nbt.getDouble("mol_Slag");
        temperature = nbt.getDouble("temperature");
        mol_Impurities = nbt.getDouble("mol_Impurities");

    }

    @Override
    public void tick() {
        if (level == null) {
            return;
        }

        if (temperature > 273.15) temperature -= 0.1;

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

        double fortune = level.random.nextDouble();

        double perMass = mol_Iron * 56 / 10;

        int count = (int) Math.round(getPurity() * Math.pow(fortune, 0.25) * 5 + 5);

        if(perMass * count > 0) {
            int[] shape = {9 + level.random.nextInt(2), 9 + level.random.nextInt(2)};
            ironStack = new ItemStack(AllItems.ironCluster.get());
            IronCluster.setMass(ironStack, perMass * count);
            IronCluster.setCarbon(ironStack, 0.02 + fortune * 0.01);
            IronCluster.setTemp(ironStack, 298.15);
            IronCluster.setImpurities(ironStack, 60  * (0.05 + mol_Impurities));
            IronCluster.setShape(ironStack, shape);
            return ironStack;
        }

        return null;
    }

    public ItemStack getRealSlagDrop(){
        ItemStack ironStack;

        double fortune = level.random.nextDouble();

        int baseCount = (int) Math.round((1-getPurity()) * Math.sqrt(fortune) * 3 + 3 * mol_Slag);
        int fortuneCount = baseCount * 2 / 3 + level.random.nextInt(baseCount / 3 + 1);

        ironStack = new ItemStack(AllItems.slag.get(), fortuneCount);

        return ironStack;
    }

    public double getPurity(){
        return mol_Iron / (mol_Iron + mol_IronOxide * 2 + mol_Impurities);
    }

    public void onBreak(Level level){
        if (temperature < 373.15) {
            ItemStack ironStack = getRealIronDrop();
            ItemStack slagStack = getRealSlagDrop();
            if (!ironStack.isEmpty()) {
                ItemEntity itemIron = new ItemEntity(level, getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5, ironStack);
                level.addFreshEntity(itemIron);
            }
            if (!slagStack.isEmpty()) {
                ItemEntity itemSlag = new ItemEntity(level, getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5, slagStack);
                level.addFreshEntity(itemSlag);
            }
        } else {
            level.setBlock(getBlockPos(), Blocks.FIRE.defaultBlockState(), 1);
        }
    }


}
