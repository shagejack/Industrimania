package shagejack.shagecraft.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shagejack.shagecraft.tile.pipes.TileEntitySteamPipe;

import javax.annotation.Nonnull;

public class BlockSteamPipe extends BlockPipe<TileEntitySteamPipe> {

    public BlockSteamPipe(Material material, String name) {
        super(material, name);
        setHardness(10.0F);
        this.setResistance(5.0f);
    }


    @Override
    public Class<TileEntitySteamPipe> getTileEntityClass() {
        return TileEntitySteamPipe.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntitySteamPipe();
    }
}
