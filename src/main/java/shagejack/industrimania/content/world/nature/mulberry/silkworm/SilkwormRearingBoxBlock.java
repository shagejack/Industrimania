package shagejack.industrimania.content.world.nature.mulberry.silkworm;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.registries.AllTileEntities;

public class SilkwormRearingBoxBlock extends Block implements ITE<SilkwormRearingBoxTileEntity> {
    public SilkwormRearingBoxBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<SilkwormRearingBoxTileEntity> getTileEntityClass() {
        return SilkwormRearingBoxTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends SilkwormRearingBoxTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends SilkwormRearingBoxTileEntity>) AllTileEntities.silkworm_rearing_box.get();
    }
}
