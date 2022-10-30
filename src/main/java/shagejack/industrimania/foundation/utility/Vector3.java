package shagejack.industrimania.foundation.utility;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public record Vector3(double x, double y, double z) {

    private static final Random RAND = new Random();
    public static final Vector3 ZERO = new Vector3(0, 0, 0);
    public static final Vector3 X_POS_AXIS = new Vector3(1, 0, 0);
    public static final Vector3 Y_POS_AXIS = new Vector3(0, 1, 0);
    public static final Vector3 Z_POS_AXIS = new Vector3(0, 0, 1);
    public static final Vector3 X_NEG_AXIS = new Vector3(-1, 0, 0);
    public static final Vector3 Y_NEG_AXIS = new Vector3(0, -1, 0);
    public static final Vector3 Z_NEG_AXIS = new Vector3(0, 0, -1);

    public static Vector3 of(double x, double y, double z) {
        return new Vector3(x, y, z);
    }

    public static Vector3 of(Vec3i pos) {
        return new Vector3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vector3 atCenterOf(Vec3i pos) {
        return new Vector3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    public static Vector3 of(Vec3 pos) {
        return new Vector3(pos.x(), pos.y(), pos.z());
    }

    public static Vector3 of(Vector3f pos) {
        return new Vector3(pos.x(), pos.y(), pos.z());
    }

    public static Vector3 of(Entity entity) {
        return Vector3.of(entity.position());
    }

    public static Vector3 of(CompoundTag tag) {
        return new Vector3(tag.getDouble("X"), tag.getDouble("Y"), tag.getDouble("Z"));
    }

    public static Vector3 random() {
        return random(RAND);
    }

    /**
     * Get a random 3d vector between (-1, -1, -1) and (1, 1, 1). This method is actually equivalent to getRandomPos(random, Vector3.of(-1, -1, -1), Vector3.of(1, 1, 1)). <br/> <br/>
     * WARNING: Usage like Vector3.random(random).normalized() are not promised to be evenly distributed. Use Vector3#randomNormal instead.
     * @param random The random instance that gives out the random 3d vector.
     * @return the random 3d vector.
     */
    public static Vector3 random(Random random) {
        return new Vector3(random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1), random.nextDouble() * (random.nextBoolean() ? 1 : -1));
    }

    public static Vector3 randomNormal() {
        return randomNormal(RAND);
    }

    /**
     * Get a random normalized 3d vector evenly distributed on the surface of a sphere of radius 1.
     * @param random The random instance that gives out the random 3d vector.
     * @return the random normalized 3d vector.
     */
    public static Vector3 randomNormal(Random random) {
        double theta = 2 * Math.PI * random.nextDouble();
        double phi = random.nextDouble() * (random.nextBoolean() ? Math.PI : -Math.PI);
        return new Vector3(Math.cos(phi) * Math.sin(theta), Math.sin(phi), Math.cos(phi) * Math.cos(theta));
    }

    public static Vector3 getRandomPos(Random random, Vector3 a, Vector3 b) {
        return a.add(b.subtract(a).multiply(random.nextDouble(), random.nextDouble(), random.nextDouble()));
    }

    public static Vector3 getRandomPos(Vector3 a, Vector3 b) {
        return getRandomPos(RAND, a, b);
    }

    public static BlockPos getRandomBlockPos(Random random, Vector3 a, Vector3 b) {
        final BlockPos posA = MathUtils.getBetweenClosedBlockPos(a, b);
        final BlockPos posB = MathUtils.getBetweenClosedBlockPos(b, a);
        return MathUtils.randomBetweenBlockPos(random, posA, posB);
    }

    public static BlockPos getRandomBlockPos(Vector3 a, Vector3 b) {
        return getRandomBlockPos(RAND, a, b);
    }

    public Vector3 copy() {
        return new Vector3(x, y, z);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof Vector3 vec3)) {
            return false;
        } else {
            return Double.compare(this.x(), vec3.x()) == 0 && Double.compare(this.y(), vec3.y()) == 0 && Double.compare(this.z(), vec3.z()) == 0;
        }
    }

    @Override
    public int hashCode() {
        long j = Double.doubleToLongBits(this.x);
        int i = (int) (j ^ j >>> 32);
        j = Double.doubleToLongBits(this.y);
        i = 31 * i + (int) (j ^ j >>> 32);
        j = Double.doubleToLongBits(this.z);
        return 31 * i + (int) (j ^ j >>> 32);
    }

    public long asLong() {
        return asLong(this.x, this.y, this.z);
    }

    public static long asLong(double pX, double pY, double pZ) {
        long j = Double.doubleToLongBits(pX);
        long i = (j ^ j >>> 32);
        j = Double.doubleToLongBits(pY);
        i = 31 * i + (j ^ j >>> 32);
        j = Double.doubleToLongBits(pZ);
        return 31 * i + (j ^ j >>> 32);
    }

    public float xF() {
        return (float) this.x;
    }

    public float yF() {
        return (float) this.y;
    }

    public float zF() {
        return (float) this.z;
    }

    public Vector3 setX(double nX) {
        return new Vector3(nX, this.y, this.z);
    }

    public Vector3 setY(double nY) {
        return new Vector3(this.x, nY, this.z);
    }

    public Vector3 setZ(double nZ) {
        return new Vector3(this.x, this.y, nZ);
    }

    public Vector3 addX(double dX) {
        return new Vector3(this.x + dX, this.y, this.z);
    }

    public Vector3 addY(double dY) {
        return new Vector3(this.x, this.y + dY, this.z);
    }

    public Vector3 addZ(double dZ) {
        return new Vector3(this.x, this.y, this.z + dZ);
    }

    public Vector3 add(Vector3 vec) {
        return new Vector3(x + vec.x, y + vec.y, z + vec.z);
    }

    public Vector3 add(double dX, double dY, double dZ) {
        return new Vector3(x + dX, y + dY, z + dZ);
    }

    public Vector3 subtract(Vector3 vec) {
        return new Vector3(x - vec.x, y - vec.y, z - vec.z);
    }

    public Vector3 multiply(double mul) {
        return new Vector3(x * mul, y * mul, z * mul);
    }

    public Vector3 multiply(double mulX, double mulY, double mulZ) {
        return new Vector3(x * mulZ, y * mulY, z * mulZ);
    }

    public Vector3 divide(double divider) {
        return new Vector3(x / divider, y / divider, z / divider);
    }

    public Vector3 divide(double dividerX, double dividerY, double dividerZ) {
        return new Vector3(x / dividerX, y / dividerY, z / dividerZ);
    }

    public double dot(Vector3 vec) {
        return x * vec.x + y * vec.y + z * vec.z;
    }

    public Vector3 cross(Vector3 vec) {
        return new Vector3(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
    }

    public Vector3 lerp(Vector3 toVec, double t) {
        return this.add(toVec.subtract(this).multiply(t));
    }

    public Vector3 quadraticInterpolation(Vector3 toVec, double t) {
        return functionalInterpolation(toVec, dt -> 2 * dt - dt * dt, t);
    }

    /**
     * method to get functional interpolated vectors
     * @param toVec the end point
     * @param rangedFunction a mapping from delta T to scale of Y (usually 0.0 - 1.0)
     * @param t delta T (usually) between 0.0 and 1.0
     * @return the interpolated vector
     */
    public Vector3 functionalInterpolation(Vector3 toVec, Double2DoubleFunction rangedFunction, double t) {
        Vector3 between = toVec.subtract(this);
        return between.setY(0).multiply(t).setY(between.y() * rangedFunction.applyAsDouble(t));
    }

    public Vector3 opposite() {
        return new Vector3(-x, -y, -z);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double distance(Vector3 vec) {
        return vec.subtract(this).length();
    }

    public Vector3 normalize() {
        if (!this.equals(ZERO))
            return this.divide(this.length());

        return Y_POS_AXIS;
    }

    public Vector3 perpendicular() {
        return this.x() > this.z() ? new Vector3(this.y(), -this.x(), 0) : new Vector3(0, -this.z(), this.y());
    }

    public boolean isPerpendicularTo(Vector3 vec) {
        return Double.compare(this.dot(vec), 0.0) == 0;
    }

    public boolean isParallelTo(Vector3 vec) {
        return Double.compare(Math.abs(this.normalize().dot(vec.normalize())), 1.0) == 0;
    }

    public double includedAngle(Vector3 vec) {
        return Math.acos(this.dot(vec) / (this.length() * vec.length()));
    }

    public double includedAngleDegree(Vector3 vec) {
        return this.includedAngle(vec) * 180 / Math.PI;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("X", x);
        tag.putDouble("Y", y);
        tag.putDouble("Z", z);
        return tag;
    }

    public VertexConsumer drawPosNormal(Matrix3f normal, VertexConsumer builder) {
        return builder.normal(normal, (float) this.x, (float) this.y, (float) this.z);
    }

    public VertexConsumer drawPosVertex(Matrix4f renderMatrix, VertexConsumer builder) {
        return builder.vertex(renderMatrix, (float) this.x, (float) this.y, (float) this.z);
    }

    public Vector3f toVec3f() {
        return new Vector3f((float) x, (float) y, (float) z);
    }

    public Vec3 toVec3() {
        return new Vec3(x, y, z);
    }

    public BlockPos toBlockPos() {
        return new BlockPos((int) x, (int) y, (int) z);
    }

    public Vector3 integral() {
        return new Vector3((int) x, (int) y, (int) z);
    }

    public Quaternion asRotateAxis(double angle) {
        return new Quaternion(this.normalize().toVec3f(), (float) angle, false);
    }

    public Quaternion asRotateAxisDegree(double angle) {
        return new Quaternion(this.normalize().toVec3f(), (float) angle, true);
    }

    public Quaternion asToVecRotation(Vector3 toVec) {
        Vector3 fromNormal = this.normalize();
        Vector3 toNormal = toVec.normalize();

        if (fromNormal.equals(toNormal.opposite()))
            return fromNormal.perpendicular().asRotateAxis(Math.PI);

        Vector3 half = fromNormal.add(toNormal).normalize();
        Vector3f crossProductHalf = fromNormal.cross(half).toVec3f();

        return new Quaternion(crossProductHalf.x(), crossProductHalf.y(), crossProductHalf.z(), (float) fromNormal.dot(half));
    }

    public Vector3 rotate(double angle, Vector3 axis) {
        if (!axis.equals(ZERO) && Double.compare(angle, 0) != 0)
            return this.transform(new Quaternion(axis.normalize().toVec3f(), (float) angle, false));

        return this;
    }

    public Vector3 rotateDegree(double angle, Vector3 axis) {
        if (!axis.equals(ZERO) && Double.compare(angle, 0) != 0)
            return this.transform(new Quaternion(axis.normalize().toVec3f(), (float) angle, true));

        return this;
    }

    public Vector3 transform(Matrix3f pMatrix) {
        Vector3f vec = this.toVec3f();
        vec.transform(pMatrix);
        return Vector3.of(vec);
    }

    public Vector3 transform(Quaternion pQuaternion) {
        Quaternion quaternion = new Quaternion(pQuaternion);
        quaternion.mul(new Quaternion((float) this.x(), (float) this.y(), (float) this.z(), 0.0F));
        Quaternion quaternion1 = new Quaternion(pQuaternion);
        quaternion1.conj();
        quaternion.mul(quaternion1);
        return new Vector3(quaternion.i(), quaternion.j(), quaternion.k());
    }

    public Vector3 apply(UnaryOperator<Vector3> operator) {
        return operator.apply(this);
    }

    public void apply(DoubleTriConsumer consumer) {
        consumer.accept(this.x(), this.y(), this.z());
    }

    public void apply(Consumer<Vector3> consumer) {
        consumer.accept(this);
    }

    @FunctionalInterface
    public interface DoubleTriConsumer {
        void accept(double x, double y, double z);
    }

    @Override
    public String toString() {
        return "[x: " + x() + ", y: " + y + ", z: " + z() + "]";
    }
}
