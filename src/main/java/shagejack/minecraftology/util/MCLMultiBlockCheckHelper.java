package shagejack.minecraftology.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MCLMultiBlockCheckHelper {

    public static int checkComplete(World world, BlockPos pos, String[] structure, BlockPos[] rPos) {
        boolean[] complete = {true, true, true, true};

        for (int i = 0; i < structure.length; i++) {
            for (int j = 0; j < complete.length; j++) {
                if (!world.getBlockState(pos.add(getRotatedPos(rPos[i], j))).getBlock().getRegistryName().toString().equalsIgnoreCase(structure[i])) {
                    complete[j] = false;
                }
            }
        }

        for(int i = 0; i < complete.length; i++){
            if (complete[i]) return i;
        }

        return -1;

    }

    //Rotating Clockwise
    public static BlockPos getRotatedPos(BlockPos pos, int i){
        switch(i){
            case 0:
                break;
            case 1:
                pos = new BlockPos(pos.getY(), pos.getY(), -pos.getX());
                break;
            case 2:
                pos = new BlockPos(-pos.getX(), pos.getY(), -pos.getY());
                break;
            case 3:
                pos = new BlockPos(-pos.getY(), pos.getY(), pos.getY());
                break;
        }

    return pos;

    }

}
