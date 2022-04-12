package shagejack.industrimania.foundation.voxel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import shagejack.industrimania.foundation.utility.ByteArrayUtils;
import shagejack.industrimania.foundation.utility.VecUtils;

import java.util.BitSet;

public class IMVoxelShape implements IVoxelShape, INBTSerializable<CompoundTag> {

    private static final int entryWidth = 1;
    public static final String DataKey = "VoxelShapeData";
    public static final IMVoxelShape EMPTY = new IMVoxelShape();

    private final int size;

    private BitSet data;
    private boolean isDeserializing;

    public IMVoxelShape() {
        this(64);
    }

    public IMVoxelShape(int size) {
        this.size = size;
        this.data = new BitSet();
        this.isDeserializing = false;
    }

    private IMVoxelShape(final IMVoxelShape stateEntryStorage) {
        this.size = stateEntryStorage.size;
        this.data = BitSet.valueOf(stateEntryStorage.getData().toLongArray()); // Automatically copies the data.
        this.isDeserializing = false;
    }

    @Override
    public void initFilled() {
        clear();
        this.data = ByteArrayUtils.fill(1, entryWidth, getTotalEntryCount());
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public void clear() {
        this.data = new BitSet();
    }

    @Override
    public BitSet getData() {
        return this.data;
    }

    @Override
    public byte[] getRawData() {
        return this.data.toByteArray();
    }

    @Override
    public IMVoxelShape copy() {
        return new IMVoxelShape(this);
    }

    @Override
    public boolean exist(int x, int y, int z) {
        final int offSetIndex = doCalculatePositionIndex(x, y, z);
        return ByteArrayUtils.getValueAt(data, entryWidth, offSetIndex) == 1;
    }

    @Override
    public void set(int x, int y, int z, boolean filled) {
        final int offSetIndex = doCalculatePositionIndex(x, y, z);

        ensureCapacity();

        ByteArrayUtils.setValueAt(data, filled ? 1 : 0, entryWidth, offSetIndex);
    }

    @Override
    public void rotate(final Direction.Axis axis, final int rotationCount)
    {
        if (rotationCount == 0)
            return;

        final IMVoxelShape copy = this.copy();
        clear();

        final Vec3 centerVector = new Vec3((size - 1) / 2D, (size - 1) / 2D, (size - 1) / 2D);

        for (int x = 0; x < size; x++)
        {
            for (int y = 0; y < size; y++)
            {
                for (int z = 0; z < size; z++)
                {
                    final Vec3 workingVector = new Vec3(x, y, z);
                    Vec3 rotatedVector = workingVector.subtract(centerVector);
                    for (int i = 0; i < rotationCount; i++)
                    {
                        rotatedVector = VecUtils.rotate90Degrees(rotatedVector, axis);
                    }

                    final BlockPos sourcePos = new BlockPos(workingVector);
                    final Vec3 offsetPos = rotatedVector.add(centerVector).multiply(1000,1000,1000);
                    final BlockPos targetPos = new BlockPos(new Vec3(Math.round(offsetPos.x()), Math.round(offsetPos.y()), Math.round(offsetPos.z())).multiply(1/1000d,1/1000d,1/1000d));

                    this.set(
                            targetPos.getX(),
                            targetPos.getY(),
                            targetPos.getZ(),
                            copy.exist(
                                    sourcePos.getX(),
                                    sourcePos.getY(),
                                    sourcePos.getZ()
                            )
                    );
                }
            }
        }
    }

    @Override
    public void mirror(final Direction.Axis axis)
    {
        final IMVoxelShape copy = this.copy();
        clear();

        for (int y = 0; y < size; y++)
        {
            for (int x = 0; x < size; x++)
            {
                for (int z = 0; z < size; z++)
                {

                    final int mirroredX = axis == Direction.Axis.X ? (size - x - 1) : x;
                    final int mirroredY = axis == Direction.Axis.Y ? (size - y - 1) : y;
                    final int mirroredZ = axis == Direction.Axis.Z ? (size - z - 1) : z;

                    this.set(
                            mirroredX, mirroredY, mirroredZ,
                            copy.exist(x, y, z)
                    );
                }
            }
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag result = new CompoundTag();

        result.putByteArray(DataKey, this.getRawData());

        return result;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt == null)
            return;

        clear();

        this.isDeserializing = true;

        this.data = BitSet.valueOf(nbt.getByteArray(DataKey));

        this.isDeserializing = false;
    }

    private int doCalculatePositionIndex(final int x, final int y, final int z)
    {
        return x * size * size + y * size + z;
    }

    private Vec3i doCalculatePosition(final int index) {
        final int x = index / (size * size);
        final int y = (index - x * size * size) / size;
        final int z = index - x * size * size - y * size;

        return new Vec3i(x, y, z);
    }

    private void ensureCapacity() {
        final int requiredSize = (int) Math.ceil((getTotalEntryCount() * entryWidth) / (float) Byte.SIZE);
        if (data.length() < requiredSize) {
            final byte[] rawData = getRawData();
            final byte[] newData = new byte[requiredSize];
            System.arraycopy(rawData, 0, newData, 0, rawData.length);
            this.data = BitSet.valueOf(newData);
        }
    }

    private int getTotalEntryCount() {
        return size * size * size;
    }

    @Override
    public int hashCode() {
        return this.getData().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IMVoxelShape shape))
            return false;
        if (this == obj)
            return true;

        return this.getSize() == shape.getSize() && this.getData().equals(shape.getData());
    }
}
