package shagejack.industrimania.foundation.utility;

public class BitUtils {

    private BitUtils() {
        throw new IllegalStateException(this.getClass().toString() + "should not be instantiated as it's a utility class.");
    }

    public static int getBitMask(final int bitWidth) {
        if (bitWidth == Integer.SIZE) {
            return 0xFFFFFFFF;
        }

        return (1 << bitWidth) - 1;
    }

    public static int getByteCount(final int totalBitCount)
    {
        return (int) Math.ceil(totalBitCount / (float) Byte.SIZE);
    }

    public static int getMaskWidth(final int bitMask)
    {
        if (bitMask == 0xFFFFFFFF) {
            return 32;
        }

        final int maxBit = bitMask + 1;
        return Integer.SIZE - Integer.numberOfLeadingZeros(maxBit) - 1;
    }

}
