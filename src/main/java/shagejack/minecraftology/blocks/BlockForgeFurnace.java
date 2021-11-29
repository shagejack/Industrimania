package shagejack.minecraftology.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.includes.MCLBlock;
import shagejack.minecraftology.blocks.includes.MCLBlockContainer;
import shagejack.minecraftology.blocks.includes.MCLBlockMachine;
import shagejack.minecraftology.data.Inventory;
import shagejack.minecraftology.tile.TileEntityClayFurnaceBottom;
import shagejack.minecraftology.tile.TileEntityForge;
import shagejack.minecraftology.tile.TileEntityForgeFurnace;
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
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
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
