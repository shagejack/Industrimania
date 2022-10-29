package shagejack.industrimania.content.metallurgyAge.block.smeltery.bronzeTube;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.registries.AllTileEntities;

import java.util.List;

public class BronzeTubeTileEntity extends SmartTileEntity {

    //TODO: Smelter Initialization

    public BronzeTubeTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.bronze_tube.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {}


}
