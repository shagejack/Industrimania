package shagejack.minecraftology.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.ItemHandlerHelper;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.api.firetongs.IToolFireTongs;
import shagejack.minecraftology.blocks.includes.MCLBlock;
import shagejack.minecraftology.blocks.includes.MCLBlockContainer;
import shagejack.minecraftology.items.FireTongs;
import shagejack.minecraftology.tile.TileEntityForge;
import shagejack.minecraftology.util.LogMCL;
import shagejack.minecraftology.util.MachineHelper;

import java.util.Arrays;

public class BlockWaterPool extends MCLBlock {

    private static final ItemStack[] toolStackList = {
        new ItemStack(Minecraftology.ITEMS.iron_cluster)
    };

    private static final int[][] shapeList = {
            //big plate
            {9,9},
            //ingot
            {9,8},
            //small plate
            {6,6}
    };

    public BlockWaterPool(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);
        setHardness(4.0F);
        setResistance(4.0F);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn instanceof FakePlayer || playerIn == null)
            return false;

        ItemStack stack = playerIn.getHeldItem(EnumHand.MAIN_HAND);
        Item held = stack.getItem();

        if (held != Minecraftology.ITEMS.fire_tongs) {

            if (isValidCoolItem(stack, playerIn)) {
                if (Minecraftology.ITEMS.iron_cluster.getTemp(stack) > 873.15) {
                    ItemStack result = getCoolResult(stack, playerIn);
                    stack.setCount(0);
                    ItemHandlerHelper.giveItemToPlayer(playerIn, result, EntityEquipmentSlot.MAINHAND.getSlotIndex());
                    return true;
                }
            }

        } else {
            if(held instanceof IToolFireTongs) {
                IToolFireTongs fireTongs = (IToolFireTongs) held;
                if(!fireTongs.canFireTongs(stack)) {
                        Item containItem = Item.REGISTRY.getObject(new ResourceLocation(Minecraftology.ITEMS.fire_tongs.getItemID(stack)));
                        ItemStack contain = new ItemStack(containItem);
                        Minecraftology.ITEMS.iron_cluster.setMass(contain, Minecraftology.ITEMS.fire_tongs.getMass(stack));
                        Minecraftology.ITEMS.iron_cluster.setImpurities(contain, Minecraftology.ITEMS.fire_tongs.getImpurities(stack));
                        Minecraftology.ITEMS.iron_cluster.setCarbon(contain, Minecraftology.ITEMS.fire_tongs.getCarbon(stack));
                        Minecraftology.ITEMS.iron_cluster.setTemp(contain, Minecraftology.ITEMS.fire_tongs.getTemp(stack));
                        Minecraftology.ITEMS.iron_cluster.setShape(contain, Minecraftology.ITEMS.fire_tongs.getShape(stack));

                        if (isValidCoolItem(contain, playerIn)) {
                            if (Minecraftology.ITEMS.iron_cluster.getTemp(contain) > 873.15) {
                                ItemStack result = getCoolResult(contain, playerIn);
                                ItemHandlerHelper.giveItemToPlayer(playerIn, result, EntityEquipmentSlot.MAINHAND.getSlotIndex());
                                Minecraftology.ITEMS.fire_tongs.setHasItem(stack, false);
                                Minecraftology.ITEMS.fire_tongs.setItemID(stack, "");
                                Minecraftology.ITEMS.fire_tongs.setMass(stack, 0);
                                Minecraftology.ITEMS.fire_tongs.setImpurities(stack, 0);
                                Minecraftology.ITEMS.fire_tongs.setCarbon(stack, 0);
                                Minecraftology.ITEMS.fire_tongs.setTemp(stack, 0);
                                Minecraftology.ITEMS.fire_tongs.setShape(stack, new int[0]);

                                playerIn.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);

                                return true;
                            }
                        }
                }
            }
        }

        return false;
    }

    public static boolean isValidCoolItem(ItemStack stack, EntityPlayer player) {
        return !stack.isEmpty() && (isCoolItemWhitelisted(stack));
    }

    private static boolean isCoolItemWhitelisted(ItemStack stack) {
        for (ItemStack itemStack : toolStackList) {
            if (ItemStack.areItemsEqualIgnoreDurability(itemStack, stack))
                return true;
        }
        return false;
    }

    public ItemStack getCoolResult(ItemStack itemStack, EntityPlayer player){
        ItemStack coolResultStack;
        double mass = Minecraftology.ITEMS.iron_cluster.getMass(itemStack);
        double carbon = Minecraftology.ITEMS.iron_cluster.getCarbon(itemStack);
        double impurities = Minecraftology.ITEMS.iron_cluster.getImpurities(itemStack);
        int[] shape = Minecraftology.ITEMS.iron_cluster.getShape(itemStack);

        LogMCL.debug((shape == shapeList[0]) ? "true" : "false");
        LogMCL.debug(shape.toString());

        if(impurities < 0.1) {
            if(carbon < 0.002) {
                LogMCL.debug("wrought");
                //wrought iron
                if(Arrays.equals(shape, shapeList[0])){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.wrought_iron_big_plate);
                    Minecraftology.ITEMS.wrought_iron_big_plate.setMass(coolResultStack, mass);
                } else if(Arrays.equals(shape, shapeList[1])){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.wrought_iron_ingot);
                    Minecraftology.ITEMS.wrought_iron_ingot.setMass(coolResultStack, mass);
                } else if(Arrays.equals(shape, shapeList[2])){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.wrought_iron_small_plate);
                    Minecraftology.ITEMS.wrought_iron_small_plate.setMass(coolResultStack, mass);
                } else {
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_rubbish);
                    Minecraftology.ITEMS.iron_rubbish.setMass(coolResultStack, mass);
                }
            } else if (carbon < 0.02){
                //iron
                if(Arrays.equals(shape, shapeList[0])){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_big_plate);
                    Minecraftology.ITEMS.iron_big_plate.setMass(coolResultStack, mass);
                } else if(Arrays.equals(shape, shapeList[1])){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_ingot);
                    Minecraftology.ITEMS.iron_ingot.setMass(coolResultStack, mass);
                } else if(Arrays.equals(shape, shapeList[2])){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_small_plate);
                    Minecraftology.ITEMS.iron_small_plate.setMass(coolResultStack, mass);
                } else {
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_rubbish);
                    Minecraftology.ITEMS.iron_rubbish.setMass(coolResultStack, mass);
                }
            } else if (carbon < 0.043){
                //pig iron
                if(Arrays.equals(shape, shapeList[0])){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.pig_iron_big_plate);
                    Minecraftology.ITEMS.pig_iron_big_plate.setMass(coolResultStack, mass);
                } else if(Arrays.equals(shape, shapeList[1])){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.pig_iron_ingot);
                    Minecraftology.ITEMS.pig_iron_ingot.setMass(coolResultStack, mass);
                } else if(Arrays.equals(shape, shapeList[2])){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.pig_iron_small_plate);
                    Minecraftology.ITEMS.pig_iron_small_plate.setMass(coolResultStack, mass);
                } else {
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_rubbish);
                    Minecraftology.ITEMS.iron_rubbish.setMass(coolResultStack, mass);
                }
            } else {
                //rubbish
                coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_rubbish);
                Minecraftology.ITEMS.iron_rubbish.setMass(coolResultStack, mass);
            }
        } else {
            //rubbish
            coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_rubbish);
            Minecraftology.ITEMS.iron_rubbish.setMass(coolResultStack, mass);
        }

        //TODO:Play Sound
        player.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("block.fire.extinguish")), 1, 1);

        return coolResultStack;
    }


}
