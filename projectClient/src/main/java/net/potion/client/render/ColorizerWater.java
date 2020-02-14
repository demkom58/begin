package net.potion.client.render;

public class ColorizerWater {
    private static int[] waterBuffer = new int[65536];

    public static void setWaterBuffer(int[] waterBuffer) {
        ColorizerWater.waterBuffer = waterBuffer;
    }
}
