package shagejack.industrimania.foundation.utility;

/**
 * A simple immutable RGBA Color class which can be used on server side.
 */
public class Color {

    public static final Color WHITE        = new Color(255, 255, 255);
    public static final Color LIGHT_GRAY   = new Color(192, 192, 192);
    public static final Color GRAY         = new Color(128, 128, 128);
    public static final Color DARK_GRAY    = new Color(64, 64, 64);
    public static final Color BLACK        = new Color(0, 0, 0);
    public static final Color RED          = new Color(255, 0, 0);
    public static final Color PINK         = new Color(255, 175, 175);
    public static final Color ORANGE       = new Color(255, 200, 0);
    public static final Color YELLOW       = new Color(255, 255, 0);
    public static final Color GREEN        = new Color(0, 255, 0);
    public static final Color MAGENTA      = new Color(255, 0, 255);
    public static final Color CYAN         = new Color(0, 255, 255);
    public static final Color BLUE         = new Color(0, 0, 255);

    private final int value;

    public Color(int r, int g, int b, int a) {
        this.value = ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
        testColorValueRange(r, g, b, a);
    }

    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public Color(int rgb) {
        this.value = 0xFF000000 | rgb;
    }

    public Color(int rgba, boolean hasAlpha) {
        this.value = hasAlpha ? rgba : (0xFF000000 | rgba);
    }

    public int getRed() {
        return (getRGB() >> 16) & 0xFF;
    }

    public int getGreen() {
        return (getRGB() >> 8) & 0xFF;
    }

    public int getBlue() {
        return getRGB() & 0xFF;
    }

    public int getAlpha() {
        return (getRGB() >> 24) & 0xff;
    }

    public int getRGB() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Color && ((Color)obj).getRGB() == this.getRGB();
    }

    @Override
    public String toString() {
        return getClass().getName() + "[r=" + getRed() + ", g=" + getGreen() + ", b=" + getBlue() + "]";
    }

    private static void testColorValueRange(int r, int g, int b, int a) {
        if ( a < 0 || a > 255) {
            throw new IllegalArgumentException("Color parameter outside of expected range: Alpha");
        } else if ( r < 0 || r > 255) {
            throw new IllegalArgumentException("Color parameter outside of expected range: Red");
        } else if ( g < 0 || g > 255) {
            throw new IllegalArgumentException("Color parameter outside of expected range: Green");
        } else if ( b < 0 || b > 255) {
            throw new IllegalArgumentException("Color parameter outside of expected range: Blue");
        }
    }
}
