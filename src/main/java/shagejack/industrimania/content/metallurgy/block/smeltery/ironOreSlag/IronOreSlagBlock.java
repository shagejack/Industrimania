package shagejack.industrimania.content.metallurgy.block.smeltery.ironOreSlag;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.registers.AllTileEntities;

public class IronOreSlagBlock extends Block implements ITE<IronOreSlagTileEntity> {

    public IronOreSlagBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<IronOreSlagTileEntity> getTileEntityClass() {
        return IronOreSlagTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends IronOreSlagTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends IronOreSlagTileEntity>) AllTileEntities.iron_ore_slag.get();
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
