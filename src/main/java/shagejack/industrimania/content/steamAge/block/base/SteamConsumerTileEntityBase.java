package shagejack.industrimania.content.steamAge.block.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.content.steamAge.steam.ISteamStorageUpdater;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;

import java.util.List;

public abstract class SteamConsumerTileEntityBase extends SmartTileEntity implements ISteamStorageUpdater {

    public SteamConsumerTileEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void onTankContentsChanged() {

    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }
}
