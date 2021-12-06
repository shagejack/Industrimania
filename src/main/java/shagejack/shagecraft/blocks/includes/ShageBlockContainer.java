package shagejack.shagecraft.blocks.includes;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import shagejack.shagecraft.api.internal.TileEntityProvider;

public abstract class ShageBlockContainer<TE extends TileEntity> extends ShageBlock implements TileEntityProvider<TE> {

    public ShageBlockContainer(Material material, String name) {
        super(material, name);
        if (hasTileEntity(getDefaultState()) && TileEntity.getKey(getTileEntityClass()) == null)
            GameRegistry.registerTileEntity(getTileEntityClass(), new ResourceLocation("shagecraft:", name));
    }

    @Override
    public abstract Class<TE> getTileEntityClass();

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
