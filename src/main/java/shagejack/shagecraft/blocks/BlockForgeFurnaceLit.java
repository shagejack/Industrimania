package shagejack.shagecraft.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.shagecraft.blocks.includes.ShageBlock;
import shagejack.shagecraft.util.ShageBlockHelper;

import java.util.Random;

public class BlockForgeFurnaceLit extends BlockForgeFurnace {

    public BlockForgeFurnaceLit(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);
        setHardness(4.0F);
        setResistance(4.0F);
        setLightLevel(4.0F);
        setHasGui(true);
        setHasRotation();
        setRotationType(ShageBlockHelper.RotationType.FOUR_WAY);
        setCreativeTab(null);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState p_180655_1_, World p_180655_2_, BlockPos p_180655_3_, Random p_180655_4_) {
            EnumFacing lvt_5_1_ = (EnumFacing) p_180655_1_.getValue(ShageBlock.PROPERTY_DIRECTION);
            double lvt_6_1_ = (double)p_180655_3_.getX() + 0.5D;
            double lvt_8_1_ = (double)p_180655_3_.getY() + p_180655_4_.nextDouble() * 6.0D / 16.0D;
            double lvt_10_1_ = (double)p_180655_3_.getZ() + 0.5D;
            double lvt_12_1_ = 0.52D;
            double lvt_14_1_ = p_180655_4_.nextDouble() * 0.6D - 0.3D;
            if (p_180655_4_.nextDouble() < 0.1D) {
                p_180655_2_.playSound((double)p_180655_3_.getX() + 0.5D, (double)p_180655_3_.getY(), (double)p_180655_3_.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            switch(lvt_5_1_) {
                case WEST:
                    p_180655_2_.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, lvt_6_1_ - 0.52D, lvt_8_1_, lvt_10_1_ + lvt_14_1_, 0.0D, 0.0D, 0.0D, new int[0]);
                    p_180655_2_.spawnParticle(EnumParticleTypes.FLAME, lvt_6_1_ - 0.52D, lvt_8_1_, lvt_10_1_ + lvt_14_1_, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case EAST:
                    p_180655_2_.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, lvt_6_1_ + 0.52D, lvt_8_1_, lvt_10_1_ + lvt_14_1_, 0.0D, 0.0D, 0.0D, new int[0]);
                    p_180655_2_.spawnParticle(EnumParticleTypes.FLAME, lvt_6_1_ + 0.52D, lvt_8_1_, lvt_10_1_ + lvt_14_1_, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case NORTH:
                    p_180655_2_.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, lvt_6_1_ + lvt_14_1_, lvt_8_1_, lvt_10_1_ - 0.52D, 0.0D, 0.0D, 0.0D, new int[0]);
                    p_180655_2_.spawnParticle(EnumParticleTypes.FLAME, lvt_6_1_ + lvt_14_1_, lvt_8_1_, lvt_10_1_ - 0.52D, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case SOUTH:
                    p_180655_2_.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, lvt_6_1_ + lvt_14_1_, lvt_8_1_, lvt_10_1_ + 0.52D, 0.0D, 0.0D, 0.0D, new int[0]);
                    p_180655_2_.spawnParticle(EnumParticleTypes.FLAME, lvt_6_1_ + lvt_14_1_, lvt_8_1_, lvt_10_1_ + 0.52D, 0.0D, 0.0D, 0.0D, new int[0]);
            }
    }

}
