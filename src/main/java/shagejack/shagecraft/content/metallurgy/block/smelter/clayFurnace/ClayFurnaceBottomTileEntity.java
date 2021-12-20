package shagejack.shagecraft.content.metallurgy.block.smelter.clayFurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.shagecraft.foundation.tileEntity.SmartTileEntity;
import shagejack.shagecraft.foundation.tileEntity.TileEntityBehaviour;

import java.util.List;

public class ClayFurnaceBottomTileEntity extends SmartTileEntity {

    public ClayFurnaceBottomTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityType.FURNACE, pos, state);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    //TODO
}
