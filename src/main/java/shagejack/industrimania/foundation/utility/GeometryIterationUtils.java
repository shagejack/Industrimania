package shagejack.industrimania.foundation.utility;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GeometryIterationUtils {

    private GeometryIterationUtils() {
        throw new IllegalStateException(this.getClass().toString() + "should not be instantiated as it's a utility class.");
    }

    public static boolean isInEllipsoid(int dx, int dy, int dz, BlockPos center, double rx2, double ry2, double rz2) {
        return Math.pow(dx - center.getX(), 2) / rx2 + Math.pow(dy - center.getY(), 2) / ry2 + Math.pow(dz - center.getZ(), 2) / rz2 <= 1;
    }

    public static boolean isInEllipsoid(BlockPos pos, BlockPos center, double rx2, double ry2, double rz2) {
        return Math.pow(pos.getX() - center.getX(), 2) / rx2 + Math.pow(pos.getY() - center.getY(), 2) / ry2 + Math.pow(pos.getZ() - center.getZ(), 2) / rz2 <= 1;
    }

    public static boolean isInEllipse(int dx, int dz, BlockPos center, double rx2, double rz2) {
        return Math.pow(dx - center.getX(), 2) / rx2 + Math.pow(dz - center.getZ(), 2) / rz2 <= 1;
    }

    public static boolean isInEllipse(BlockPos pos, BlockPos center, double rx2, double rz2) {
        return Math.pow(pos.getX() - center.getX(), 2) / rx2 + Math.pow(pos.getZ() - center.getZ(), 2) / rz2 <= 1;
    }

    public static boolean isInCylinder(int dx, int dy, int dz, BlockPos center, double rx2, int height, double rz2) {
        return Math.pow(dx - center.getX(), 2) / rx2 + Math.pow(dz - center.getZ(), 2) / rz2 <= 1 && dy > center.getY() - height / 2 && dy < center.getY() + height / 2;
    }

    public static boolean isInCylinder(BlockPos pos, BlockPos center, double rx2, int height, double rz2) {
        return Math.pow(pos.getX() - center.getX(), 2) / rx2 + Math.pow(pos.getZ() - center.getZ(), 2) / rz2 <= 1 && pos.getY() > center.getY() - height / 2 && pos.getY() < center.getY() + height / 2;
    }

    public static Stream<BlockPos> getEllipsoidStream(BlockPos center, int rx, int ry, int rz) {
        double rx2 = Math.pow(rx, 2);
        double ry2 = Math.pow(ry, 2);
        double rz2 = Math.pow(rz, 2);
        return BlockPos.betweenClosedStream(center, center.offset(rx, ry, rz)).parallel()
                .filter(pos -> isInEllipsoid(pos, center, rx2, ry2, rz2))
                .flatMap(pos -> getSymmetricPosStream(center, pos));
    }

    public static Stream<BlockPos> getEllipseStream(BlockPos center, int rx, int rz) {
        double rx2 = Math.pow(rx, 2);
        double rz2 = Math.pow(rz, 2);
        return BlockPos.betweenClosedStream(center.offset(-rx, 0, -rz), center.offset(rx, 0, rz)).parallel()
                .filter(pos -> isInEllipse(pos, center, rx2, rz2));
    }

    public static Stream<BlockPos> getCylinderStream(BlockPos center, int rx, int height, int rz) {
        return getEllipseStream(center, rx, rz).parallel().flatMap(pos -> getHeightStream(center, pos, height));
    }

    public static Stream<BlockPos> getSymmetricPosStream(BlockPos center, BlockPos pos) {
        int dx = center.getX() - pos.getX(), dy = center.getY() - pos.getY(), dz = center.getZ() - pos.getZ();
        return Stream.of(
                pos,
                center.offset(-dx, dy, dz),
                center.offset(dx, -dy, dz),
                center.offset(dx, dy, -dz),
                center.offset(-dx, -dy, dz),
                center.offset(dx, -dy, -dz),
                center.offset(-dx, dy, -dz),
                center.offset(-dx, -dy, -dz)
        );
    }

    public static Stream<BlockPos> getHeightStream(BlockPos center, BlockPos pos, int height) {
        int dx = center.getX() - pos.getX(), dz = center.getZ() - pos.getZ();
        return IntStream.range(center.getY() - height / 2, center.getY() + height / 2 + 1)
                .mapToObj(dy -> new BlockPos(dx, dy, dz));
    }

}
