package shagejack.minecraftology.blocks.includes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import shagejack.minecraftology.Minecraftology;

public class BlockStateHelper {

    public static void setState(boolean active, World worldIn, BlockPos pos){
        IBlockState iBlockState = worldIn.getBlockState(pos);
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if(active)
        {
            worldIn.setBlockState(pos, Minecraftology.BLOCKS.mechanic_forge_furnace_lit.getDefaultState().withProperty(MCLBlock.PROPERTY_DIRECTION,iBlockState.getValue(MCLBlock.PROPERTY_DIRECTION)),3);
        }
        else
        {
            worldIn.setBlockState(pos, Minecraftology.BLOCKS.mechanic_forge_furnace.getDefaultState().withProperty(MCLBlock.PROPERTY_DIRECTION,iBlockState.getValue(MCLBlock.PROPERTY_DIRECTION)),3);
        }
        if(tileEntity != null) {
            tileEntity.validate();
            worldIn.setTileEntity(pos,tileEntity);
        }
    }
}

