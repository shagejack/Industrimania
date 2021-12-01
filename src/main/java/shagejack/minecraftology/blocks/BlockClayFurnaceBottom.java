package shagejack.minecraftology.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.includes.MCLBlock;
import shagejack.minecraftology.blocks.includes.MCLBlockContainer;
import shagejack.minecraftology.tile.TileEntityClayFurnaceBottom;
import shagejack.minecraftology.util.MCLBlockHelper;
import shagejack.minecraftology.util.MCLStringHelper;
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
        TileEntityClayFurnaceBottom tileEntity = (TileEntityClayFurnaceBottom) worldIn.getTileEntity(pos);
        if (tileEntity == null) {
            return false;
        }
        if(playerIn.getHeldItem(hand).getItem() == Minecraftology.ITEMS.multimeter) {
            tileEntity.debugCheckComplete();
            playerIn.sendMessage(new TextComponentString("\u6e29\u5ea6\uff1a" + MCLStringHelper.formatNumber(tileEntity.getTemperature()) + "K"));
            playerIn.sendMessage(new TextComponentString("\u6c27\u6c14\u6d41\u91cf\uff1a" + MCLStringHelper.formatNumber(tileEntity.getOxygenFlow()) + "mol/t"));
            return true;
        }

        if(tileEntity.checkComplete(worldIn) == -1) {
            if (playerIn.getHeldItem(hand).getItem() == Minecraftology.ITEMS.gloves) {
                double temp = tileEntity.getTemperature() - 15.0D + 30 * worldIn.rand.nextDouble();
                playerIn.sendMessage(new TextComponentString("\u6478\u8d77\u6765\u6e29\u5ea6\u5927\u6982\u662f\u2026\u2026" + MCLStringHelper.formatNumber(temp) + "K"));
            } else {
                if (tileEntity.getTemperature() < 423.15) {
                    double temp = tileEntity.getTemperature() - 25.0D + 50 * worldIn.rand.nextDouble();
                    playerIn.sendMessage(new TextComponentString("\u6478\u8d77\u6765\u6e29\u5ea6\u5927\u6982\u662f\u2026\u2026" + MCLStringHelper.formatNumber(temp) + "K"));
                } else {
                    playerIn.attackEntityFrom(DamageSource.ON_FIRE, 2);
                    playerIn.sendMessage(new TextComponentString("\u4e0d\u884c\uff0c\u592a\u70eb\u4e86\uff01"));
                }
            }
        } else {
            playerIn.sendMessage(new TextComponentString("\u6839\u672c\u6ca1\u70b9\u71c3\uff0c\u6478\u8d77\u6765\u5c31\u50cf\u662f\u5ba4\u6e29\uff0c\u0032\u0037\u0033\u002e\u0031\u0035\u004b"));
        }

        return true;
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
