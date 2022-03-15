package net.potion.client.render;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;

@Side(CodeSide.CLIENT)
public class RenderColorizerFoliage {
    private static int[] foliageBuffer = new int[65536];

    public static void setFoliageBuffer(int[] foliageBuffer) {
        RenderColorizerFoliage.foliageBuffer = foliageBuffer;
    }

    public static int getFoliageColor(double temperature, double humidity) {
        humidity = humidity * temperature;
        int tmp = (int) ((1.0D - temperature) * 255.0D);
        int hum = (int) ((1.0D - humidity) * 255.0D);
        return foliageBuffer[hum << 8 | tmp];
    }

    public static int getFoliageColorPine() {
        return 0x619961;
    }

    public static int getFoliageColorBirch() {
        return 0x80a755;
    }

    public static int func_31073_c() {
        return 0x48b518;
    }
}
