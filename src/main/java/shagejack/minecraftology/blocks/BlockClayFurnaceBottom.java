package shagejack.minecraftology.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.includes.MCLBlock;
import shagejack.minecraftology.blocks.includes.MCLBlockContainer;
import shagejack.minecraftology.tile.TileEntityClayFurnaceBottom;
import shagejack.minecraftology.util.MCLBlockHelper;
import shagejack.minecraftology.util.MachineHelper;

public class BlockClayFurnaceBottom extends MCLBlockContainer<TileEntityClayFurnaceBottom> {

    public BlockClayFurnaceBottom(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.GROUND);
        setResistance(4.0F);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityClayFurnaceBottom) {
            ((TileEntityClayFurnaceBottom) tileEntity).debugCheckComplete();
        }
        return true;
    }

    @Override
    public Class<TileEntityClayFurnaceBottom> getTileEntityClass() {
        return TileEntityClayFurnaceBottom.class;
    }

}
