package shagejack.shagecraft.content.metallurgy.block.smeltery.ironOreSlag;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
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

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState state, boolean p_48717_) {
        BlockEntity te = level.getBlockEntity(pos);
        if (te instanceof IronOreSlagTileEntity) {
            ((IronOreSlagTileEntity) te).onBreak(level);
        }
        super.onRemove(oldState, level, pos, state, p_48717_);
    }


}
