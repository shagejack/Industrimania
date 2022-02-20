package shagejack.industrimania.foundation.utility.color;

import com.mojang.blaze3d.platform.NativeImage;

import java.awt.*;

import static com.mojang.blaze3d.platform.NativeImage.getA;


public class GreyColorizeHelper {

    private int getNewColor(int color, int x, int y) {
        if (getA(color) == 0)
            return 0x00000000;

        int grey = GreyColorizeMapping.getGrey(new Color(color, true));

        //TODO: return new color
        return 0;
    }

    public void transform(NativeImage image) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                image.setPixelRGBA(x, y, getNewColor(image.getPixelRGBA(x, y), x, y));
            }
        }
    }


}
