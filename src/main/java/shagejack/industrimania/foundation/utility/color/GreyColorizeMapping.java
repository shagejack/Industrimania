package shagejack.industrimania.foundation.utility.color;

import java.awt.*;
import java.util.function.ToIntFunction;

public class GreyColorizeMapping {

    private ToIntFunction<ColorMapping> GET_GREY = ColorMapping::grey;

    public static int getGrey(Color color) {
        return (int) (0.299 * color.getRed() +0.587 * color.getGreen() + 0.114 * color.getBlue());
    }

    private record ColorMapping(int grey, int color) {}

}
