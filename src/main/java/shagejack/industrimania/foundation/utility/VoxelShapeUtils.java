package shagejack.industrimania.foundation.utility;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class VoxelShapeUtils {

    private VoxelShapeUtils() {
        throw new IllegalStateException(this.getClass().toString() + "should not be instantiated as it's a utility class.");
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{ shape, Shapes.empty() };

        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1-maxZ, minY, minX, 1-minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    public static VoxelShape joinAllShape(BooleanOp booleanOperator, VoxelShape ... shapes) {
        VoxelShape result = Shapes.empty();
        for (VoxelShape shape : shapes) {
            result = Shapes.join(result, shape, booleanOperator);
        }
        return result;
    }

    public static VoxelShape joinAllShape(List<BooleanOp> booleanOperators, VoxelShape ... shapes) {
        VoxelShape result = Shapes.empty();

        if (shapes.length > booleanOperators.size())
            throw new IllegalArgumentException("The number of boolean operators is lesser than shapes passed in.");

        for (int i = 0; i < shapes.length; i++) {
            result = Shapes.join(result, shapes[i], booleanOperators.get(i));
        }

        return result;
    }

}
