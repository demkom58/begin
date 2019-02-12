package net.minecraft.src;

public class TextureLavaFX extends TextureFX {
   protected float[] field_1147_g = new float[256];
   protected float[] field_1146_h = new float[256];
   protected float[] field_1145_i = new float[256];
   protected float[] field_1144_j = new float[256];

   public TextureLavaFX() {
      super(Block.lavaMoving.blockIndexInTexture);
   }

   public void onTick() {
      for(int var1 = 0; var1 < 16; ++var1) {
         for(int var2 = 0; var2 < 16; ++var2) {
            float var3 = 0.0F;
            int var4 = (int)(MathHelper.sin((float)var2 * 3.1415927F * 2.0F / 16.0F) * 1.2F);
            int var5 = (int)(MathHelper.sin((float)var1 * 3.1415927F * 2.0F / 16.0F) * 1.2F);

            for(int var6 = var1 - 1; var6 <= var1 + 1; ++var6) {
               for(int var7 = var2 - 1; var7 <= var2 + 1; ++var7) {
                  int var8 = var6 + var4 & 15;
                  int var9 = var7 + var5 & 15;
                  var3 += this.field_1147_g[var8 + var9 * 16];
               }
            }

            this.field_1146_h[var1 + var2 * 16] = var3 / 10.0F + (this.field_1145_i[(var1 + 0 & 15) + (var2 + 0 & 15) * 16] + this.field_1145_i[(var1 + 1 & 15) + (var2 + 0 & 15) * 16] + this.field_1145_i[(var1 + 1 & 15) + (var2 + 1 & 15) * 16] + this.field_1145_i[(var1 + 0 & 15) + (var2 + 1 & 15) * 16]) / 4.0F * 0.8F;
            this.field_1145_i[var1 + var2 * 16] += this.field_1144_j[var1 + var2 * 16] * 0.01F;
            if (this.field_1145_i[var1 + var2 * 16] < 0.0F) {
               this.field_1145_i[var1 + var2 * 16] = 0.0F;
            }

            this.field_1144_j[var1 + var2 * 16] -= 0.06F;
            if (Math.random() < 0.005D) {
               this.field_1144_j[var1 + var2 * 16] = 1.5F;
            }
         }
      }

      float[] var11 = this.field_1146_h;
      this.field_1146_h = this.field_1147_g;
      this.field_1147_g = var11;

      for(int var12 = 0; var12 < 256; ++var12) {
         float var13 = this.field_1147_g[var12] * 2.0F;
         if (var13 > 1.0F) {
            var13 = 1.0F;
         }

         if (var13 < 0.0F) {
            var13 = 0.0F;
         }

         int var14 = (int)(var13 * 100.0F + 155.0F);
         int var15 = (int)(var13 * var13 * 255.0F);
         int var16 = (int)(var13 * var13 * var13 * var13 * 128.0F);
         if (this.anaglyphEnabled) {
            int var17 = (var14 * 30 + var15 * 59 + var16 * 11) / 100;
            int var18 = (var14 * 30 + var15 * 70) / 100;
            int var10 = (var14 * 30 + var16 * 70) / 100;
            var14 = var17;
            var15 = var18;
            var16 = var10;
         }

         this.imageData[var12 * 4 + 0] = (byte)var14;
         this.imageData[var12 * 4 + 1] = (byte)var15;
         this.imageData[var12 * 4 + 2] = (byte)var16;
         this.imageData[var12 * 4 + 3] = -1;
      }

   }
}
