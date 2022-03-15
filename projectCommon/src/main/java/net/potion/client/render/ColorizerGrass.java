package net.potion.client.render;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;

@Side(CodeSide.CLIENT)
public class ColorizerGrass {
    private static int[] grassBuffer = new int[65536];

    public static void setGrassBuffer(int[] grassBuffer) {
        ColorizerGrass.grassBuffer = grassBuffer;
    }

    public static int getGrassColor(double var0, double var2) {
        var2 = var2 * var0;
        int var4 = (int) ((1.0D - var0) * 255.0D);
        int var5 = (int) ((1.0D - var2) * 255.0D);
        return grassBuffer[var5 << 8 | var4];
    }
}
