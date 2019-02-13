package net.minecraft;

public class TextureWaterFX extends TextureFX {
    protected float[] field_1158_g = new float[256];
    protected float[] field_1157_h = new float[256];
    protected float[] field_1156_i = new float[256];
    protected float[] field_1155_j = new float[256];
    private int tickCounter = 0;

    public TextureWaterFX() {
        super(Block.waterMoving.blockIndexInTexture);
    }

    public void onTick() {
        ++this.tickCounter;

        for (int var1 = 0; var1 < 16; ++var1) {
            for (int var2 = 0; var2 < 16; ++var2) {
                float var3 = 0.0F;

                for (int var4 = var1 - 1; var4 <= var1 + 1; ++var4) {
                    int var5 = var4 & 15;
                    int var6 = var2 & 15;
                    var3 += this.field_1158_g[var5 + var6 * 16];
                }

                this.field_1157_h[var1 + var2 * 16] = var3 / 3.3F + this.field_1156_i[var1 + var2 * 16] * 0.8F;
            }
        }

        for (int var12 = 0; var12 < 16; ++var12) {
            for (int var14 = 0; var14 < 16; ++var14) {
                this.field_1156_i[var12 + var14 * 16] += this.field_1155_j[var12 + var14 * 16] * 0.05F;
                if (this.field_1156_i[var12 + var14 * 16] < 0.0F) {
                    this.field_1156_i[var12 + var14 * 16] = 0.0F;
                }

                this.field_1155_j[var12 + var14 * 16] -= 0.1F;
                if (Math.random() < 0.05D) {
                    this.field_1155_j[var12 + var14 * 16] = 0.5F;
                }
            }
        }

        float[] var13 = this.field_1157_h;
        this.field_1157_h = this.field_1158_g;
        this.field_1158_g = var13;

        for (int var15 = 0; var15 < 256; ++var15) {
            float var16 = this.field_1158_g[var15];
            if (var16 > 1.0F) {
                var16 = 1.0F;
            }

            if (var16 < 0.0F) {
                var16 = 0.0F;
            }

            float var17 = var16 * var16;
            int var18 = (int) (32.0F + var17 * 32.0F);
            int var19 = (int) (50.0F + var17 * 64.0F);
            int var7 = 255;
            int var8 = (int) (146.0F + var17 * 50.0F);
            if (this.anaglyphEnabled) {
                int var9 = (var18 * 30 + var19 * 59 + var7 * 11) / 100;
                int var10 = (var18 * 30 + var19 * 70) / 100;
                int var11 = (var18 * 30 + var7 * 70) / 100;
                var18 = var9;
                var19 = var10;
                var7 = var11;
            }

            this.imageData[var15 * 4 + 0] = (byte) var18;
            this.imageData[var15 * 4 + 1] = (byte) var19;
            this.imageData[var15 * 4 + 2] = (byte) var7;
            this.imageData[var15 * 4 + 3] = (byte) var8;
        }

    }
}
