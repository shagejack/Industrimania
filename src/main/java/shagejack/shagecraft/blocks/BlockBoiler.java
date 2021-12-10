package shagejack.shagecraft.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shagejack.shagecraft.blocks.includes.ShageBlockMachine;
import shagejack.shagecraft.tile.TileEntityMachineBoiler;

import javax.annotation.Nonnull;

public class BlockBoiler extends ShageBlockMachine<TileEntityMachineBoiler> {

    public BlockBoiler(Material material, String name) {
        super(material, name);
        setHasRotation();
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        setHasGui(true);
    }


    @Override
    public Class<TileEntityMachineBoiler> getTileEntityClass() {
        return TileEntityMachineBoiler.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState meta) {
        return new TileEntityMachineBoiler();
    }
}
