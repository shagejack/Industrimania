package shagejack.industrimania.content.world.nature.rubberTree;

import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.registers.AllTileEntities;

public class RubberTreeLog extends RotatedPillarBlock implements ITE<RubberTreeLogTileEntity> {

    public RubberTreeLog(Properties properties) {
        super(properties);
    }

    @Override
    public Class<RubberTreeLogTileEntity> getTileEntityClass() {
        return RubberTreeLogTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends RubberTreeLogTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends RubberTreeLogTileEntity>) AllTileEntities.rubber_tree_log.get();
    }

}
