package shagejack.minecraftology.blocks.includes;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import shagejack.minecraftology.api.internal.TileEntityProvider;

public abstract class MCLBlockContainer<TE extends TileEntity> extends MCLBlock implements TileEntityProvider<TE> {
    public MCLBlockContainer(Material material, String name) {
        super(material, name);
        if (hasTileEntity(getDefaultState()) && TileEntity.getKey(getTileEntityClass()) == null)
            GameRegistry.registerTileEntity(getTileEntityClass(), new ResourceLocation("minecraftology:", name));
    }

    @Override
    public abstract Class<TE> getTileEntityClass();

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
