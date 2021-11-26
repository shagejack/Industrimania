package shagejack.minecraftology.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IMCLTileEntity {
    void onAdded(World world, BlockPos pos, IBlockState state);

    void onPlaced(World world, EntityLivingBase entityLiving);

    void onDestroyed(World worldIn, BlockPos pos, IBlockState state);

    void onNeighborBlockChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock);

    void writeToDropItem(ItemStack itemStack);

    void readFromPlaceItem(ItemStack itemStack);
}
