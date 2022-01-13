package shagejack.industrimania.content.primalAge.item.itemPlaceable.woodPlaceable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.registers.AllTileEntities;

public class BlockWoodPlaceableBase extends Block implements ITE<WoodPlaceableTileEntity> {

    public BlockWoodPlaceableBase(Properties properties) {
        super(properties);
    }

    @Override
    public Class<WoodPlaceableTileEntity> getTileEntityClass() {
        return WoodPlaceableTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends WoodPlaceableTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends WoodPlaceableTileEntity>) AllTileEntities.wood_placeable.get();
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState state, boolean p_48717_) {
        BlockEntity te = level.getBlockEntity(pos);
        if (te instanceof WoodPlaceableTileEntity) {
            ((WoodPlaceableTileEntity) te).onBreak(level);
        }
        super.onRemove(oldState, level, pos, state, p_48717_);
    }


}
