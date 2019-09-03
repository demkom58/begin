package net.minecraft.client.render;

import net.minecraft.client.GameSettings;

public class ScaledResolution {
    public double width;
    public double height;
    public int scaleFactor;
    private int scaledWidth;
    private int scaledHeight;

    public ScaledResolution(GameSettings settings, int scaledWidth, int scaledHeight) {
        this.scaledWidth = scaledWidth;
        this.scaledHeight = scaledHeight;
        this.scaleFactor = 1;

        int scale = settings.guiScale;
        if (scale == 0) {
            scale = 1000;
        }

        while (this.scaleFactor < scale
                && this.scaledWidth / (this.scaleFactor + 1) >= 320
                && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }

        this.width = (double) this.scaledWidth / (double) this.scaleFactor;
        this.height = (double) this.scaledHeight / (double) this.scaleFactor;

        this.scaledWidth = (int) Math.ceil(this.width);
        this.scaledHeight = (int) Math.ceil(this.height);
    }

    public int getScaledWidth() {
        return this.scaledWidth;
    }

    public int getScaledHeight() {
        return this.scaledHeight;
    }
}
