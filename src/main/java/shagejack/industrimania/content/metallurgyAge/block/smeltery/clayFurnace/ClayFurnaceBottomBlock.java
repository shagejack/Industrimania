package shagejack.industrimania.content.metallurgyAge.block.smeltery.clayFurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.registers.AllTileEntities;

public class ClayFurnaceBottomBlock extends Block implements ITE<ClayFurnaceBottomTileEntity> {

    public ClayFurnaceBottomBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<ClayFurnaceBottomTileEntity> getTileEntityClass() {
        return ClayFurnaceBottomTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends ClayFurnaceBottomTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends ClayFurnaceBottomTileEntity>) AllTileEntities.clay_furnace_bottom.get();
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState state, boolean p_48717_) {
        BlockEntity te = level.getBlockEntity(pos);
        if (te instanceof ClayFurnaceBottomTileEntity) {
            ((ClayFurnaceBottomTileEntity) te).onDestroyed(level, pos, oldState);
        }
        super.onRemove(oldState, level, pos, state, p_48717_);
    }


}
