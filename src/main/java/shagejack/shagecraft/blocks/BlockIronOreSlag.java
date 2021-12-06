package shagejack.shagecraft.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import shagejack.shagecraft.blocks.includes.ShageBlockContainer;
import shagejack.shagecraft.tile.TileEntityIronOreSlag;

import java.util.Random;

public class BlockIronOreSlag extends ShageBlockContainer<TileEntityIronOreSlag> {

    public BlockIronOreSlag(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.GROUND);
        setHarvestLevel("shovel", 1);
        setHardness(4.0F);
        setResistance(4.0F);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityIronOreSlag) {
            ((TileEntityIronOreSlag) tileEntity).onBreak(worldIn);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public Class<TileEntityIronOreSlag> getTileEntityClass() {
        return TileEntityIronOreSlag.class;
    }

}
