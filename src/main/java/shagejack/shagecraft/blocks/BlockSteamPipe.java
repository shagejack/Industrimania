package shagejack.shagecraft.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shagejack.shagecraft.tile.pipes.TileEntitySteamPipe;

import javax.annotation.Nonnull;

public class BlockSteamPipe extends BlockPipe<TileEntitySteamPipe> {
    protected double capacity;

    public BlockSteamPipe(Material material, String name, double capacity) {
        super(material, name);
        setHardness(10.0F);
        this.setResistance(5.0f);
        this.setCapacity(capacity);
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    @Override
    public Class<TileEntitySteamPipe> getTileEntityClass() {
        return TileEntitySteamPipe.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntitySteamPipe(capacity);
    }
}
