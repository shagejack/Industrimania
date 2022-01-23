package shagejack.industrimania.content.primalAge.item.itemPlaceable.woodPlaceable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableBaseTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.registers.AllTileEntities;

import java.util.List;

public class WoodPlaceableTileEntity extends ItemPlaceableBaseTileEntity {

    public WoodPlaceableTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.wood_placeable.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {}

    //TODO: burn
    public void burn() {

    }

}
