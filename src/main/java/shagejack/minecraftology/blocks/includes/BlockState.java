package shagejack.minecraftology.blocks.includes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockState {

    public static void setState(boolean active, World worldIn, BlockPos pos){
        IBlockState iBlockState = worldIn.getBlockState(pos);
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        //把这里的Facing改为响应面的property
        if(active)
        {
            worldIn.setBlockState(pos,Blocks.LIT_FURNACE.getDefaultState().withProperty(Facing,iBlockState.(Facing)),3);
            worldIn.setBlockState(pos, Blocks.LIT_FURNACE.getDefaultState().withProperty(Facing,iBlockState.getValue(Facing)),3);
        }
        else
        {
            worldIn.setBlockState(pos,Blocks.FURNACE.getDefaultState().withProperty(Facing,iBlockState.getValue(Facing)),3);
            worldIn.setBlockState(pos,Blocks.FURNACE.getDefaultState().withProperty(Facing,iBlockState.getValue(Facing)),3);
        }
        if(tileEntity != null) {
            tileEntity.validate();
            worldIn.setTileEntity(pos,tileEntity);
        }
    }
}

