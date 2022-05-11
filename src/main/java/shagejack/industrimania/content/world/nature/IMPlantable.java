package shagejack.industrimania.content.world.nature;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import shagejack.industrimania.content.world.nature.rush.RushBlockBottom;

public interface IMPlantable extends IPlantable
{
    @Override
    default PlantType getPlantType(BlockGetter world, BlockPos pos) {
        if (this instanceof RushBlockBottom)      return PlantType.PLAINS;
        return net.minecraftforge.common.PlantType.PLAINS;
    }

    BlockState getPlant(BlockGetter world, BlockPos pos);
}
