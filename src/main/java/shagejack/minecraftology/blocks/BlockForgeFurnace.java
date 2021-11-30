package shagejack.minecraftology.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.api.firetongs.IToolFireTongs;
import shagejack.minecraftology.blocks.includes.MCLBlock;
import shagejack.minecraftology.blocks.includes.MCLBlockContainer;
import shagejack.minecraftology.blocks.includes.MCLBlockMachine;
import shagejack.minecraftology.data.Inventory;
import shagejack.minecraftology.items.FireTongs;
import shagejack.minecraftology.tile.TileEntityClayFurnaceBottom;
import shagejack.minecraftology.tile.TileEntityForge;
import shagejack.minecraftology.tile.TileEntityForgeFurnace;
import shagejack.minecraftology.util.LogMCL;
import shagejack.minecraftology.util.MCLBlockHelper;
import shagejack.minecraftology.util.MachineHelper;
import shagejack.minecraftology.util.MatterHelper;

import java.util.Random;

public class BlockForgeFurnace extends MCLBlockMachine<TileEntityForgeFurnace> {

    public static boolean keepInventory;

    private static final ItemStack[] toolStackList = {
        new ItemStack(Minecraftology.ITEMS.iron_cluster)
    };

    public BlockForgeFurnace(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);
        setHardness(4.0F);
        setResistance(4.0F);
        setHasGui(true);
        setHasRotation();
        setRotationType(MCLBlockHelper.RotationType.FOUR_WAY);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(EnumHand.MAIN_HAND);
        Item held = stack.getItem();
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (held != Minecraftology.ITEMS.fire_tongs) {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        } else {
            //LogMCL.debug("0");
            if(held instanceof IToolFireTongs) {
                //LogMCL.debug("1");
                IToolFireTongs fireTongs = (IToolFireTongs) held;
                if (tileEntity instanceof TileEntityForgeFurnace) {
                    //LogMCL.debug("2");
                    ItemStack input = ((TileEntityForgeFurnace) tileEntity).getInventory().getStackInSlot(TileEntityForgeFurnace.INPUT_SLOT_ID);
                    if (fireTongs.canFireTongs(stack) && (!input.isEmpty())) {
                        //LogMCL.debug("3");
                        if(input.getItem() == Minecraftology.ITEMS.iron_cluster){
                            stack.setTagCompound(input.getTagCompound());
                            Minecraftology.ITEMS.fire_tongs.setHasItem(stack,true);
                            Minecraftology.ITEMS.fire_tongs.setItemID(stack, input.getItem().getRegistryName().toString());
                            playerIn.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
                            input.shrink(1);
                        }
                    } else if ((!fireTongs.canFireTongs(stack)) && input.isEmpty()) {
                        Item containItem = Item.REGISTRY.getObject(new ResourceLocation(Minecraftology.ITEMS.fire_tongs.getItemID(stack)));
                        ItemStack contain = new ItemStack(containItem);
                        Minecraftology.ITEMS.iron_cluster.setMass(contain, Minecraftology.ITEMS.fire_tongs.getMass(stack));
                        Minecraftology.ITEMS.iron_cluster.setImpurities(contain, Minecraftology.ITEMS.fire_tongs.getImpurities(stack));
                        Minecraftology.ITEMS.iron_cluster.setCarbon(contain, Minecraftology.ITEMS.fire_tongs.getCarbon(stack));
                        Minecraftology.ITEMS.iron_cluster.setTemp(contain, Minecraftology.ITEMS.fire_tongs.getTemp(stack));
                        Minecraftology.ITEMS.iron_cluster.setShape(contain, Minecraftology.ITEMS.fire_tongs.getShape(stack));
                        ((TileEntityForgeFurnace) tileEntity).getInventory().setInventorySlotContents(TileEntityForgeFurnace.INPUT_SLOT_ID, contain);
                        Minecraftology.ITEMS.fire_tongs.setHasItem(stack, false);
                        Minecraftology.ITEMS.fire_tongs.setItemID(stack, "");
                        Minecraftology.ITEMS.fire_tongs.setMass(stack, 0);
                        Minecraftology.ITEMS.fire_tongs.setImpurities(stack, 0);
                        Minecraftology.ITEMS.fire_tongs.setCarbon(stack, 0);
                        Minecraftology.ITEMS.fire_tongs.setTemp(stack, 0);
                        Minecraftology.ITEMS.fire_tongs.setShape(stack, new int[0]);
                        playerIn.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
                    }
                }
            }
        }

        return true;

    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if(!keepInventory) {
            super.breakBlock(worldIn, pos, state);
        }
    }

    @Override
    public Class<TileEntityForgeFurnace> getTileEntityClass() {
        return TileEntityForgeFurnace.class;
    }

    public static void setState(boolean active, World worldIn, BlockPos pos){
        IBlockState iBlockState = worldIn.getBlockState(pos);
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        keepInventory = true;

        if(active)
        {
            worldIn.setBlockState(pos, Minecraftology.BLOCKS.mechanic_forge_furnace_lit.getDefaultState().withProperty(MCLBlock.PROPERTY_DIRECTION,iBlockState.getValue(MCLBlock.PROPERTY_DIRECTION)),3);
        }
        else
        {
            worldIn.setBlockState(pos, Minecraftology.BLOCKS.mechanic_forge_furnace.getDefaultState().withProperty(MCLBlock.PROPERTY_DIRECTION,iBlockState.getValue(MCLBlock.PROPERTY_DIRECTION)),3);
        }

        keepInventory = false;

        if(tileEntity != null) {
            tileEntity.validate();
            worldIn.setTileEntity(pos,tileEntity);
        }

    }


}
