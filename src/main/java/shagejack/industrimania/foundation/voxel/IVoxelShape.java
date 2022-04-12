package shagejack.industrimania.foundation.voxel;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

import java.util.BitSet;

public interface IVoxelShape {

    void initFilled();

    int getSize();

    void clear();

    /**
     * Check if the given position is not empty
     *
     * @param x The x offset, from to {@link #getSize()} excluding.
     * @param y The y offset, from to {@link #getSize()} excluding.
     * @param z The z offset, from to {@link #getSize()} excluding.
     * @return If the given position is not empty.
     */
    boolean exist(int x, int y, int z);

    /**
     * Check if the given position is not empty
     *
     * @param  coordinate The coordinate to check.
     * @return If the given position is not empty.
     */
    default boolean exist(Vec3i coordinate) {
        return exist(coordinate.getX(), coordinate.getY(), coordinate.getZ());
    }

    /**
     * Set the state of given position.
     * true: filled, false: empty
     *
     * @param x The x offset, from to {@link #getSize()} excluding.
     * @param y The y offset, from to {@link #getSize()} excluding.
     * @param z The z offset, from to {@link #getSize()} excluding.
     * @param filled the state to set.
     */
    void set(int x, int y, int z, boolean filled);

    default void set(Vec3i coordinate, boolean filled) {
        set(coordinate.getX(), coordinate.getY(), coordinate.getZ(), filled);
    }

    /**
     * Rotates the current voxel shape 90 degrees around the given axis with the given rotation count.
     *
     * @param axis          The axis to rotate around.
     * @param rotationCount The amount of times to rotate.
     */
    void rotate(Direction.Axis axis, int rotationCount);

    /**
     * Rotates the current voxel shape exactly once 90 degrees around the given axis.
     *
     * @param axis The axis to rotate around.
     */
    default void rotate(final Direction.Axis axis) {
        this.rotate(axis, 1);
    }

    /**
     * Mirrors the current voxel shape around the given axis.
     *
     * @param axis The axis to mirror over.
     */
    void mirror(Direction.Axis axis);

    BitSet getData();

    byte[] getRawData();

    IMVoxelShape copy();

}
