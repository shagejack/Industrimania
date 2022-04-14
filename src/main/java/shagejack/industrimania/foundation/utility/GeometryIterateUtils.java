package shagejack.industrimania.foundation.utility;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class GeometryIterateUtils {

    private GeometryIterateUtils() {
        throw new IllegalStateException(this.getClass().toString() + "should not be instantiated as it's a utility class.");
    }

    public static boolean isInEllipsoid(int dx, int dy, int dz, BlockPos center, int rx2, int ry2, int rz2) {
        return Math.pow(dx - center.getX(), 2) / rx2 + Math.pow(dy - center.getY(), 2) / ry2 + Math.pow(dz - center.getZ(), 2) / rz2 <= 1;
    }

    public static boolean isInEllipse(int dx, int dz, BlockPos center, int rx2, int rz2) {
        return Math.pow(dx - center.getX(), 2) / rx2 + Math.pow(dz - center.getZ(), 2) / rz2 <= 1;
    }

    public static boolean isInCylinder(int dx, int dy, int dz, BlockPos center, int rx2, int height, int rz2) {
        return Math.pow(dx - center.getX(), 2) / rx2 + Math.pow(dz - center.getZ(), 2) / rz2 <= 1 && dy > center.getY() - height / 2 && dy < center.getY() + height / 2;
    }

    /**
     * get other seven pos from one pos of a symmetrical shape.
     */
    public static BlockPos getPosInOtherQuadrant(BlockPos pos, BlockPos center, int index) {
        int nX = 2 * center.getX() - pos.getX();
        int nY = 2 * center.getY() - pos.getY();
        int nZ = 2 * center.getZ() - pos.getZ();
        switch(index) {
            case 0 -> {return new BlockPos(nX, pos.getY(), pos.getZ());}
            case 1 -> {return new BlockPos(nX, pos.getY(), nZ);}
            case 2 -> {return new BlockPos(nX, nY, pos.getZ());}
            case 3 -> {return new BlockPos(nX, nY, nZ);}
            case 4 -> {return new BlockPos(pos.getX(), pos.getY(), nZ);}
            case 5 -> {return new BlockPos(pos.getX(), nY, pos.getZ());}
            case 6 -> {return new BlockPos(pos.getX(), nY, nZ);}
        }

        return pos;
    }

    /**
     * get all other seven pos from one pos of a symmetrical shape.
     */
    public static List<BlockPos> getPosListInOtherQuadrant(BlockPos pos, BlockPos center, int index) {
        int nX = 2 * center.getX() - pos.getX();
        int nY = 2 * center.getY() - pos.getY();
        int nZ = 2 * center.getZ() - pos.getZ();

        return List.of(
            new BlockPos(nX, pos.getY(), pos.getZ()),
            new BlockPos(nX, pos.getY(), nZ),
            new BlockPos(nX, nY, pos.getZ()),
            new BlockPos(nX, nY, nZ),
            new BlockPos(pos.getX(), pos.getY(), nZ),
            new BlockPos(pos.getX(), nY, pos.getZ()),
            new BlockPos(pos.getX(), nY, nZ)
         );
    }

}
