package shagejack.minecraftology.tile;

import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface IMultiBlockTileEntity {

    List<BlockPos> getBoundingBlocks();

}

