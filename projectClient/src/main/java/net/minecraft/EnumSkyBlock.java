package net.minecraft;

public enum EnumSkyBlock {
    SKY(15),
    BLOCK(0);

    public final int lightValue;

    EnumSkyBlock(int lightValue) {
        this.lightValue = lightValue;
    }
}
