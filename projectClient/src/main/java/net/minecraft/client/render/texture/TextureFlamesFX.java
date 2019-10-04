package net.minecraft.client.render.texture;

import net.minecraft.block.Block;

public class TextureFlamesFX extends TextureFX {
    protected float[] field_1133_g = new float[320];
    protected float[] field_1132_h = new float[320];

    public TextureFlamesFX(int var1) {
        super(Block.FIRE.blockIndexInTexture + var1 * 16);
    }

    @Override
    public void onTick() {
        for (int var1 = 0; var1 < 16; ++var1) {
            for (int var2 = 0; var2 < 20; ++var2) {
                int var3 = 18;
                float var4 = this.field_1133_g[var1 + (var2 + 1) % 20 * 16] * (float) var3;

                for (int var5 = var1 - 1; var5 <= var1 + 1; ++var5) {
                    for (int var6 = var2; var6 <= var2 + 1; ++var6) {
                        if (var5 >= 0 && var6 >= 0 && var5 < 16 && var6 < 20) {
                            var4 += this.field_1133_g[var5 + var6 * 16];
                        }

                        ++var3;
                    }
                }

                this.field_1132_h[var1 + var2 * 16] = var4 / ((float) var3 * 1.06F);
                if (var2 >= 19) {
                    this.field_1132_h[var1 + var2 * 16] = (float) (Math.random() * Math.random() * Math.random() * 4.0D + Math.random() * 0.10000000149011612D + 0.20000000298023224D);
                }
            }
        }

        float[] var12 = this.field_1132_h;
        this.field_1132_h = this.field_1133_g;
        this.field_1133_g = var12;

        for (int var13 = 0; var13 < 256; ++var13) {
            float var14 = this.field_1133_g[var13] * 1.8F;
            if (var14 > 1.0F) {
                var14 = 1.0F;
            }

            if (var14 < 0.0F) {
                var14 = 0.0F;
            }

            int var16 = (int) (var14 * 155.0F + 100.0F);
            int var17 = (int) (var14 * var14 * 255.0F);
            int var7 = (int) (var14 * var14 * var14 * var14 * var14 * var14 * var14 * var14 * var14 * var14 * 255.0F);
            short var8 = 255;
            if (var14 < 0.5F) {
                var8 = 0;
            }

            float var15 = (var14 - 0.5F) * 2.0F;
            if (this.anaglyphEnabled) {
                int var9 = (var16 * 30 + var17 * 59 + var7 * 11) / 100;
                int var10 = (var16 * 30 + var17 * 70) / 100;
                int var11 = (var16 * 30 + var7 * 70) / 100;
                var16 = var9;
                var17 = var10;
                var7 = var11;
            }

            this.imageData[var13 * 4] = (byte) var16;
            this.imageData[var13 * 4 + 1] = (byte) var17;
            this.imageData[var13 * 4 + 2] = (byte) var7;
            this.imageData[var13 * 4 + 3] = (byte) var8;
        }

    }
}
