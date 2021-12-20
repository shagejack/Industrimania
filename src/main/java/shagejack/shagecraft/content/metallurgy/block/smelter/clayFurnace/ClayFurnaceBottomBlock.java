package shagejack.shagecraft.content.metallurgy.block.smelter.clayFurnace;

import net.minecraft.world.level.block.entity.BlockEntityType;
import shagejack.shagecraft.foundation.block.ITE;

public class ClayFurnaceBottomBlock implements ITE<ClayFurnaceBottomTileEntity> {

    @Override
    public Class<ClayFurnaceBottomTileEntity> getTileEntityClass() {
        return null;
    }

    @Override
    public BlockEntityType<? extends ClayFurnaceBottomTileEntity> getTileEntityType() {
        return null;
    }
}
