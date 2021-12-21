package shagejack.shagecraft.content.metallurgy.block.smeltery.clayFurnace;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import shagejack.shagecraft.foundation.block.ITE;

public class ClayFurnaceBottomBlock extends Block implements ITE<ClayFurnaceBottomTileEntity> {

    public ClayFurnaceBottomBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<ClayFurnaceBottomTileEntity> getTileEntityClass() {
        return null;
    }

    @Override
    public BlockEntityType<? extends ClayFurnaceBottomTileEntity> getTileEntityType() {
        return null;
    }


}
