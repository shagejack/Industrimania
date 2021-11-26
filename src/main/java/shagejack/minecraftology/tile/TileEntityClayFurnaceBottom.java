package shagejack.minecraftology.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.init.BlocksMCL;
import shagejack.minecraftology.util.MCLMultiBlockCheckHelper;
import shagejack.minecraftology.machines.MachineNBTCategory;
import shagejack.minecraftology.multiblock.IMultiBlockTile;

import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import shagejack.minecraftology.multiblock.IMultiBlockTileStructure;
import shagejack.minecraftology.multiblock.MultiBlockTileStructureMachine;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.EnumSet;
import java.util.List;

public class TileEntityClayFurnaceBottom extends MCLTileEntity implements IMCLTickable, ITickable {

    private double durability;
    private double mol_Iron;
    private double mol_IronOxide;
    private double mol_Charcoal;
    private double mol_CarbonOxide;
    private double mol_CalciumOxide;
    private double mol_Slag;
    private double temperature;
    private double mol_OxygenFlow;
    private double mol_Impurities;
    private boolean completed;
    private boolean burning;

    private int complete;

    private final String clay = "minecraftology:building.fine_clay";
    private final String tube = "minecraftology:mechanic.bronze_tube";
    private final String coal = "minecraftology:gravity.charcoal";

    //Multi Blocks
    private String[] structure = {
            clay,clay,clay,
            clay,     clay,
            clay,clay,clay,

            clay,clay,clay,
            clay,     clay,
            clay,clay,clay,

            clay,clay,clay,
            clay,coal,clay,
            clay,clay,clay,

            clay,clay,clay,
            clay,coal,clay,
            clay,clay,clay,

            clay,tube,clay,
            clay,coal,clay,
            clay,clay,clay,

            clay,clay,clay,
            clay,     clay,
            clay,clay,clay
    };

    private BlockPos[] posArr = {
            new BlockPos(-1, 5, 1),   new BlockPos(0, 5, 1),  new BlockPos(1, 5, 1),
            new BlockPos(-1, 5, 0),                                    new BlockPos(1, 5, 0),
            new BlockPos(-1, 5, -1),  new BlockPos(0, 5, -1),  new BlockPos(1, 5, -1),

            new BlockPos(-1, 4, 1),   new BlockPos(0, 4, 1),  new BlockPos(1, 4, 1),
            new BlockPos(-1, 4, 0),                                    new BlockPos(1, 4, 0),
            new BlockPos(-1, 4, -1),  new BlockPos(0, 4, -1),  new BlockPos(1, 4, -1),

            new BlockPos(-1, 3, 1),   new BlockPos(0, 3, 1),  new BlockPos(1, 3, 1),
            new BlockPos(-1, 3, 0),   new BlockPos(0, 3, 0),  new BlockPos(1, 3, 0),
            new BlockPos(-1, 3, -1),  new BlockPos(0, 3, -1),  new BlockPos(1, 3, -1),

            new BlockPos(-1, 2, 1),   new BlockPos(0, 2, 1),  new BlockPos(1, 2, 1),
            new BlockPos(-1, 2, 0),   new BlockPos(0, 2, 0),  new BlockPos(1, 2, 0),
            new BlockPos(-1, 2, -1),  new BlockPos(0, 2, -1),  new BlockPos(1, 2, -1),

            new BlockPos(-1, 1, 1),   new BlockPos(0, 1, 1),  new BlockPos(1, 1, 1),
            new BlockPos(-1, 1, 0),   new BlockPos(0, 1, 0),  new BlockPos(1, 1, 0),
            new BlockPos(-1, 1, -1),  new BlockPos(0, 1, -1),  new BlockPos(1, 1, -1),

            new BlockPos(-1, 0, 1),   new BlockPos(0, 0, 1),  new BlockPos(1, 0, 1),
            new BlockPos(-1, 0, 0),                                    new BlockPos(1, 0, 0),
            new BlockPos(-1, 0, -1),  new BlockPos(0, 0, -1),  new BlockPos(1, 0, -1)
    };

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        if (categories.contains(MachineNBTCategory.DATA)) {

            nbt.setBoolean("completed", completed);
            nbt.setBoolean("burning", burning);
            nbt.setDouble("durability", durability);
            nbt.setDouble("mol_Iron", mol_Iron);
            nbt.setDouble("mol_IronOxide", mol_IronOxide);
            nbt.setDouble("mol_Charcoal", mol_Charcoal);
            nbt.setDouble("mol_CarbonOxide", mol_CarbonOxide);
            nbt.setDouble("mol_CalciumOxide", mol_CalciumOxide);
            nbt.setDouble("mol_Slag", mol_Slag);
            nbt.setDouble("temperature", temperature);
            nbt.setDouble("mol_OxygenFlow", mol_OxygenFlow);
            nbt.setDouble("mol_Impurities", mol_Impurities);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.DATA)) {
            completed = nbt.getBoolean("completed");
            burning = nbt.getBoolean("burning");
            mol_Iron = nbt.getDouble("mol_Iron");
            mol_IronOxide = nbt.getDouble("mol_IronOxide");
            mol_Charcoal = nbt.getDouble("mol_Charcoal");
            mol_CarbonOxide = nbt.getDouble("mol_CarbonOxide");
            mol_CalciumOxide = nbt.getDouble("mol_CalciumOxide");
            mol_Slag = nbt.getDouble("mol_Slag");
            temperature = nbt.getDouble("temperature");
            mol_OxygenFlow = nbt.getDouble("mol_OxygenFlow");
            mol_Impurities = nbt.getDouble("mol_Impurities");
            durability = nbt.getDouble("durability");
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
                checkComplete(world);
                if (complete != -1) {
                    if (!completed) {
                        //Initialization
                        durability = 100;
                        mol_Charcoal = 3;
                        mol_OxygenFlow = 1;
                        temperature = 298.15;
                        completed = true;
                    } else if(durability > 0) {
                        manageInput(world);
                        if (burning) {
                            manageBurn(world);
                        } else {
                            if (temperature > 298.15) {
                                temperature -= 1;
                            } else {
                                temperature += 1;
                            }
                        }
                    } else {
                        burnOut();
                    }
                } else {
                    if (completed){
                        burnOut();
                    }
                }
            markDirty();
            }

    }

    public void manageInput(World world) {
        BlockPos inputUp = getPos().add(0,5,0);
        BlockPos inputDown = getPos().add(0,4,0);

        if (world.getBlockState(inputDown).getBlock() != Blocks.AIR) {
            if (world.getBlockState(inputDown).getBlock() != Blocks.FIRE) {
                if (world.getBlockState(inputUp).getBlock() == Minecraftology.BLOCKS.gravity_charcoal || world.getBlockState(inputUp).getBlock() == Minecraftology.BLOCKS.gravity_iron_oxide || world.getBlockState(inputUp).getBlock() == Minecraftology.BLOCKS.gravity_calcite) {
                    consumeBlock(world, inputDown);
                } else if (world.getBlockState(inputUp).getBlock() == Blocks.FIRE && world.getBlockState(inputDown).getBlock() == Minecraftology.BLOCKS.gravity_charcoal) {
                    burning = true;
                }
            } else {
                burning = true;
            }
        }


    }

    public void manageBurn(World world) {
        /*
        输入参数:
        mol_Charcoal
        mol_IronOxide
        mol_CalciumOxide
        mol_OxygenFlow
        mol_Impurities
        temperature
        中间产品:
        mol_CarbonOxide
        输出参数:
        mol_Iron
        mol_Slag
        其他参数:
        durability
         */

        durability -= 0.0004 * temperature / 200;
        resetVarBelowZero(world);
        adjustOxygenFlow(world);
        adjustTemperature(world);
        smelting(world);

    }

    public void resetVarBelowZero(World world){
        mol_CalciumOxide = getToZero(mol_CalciumOxide);
        mol_Charcoal = getToZero(mol_Charcoal);
        mol_Iron = getToZero(mol_Iron);
        mol_CarbonOxide = getToZero(mol_CarbonOxide);
        mol_IronOxide = getToZero(mol_IronOxide);
        mol_OxygenFlow = getToZero(mol_OxygenFlow);
        mol_Impurities = getToZero(mol_Impurities);
        mol_Slag = getToZero(mol_Slag);
    }

    public double getToZero(double num){
        if (num < 0) num = 0;
        return num;
    }


    public void adjustOxygenFlow(World world) {

    }

    public void smelting(World world) {
        /*
        !INSTANT!: CaCO3 ~ CaO + CO2

        *750 ~ 1000*
        CaO + 3C ~ CO + CaC2
        Fe2O3 + 3CO ~ 2Fe
        CaO + Impurities(SiO2, P2O5) ~ Slag

        *1000 ~ 1200*
        C ~ 2CO
        CaO + 3C ~ CO
        Fe2O3 + 3CO ~ 2Fe
        CaO + Impurities(SiO2, P2O5) ~ Slag


       *Only When Too Much Oxygen*
        2CO+ O2 ~ 2CO2
        4Fe + 3O2 ~ 2Fe2O3
         */

        /*
        相关参数:
        temperature
        mol_IronOxide
        mol_Impurities
        mol_Charcoal
        mol_CarbonOxide
        mol_CalciumOxide
        mol_OxygenFlow

        mol_Iron
        mol_Slag
         */

        if (temperature > 1023.15){
            //*Only When Too Much Oxygen*
            if (mol_OxygenFlow > 3.5){

                double r4 = getReactionAmount(4);
                double r5 = getReactionAmount(5);

                mol_CarbonOxide -= r4 * 2;
                temperature += 5 * r4;

                mol_IronOxide += r5 * 2;
                mol_Iron -= r5 * 4;
                temperature += 16 * r5;
            }

            if(temperature < 1273.15){
                //*900 ~ 1000*

                double r1 = getReactionAmount(1);
                double r2 = getReactionAmount(2);
                double r3 = getReactionAmount(3);

                //CaO + 3C ~ CO
                mol_CalciumOxide -= r1;
                mol_Charcoal -= r1 * 3;
                mol_CarbonOxide += r1;
                temperature -= 4 * r1;

                //Fe2O3 + 3CO ~ 2Fe
                mol_IronOxide -= r2;
                mol_CarbonOxide -= r2 * 3;
                mol_Iron += r2 * 2;
                temperature += 0.2 * r2;


                //CaO + Impurities(SiO2, P2O5) ~ Slag
                mol_CalciumOxide -= r3;
                mol_Impurities -= r3;
                mol_Slag += r3;
                temperature += 20 * r3;

            } else {
                //*1000 ~ 1200*

                double r0 = getReactionAmount(0);
                double r1 = getReactionAmount(1);
                double r2 = getReactionAmount(2);
                double r3 = getReactionAmount(3);

                //C ~ 2CO
                mol_Charcoal -= r0;
                mol_CarbonOxide += r0 * 2;
                temperature -= 2 * r0;

                //CaO + 3C ~ CO
                mol_CalciumOxide -= r1;
                mol_Charcoal -= r1 * 3;
                mol_CarbonOxide += r1;
                temperature -= 4 * r1;

                //Fe2O3 + 3CO ~ 2Fe
                mol_IronOxide -= r2;
                mol_CarbonOxide -= r2 * 3;
                mol_Iron += r2 * 2;
                temperature += 0.2 * r2;

                //CaO + Impurities(SiO2, P2O5) ~ Slag
                mol_CalciumOxide -= r3;
                mol_Impurities -= r3;
                mol_Slag += r3;
                temperature += 20 * r3;


            }

        }

    }

    public double getReactionAmount(int reaction){
        double amount = 0;
        /*
        [0]C ~ 2CO
        [1]CaO+3C ~ CO
        [2]Fe2O3 + 3CO ~ 2Fe
        [3]CaO + Impurities(SiO2, P2O5) ~ Slag
        [4]2CO+ O2 ~ 2CO2
        [5]4Fe + 3O2 ~ 2Fe2O3
         */

        switch(reaction){
            case 0:
                amount = 0.0005 * temperature / 800 - mol_CarbonOxide * 0.0001 * temperature / 800;
                if (amount > mol_Charcoal) amount = mol_Charcoal;
                if (amount < 0) amount = 0;
                break;
            case 1:
                amount = 0.00005 * temperature / 800 - mol_CarbonOxide * 0.00001 * temperature / 800;
                if (amount > mol_CalciumOxide || amount * 3 > mol_Charcoal) amount = Math.min(mol_CalciumOxide, mol_Charcoal / 3 );
                if (amount < 0) amount = 0;
                break;
            case 2:
                amount = mol_CarbonOxide * 0.00025 * temperature / 800;
                if (amount > mol_IronOxide || amount * 3 > mol_CarbonOxide) amount = Math.min(mol_IronOxide, mol_CarbonOxide/ 3 );
                if (amount < 0) amount = 0;
                break;
            case 3:
                amount = 0.00005 * temperature / 500;
                if (amount > mol_CalciumOxide || amount > mol_Impurities) amount = Math.min(mol_CalciumOxide, mol_Impurities);
                if (amount < 0) amount = 0;
                break;
            case 4:
                amount = mol_CarbonOxide * mol_OxygenFlow * 0.000005 * temperature / 800;
                if (amount * 2 > mol_CarbonOxide) amount = mol_CarbonOxide / 2;
                if (amount < 0) amount = 0;
                break;
            case 5:
                amount = mol_OxygenFlow * 0.000005 * temperature / 800;
                if (amount * 4 > mol_Iron) amount = mol_Iron / 4;
                if (amount < 0) amount = 0;
                break;
        }

        return amount;
    }

    public void adjustTemperature(World world){
        /*
        相关参数:
        mol_Charcoal
        mol_OxygenFlow
        temperature
        durability
         */
        if (mol_Charcoal > 0){

            double consume = getRealCharcoalConsumption();

            mol_Charcoal -= consume;
            temperature += getRealTemperatureIncrease(consume);

        } else {
            temperature -= 0.1;
            if (temperature < 873.5) burning = false;
        }
    }

    public double getRealCharcoalConsumption(){
        double consume = 0;

        consume = 0.000005 + 0.00005 * mol_OxygenFlow + 0.00005 * temperature / 1000;

        if (consume >= mol_Charcoal){
            consume = mol_Charcoal;
        }

        return consume;
    }

    public double getRealTemperatureIncrease(double consume){
        double increase = 0;

        if (temperature < 298.15){
            increase += 0.001;
            increase += consume * 320 * Math.pow(mol_OxygenFlow, 0.25);
        } else if (temperature < 1173.15) {
            increase -= 0.001;
            increase += consume * 320 * Math.pow(mol_OxygenFlow, 0.25);
        } else if (temperature < 1273.15) {
            increase -= 0.002;
            increase += consume * 300 * Math.pow(mol_OxygenFlow, 0.25);
        } else if (temperature < 1373.15) {
            increase -= 0.005;
            increase += consume * 280 * Math.pow(mol_OxygenFlow, 0.25);
        } else if (temperature < 1473.15) {
            increase -= 0.01;
            increase += consume * 250 * Math.pow(mol_OxygenFlow, 0.25);
        }

        return increase;
    }

    //TODO: Data Rationalization
    public void consumeBlock(World world, BlockPos posInput){
        if (world.getBlockState(posInput).getBlock() == Minecraftology.BLOCKS.gravity_iron_oxide && mol_IronOxide <= 0.5){
            world.setBlockToAir(posInput);
            mol_IronOxide += 1;
            mol_Impurities += 1;
        } else if (world.getBlockState(posInput).getBlock() == Minecraftology.BLOCKS.gravity_calcite && mol_CalciumOxide <= 0.5){
            world.setBlockToAir(posInput);
            mol_CalciumOxide += 1;
        } else if (world.getBlockState(posInput).getBlock() == Minecraftology.BLOCKS.gravity_charcoal && mol_Charcoal <= 0.5){
            world.setBlockToAir(posInput);
            mol_Charcoal += 1;
        }
    }


    public void burnOut() {
        //world.setBlockState(getPos(), Minecraftology.BLOCKS.building_fine_clay.getDefaultState());
    }

    public void checkComplete(World world) {
        complete = MCLMultiBlockCheckHelper.checkComplete(world, getPos(), structure, posArr);
    }

    public void debugCheckComplete(){
        complete = MCLMultiBlockCheckHelper.checkComplete(world, getPos(), structure, posArr, true);
    }

}
