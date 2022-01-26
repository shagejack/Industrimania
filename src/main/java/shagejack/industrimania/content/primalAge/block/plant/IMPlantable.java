package shagejack.industrimania.content.primalAge.block.plant;

import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import shagejack.industrimania.content.primalAge.block.plant.rush.RushBlockBottom;

public interface IMPlantable extends IPlantable
{
    @Override
    default PlantType getPlantType(BlockGetter world, BlockPos pos) {
        if (this instanceof RushBlockBottom)      return PlantType.PLAINS;
        return net.minecraftforge.common.PlantType.PLAINS;
    }

    BlockState getPlant(BlockGetter world, BlockPos pos);
}
