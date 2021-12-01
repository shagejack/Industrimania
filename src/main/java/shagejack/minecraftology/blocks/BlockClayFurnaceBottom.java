package shagejack.minecraftology.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.includes.MCLBlock;
import shagejack.minecraftology.blocks.includes.MCLBlockContainer;
import shagejack.minecraftology.tile.TileEntityClayFurnaceBottom;
import shagejack.minecraftology.util.MCLBlockHelper;
import shagejack.minecraftology.util.MachineHelper;

import java.util.Random;

public class BlockClayFurnaceBottom extends MCLBlockContainer<TileEntityClayFurnaceBottom> {

    public BlockClayFurnaceBottom(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.GROUND);
        setResistance(4.0F);
    }

    /* DEBUG
     */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn.getHeldItem(hand).getItem() == Minecraftology.ITEMS.multimeter) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityClayFurnaceBottom) {
                ((TileEntityClayFurnaceBottom) tileEntity).debugCheckComplete();
            }
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState p_180655_1_, World p_180655_2_, BlockPos p_180655_3_, Random p_180655_4_) {
        TileEntityClayFurnaceBottom tileEntity = (TileEntityClayFurnaceBottom) p_180655_2_.getTileEntity(p_180655_3_);
        if (tileEntity == null){
            return;
        }

        if(!tileEntity.isBurning()){
            return;
        }

        double lvt_6_1_ = (double)p_180655_3_.getX() + 0.5D;
        double lvt_8_1_ = (double)p_180655_3_.getY() + p_180655_4_.nextDouble() * 6.0D / 16.0D;
        double lvt_10_1_ = (double)p_180655_3_.getZ() + 0.5D;
        double rand_1 = p_180655_4_.nextDouble() * 0.6D - 0.3D;
        double rand_2 = p_180655_4_.nextDouble() * 0.6D - 0.3D;
        if (p_180655_4_.nextDouble() < 0.2D) {
            p_180655_2_.playSound((double)p_180655_3_.getX() + 0.5D, (double)p_180655_3_.getY(), (double)p_180655_3_.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }

        p_180655_2_.spawnParticle(EnumParticleTypes.SMOKE_LARGE, lvt_6_1_ + rand_1, lvt_8_1_ + 5.0D, lvt_10_1_ + rand_2, 0.0D, 0.0D, 0.0D, new int[0]);
        p_180655_2_.spawnParticle(EnumParticleTypes.FLAME, lvt_6_1_ + rand_1, lvt_8_1_ + 5.0D, lvt_10_1_+ rand_2, 0.0D, 0.0D, 0.0D, new int[0]);
        p_180655_2_.spawnParticle(EnumParticleTypes.SMOKE_LARGE, lvt_6_1_ + rand_1, lvt_8_1_ + 4.0D, lvt_10_1_+ rand_2, 0.0D, 0.0D, 0.0D, new int[0]);
        p_180655_2_.spawnParticle(EnumParticleTypes.FLAME, lvt_6_1_ + rand_1, lvt_8_1_ + 4.0D, lvt_10_1_+ rand_2, 0.0D, 0.0D, 0.0D, new int[0]);
    }


    @Override
    public Class<TileEntityClayFurnaceBottom> getTileEntityClass() {
        return TileEntityClayFurnaceBottom.class;
    }

}
