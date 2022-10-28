package net.potion.client.render;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;

@Side(CodeSide.CLIENT)
public class RenderColorizerWater {
    private static int[] waterBuffer = new int[65536];

    public static void setWaterBuffer(int[] waterBuffer) {
        RenderColorizerWater.waterBuffer = waterBuffer;
    }
}
