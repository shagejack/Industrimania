package shagejack.shagecraft.content.metallurgy.block.smeltery.ironOreSlag;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.shagecraft.content.metallurgy.block.smeltery.clayFurnace.ClayFurnaceBottomTileEntity;
import shagejack.shagecraft.foundation.block.ITE;

public class IronOreSlagBlock extends Block implements ITE<IronOreSlagTileEntity> {

    public IronOreSlagBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<IronOreSlagTileEntity> getTileEntityClass() {
        return null;
    }

    @Override
    public BlockEntityType<? extends IronOreSlagTileEntity> getTileEntityType() {
        return null;
    }


}
