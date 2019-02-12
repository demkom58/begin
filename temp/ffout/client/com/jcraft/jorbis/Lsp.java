package com.jcraft.jorbis;

class Lsp {
   static final float M_PI = 3.1415927F;

   static void lsp_to_curve(float[] var0, int[] var1, int var2, int var3, float[] var4, int var5, float var6, float var7) {
      float var9 = 3.1415927F / (float)var3;

      for(int var8 = 0; var8 < var5; ++var8) {
         var4[var8] = Lookup.coslook(var4[var8]);
      }

      int var10 = var5 / 2 * 2;

      float var24;
      int var10001;
      for(int var18 = 0; var18 < var2; var0[var10001] *= var24) {
         int var11 = var1[var18];
         float var12 = 0.70710677F;
         var24 = 0.70710677F;
         float var14 = Lookup.coslook(var9 * (float)var11);

         for(int var15 = 0; var15 < var10; var15 += 2) {
            var24 *= var4[var15] - var14;
            var12 *= var4[var15 + 1] - var14;
         }

         if ((var5 & 1) != 0) {
            var24 = var24 * (var4[var5 - 1] - var14);
            var24 = var24 * var24;
            var12 = var12 * var12 * (1.0F - var14 * var14);
         } else {
            var24 = var24 * var24 * (1.0F + var14);
            var12 = var12 * var12 * (1.0F - var14);
         }

         var24 = var12 + var24;
         int var25 = Float.floatToIntBits(var24);
         int var16 = Integer.MAX_VALUE & var25;
         int var17 = 0;
         if (var16 < 2139095040 && var16 != 0) {
            if (var16 < 8388608) {
               var24 = (float)((double)var24 * 3.3554432E7D);
               var25 = Float.floatToIntBits(var24);
               var16 = Integer.MAX_VALUE & var25;
               var17 = -25;
            }

            var17 += (var16 >>> 23) - 126;
            var25 = var25 & -2139095041 | 1056964608;
            var24 = Float.intBitsToFloat(var25);
         }

         var24 = Lookup.fromdBlook(var6 * Lookup.invsqlook(var24) * Lookup.invsq2explook(var17 + var5) - var7);

         while(true) {
            var10001 = var18++;
            if (var18 >= var2 || var1[var18] != var11) {
               break;
            }
         }
      }

   }
}
