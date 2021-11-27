package shagejack.minecraftology.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.core.pattern.EqualsIgnoreCaseReplacementConverter;

public class MCLMultiBlockCheckHelper {

    public static int checkComplete(World world, BlockPos pos, String[] structure, BlockPos[] rPos) {
        boolean[] complete = {true, true, true, true};

        for (int j = 0; j < complete.length; j++) {
        for (int i = 0; i < structure.length; i++) {
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

    public static int checkComplete(World world, BlockPos pos, String[] structure, BlockPos[] rPos, boolean debug) {
        boolean[] complete = {true, true, true, true};

        for (int j = 0; j < complete.length; j++) {
        for (int i = 0; i < structure.length; i++) {
                if (!world.getBlockState(pos.add(getRotatedPos(rPos[i], j))).getBlock().getRegistryName().toString().equalsIgnoreCase(structure[i])) {
                    complete[j] = false;
                    if(debug) LogMCL.debug("Rotation Type [" + j + "] PosArr" + rPos[i] + " Wrong Block on" + pos.add(getRotatedPos(rPos[i], j)) + " which should be " + structure[i] + " but actually " + world.getBlockState(pos.add(getRotatedPos(rPos[i], j))).getBlock().getRegistryName().toString());
                }
            }
        }

        for(int i = 0; i < complete.length; i++){
            if (complete[i]) {
                if(debug) LogMCL.debug("Complete! Rotation Type = " + i);
                return i;
            }
            }

        if(debug) LogMCL.debug("Incomplete! Rotation Type = -1");
        return -1;

    }

    //Rotating Clockwise
    public static BlockPos getRotatedPos(BlockPos pos, int i){
        switch(i){
            case 0:
            case -1:
                break;
            case 1:
                pos = new BlockPos(pos.getZ(), pos.getY(), -pos.getX());
                break;
            case 2:
                pos = new BlockPos(-pos.getX(), pos.getY(), -pos.getZ());
                break;
            case 3:
                pos = new BlockPos(-pos.getZ(), pos.getY(), pos.getX());
                break;
        }

    return pos;

    }

}
