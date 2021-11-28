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
            //
        }
        else
        {
            //
        }
        if(tileEntity != null) {
            tileEntity.validate();
            worldIn.setTileEntity(pos,tileEntity);
        }
    }
}

