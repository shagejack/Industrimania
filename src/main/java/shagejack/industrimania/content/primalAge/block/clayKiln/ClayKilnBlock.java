package shagejack.industrimania.content.primalAge.block.clayKiln;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.registers.AllTileEntities;

public class ClayKilnBlock extends Block implements ITE<ClayKilnTileEntity> {

    public ClayKilnBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<ClayKilnTileEntity> getTileEntityClass() {
        return ClayKilnTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends ClayKilnTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends ClayKilnTileEntity>) AllTileEntities.clay_kiln.get();
    }

}
