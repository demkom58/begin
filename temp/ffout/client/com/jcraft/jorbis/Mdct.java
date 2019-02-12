package com.jcraft.jorbis;

class Mdct {
   int n;
   int log2n;
   float[] trig;
   int[] bitrev;
   float scale;
   float[] _x = new float[1024];
   float[] _w = new float[1024];

   void init(int var1) {
      this.bitrev = new int[var1 / 4];
      this.trig = new float[var1 + var1 / 4];
      this.log2n = (int)Math.rint(Math.log((double)var1) / Math.log(2.0D));
      this.n = var1;
      byte var2 = 0;
      byte var3 = 1;
      int var4 = var2 + var1 / 2;
      int var5 = var4 + 1;
      int var6 = var4 + var1 / 2;
      int var7 = var6 + 1;

      for(int var8 = 0; var8 < var1 / 4; ++var8) {
         this.trig[var2 + var8 * 2] = (float)Math.cos(3.141592653589793D / (double)var1 * (double)(4 * var8));
         this.trig[var3 + var8 * 2] = (float)(-Math.sin(3.141592653589793D / (double)var1 * (double)(4 * var8)));
         this.trig[var4 + var8 * 2] = (float)Math.cos(3.141592653589793D / (double)(2 * var1) * (double)(2 * var8 + 1));
         this.trig[var5 + var8 * 2] = (float)Math.sin(3.141592653589793D / (double)(2 * var1) * (double)(2 * var8 + 1));
      }

      for(int var13 = 0; var13 < var1 / 8; ++var13) {
         this.trig[var6 + var13 * 2] = (float)Math.cos(3.141592653589793D / (double)var1 * (double)(4 * var13 + 2));
         this.trig[var7 + var13 * 2] = (float)(-Math.sin(3.141592653589793D / (double)var1 * (double)(4 * var13 + 2)));
      }

      int var14 = (1 << this.log2n - 1) - 1;
      int var9 = 1 << this.log2n - 2;

      for(int var10 = 0; var10 < var1 / 8; ++var10) {
         int var11 = 0;

         for(int var12 = 0; var9 >>> var12 != 0; ++var12) {
            if ((var9 >>> var12 & var10) != 0) {
               var11 |= 1 << var12;
            }
         }

         this.bitrev[var10 * 2] = ~var11 & var14;
         this.bitrev[var10 * 2 + 1] = var11;
      }

      this.scale = 4.0F / (float)var1;
   }

   void clear() {
   }

   void forward(float[] var1, float[] var2) {
   }

   synchronized void backward(float[] var1, float[] var2) {
      if (this._x.length < this.n / 2) {
         this._x = new float[this.n / 2];
      }

      if (this._w.length < this.n / 2) {
         this._w = new float[this.n / 2];
      }

      float[] var3 = this._x;
      float[] var4 = this._w;
      int var5 = this.n >>> 1;
      int var6 = this.n >>> 2;
      int var7 = this.n >>> 3;
      int var8 = 1;
      int var9 = 0;
      int var10 = var5;

      for(int var11 = 0; var11 < var7; ++var11) {
         var10 -= 2;
         var3[var9++] = -var1[var8 + 2] * this.trig[var10 + 1] - var1[var8] * this.trig[var10];
         var3[var9++] = var1[var8] * this.trig[var10 + 1] - var1[var8 + 2] * this.trig[var10];
         var8 += 4;
      }

      var8 = var5 - 4;

      for(int var24 = 0; var24 < var7; ++var24) {
         var10 -= 2;
         var3[var9++] = var1[var8] * this.trig[var10 + 1] + var1[var8 + 2] * this.trig[var10];
         var3[var9++] = var1[var8] * this.trig[var10] - var1[var8 + 2] * this.trig[var10 + 1];
         var8 -= 4;
      }

      float[] var19 = this.mdct_kernel(var3, var4, this.n, var5, var6, var7);
      var9 = 0;
      var10 = var5;
      int var25 = var6;
      int var12 = var6 - 1;
      int var13 = var6 + var5;
      int var14 = var13 - 1;

      for(int var15 = 0; var15 < var6; ++var15) {
         float var16 = var19[var9] * this.trig[var10 + 1] - var19[var9 + 1] * this.trig[var10];
         float var17 = -(var19[var9] * this.trig[var10] + var19[var9 + 1] * this.trig[var10 + 1]);
         var2[var25] = -var16;
         var2[var12] = var16;
         var2[var13] = var17;
         var2[var14] = var17;
         ++var25;
         --var12;
         ++var13;
         --var14;
         var9 += 2;
         var10 += 2;
      }

   }

   private float[] mdct_kernel(float[] var1, float[] var2, int var3, int var4, int var5, int var6) {
      int var7 = var5;
      int var8 = 0;
      int var9 = var5;
      int var10 = var4;

      for(int var11 = 0; var11 < var5; ++var11) {
         float var12 = var1[var7] - var1[var8];
         var2[var9 + var11] = var1[var7++] + var1[var8++];
         float var13 = var1[var7] - var1[var8];
         var10 -= 4;
         var2[var11++] = var12 * this.trig[var10] + var13 * this.trig[var10 + 1];
         var2[var11] = var13 * this.trig[var10] - var12 * this.trig[var10 + 1];
         var2[var9 + var11] = var1[var7++] + var1[var8++];
      }

      for(int var32 = 0; var32 < this.log2n - 3; ++var32) {
         int var35 = var3 >>> var32 + 2;
         int var39 = 1 << var32 + 3;
         int var14 = var4 - 2;
         var10 = 0;

         for(int var16 = 0; var16 < var35 >>> 2; ++var16) {
            int var17 = var14;
            var9 = var14 - (var35 >> 1);
            float var18 = this.trig[var10];
            float var20 = this.trig[var10 + 1];
            var14 -= 2;
            ++var35;

            for(int var22 = 0; var22 < 2 << var32; ++var22) {
               float var21 = var2[var17] - var2[var9];
               var1[var17] = var2[var17] + var2[var9];
               ++var17;
               float var10000 = var2[var17];
               ++var9;
               float var19 = var10000 - var2[var9];
               var1[var17] = var2[var17] + var2[var9];
               var1[var9] = var19 * var18 - var21 * var20;
               var1[var9 - 1] = var21 * var18 + var19 * var20;
               var17 -= var35;
               var9 -= var35;
            }

            --var35;
            var10 += var39;
         }

         float[] var15 = var2;
         var2 = var1;
         var1 = var15;
      }

      int var33 = var3;
      int var37 = 0;
      int var40 = 0;
      int var42 = var4 - 1;

      for(int var44 = 0; var44 < var6; ++var44) {
         int var45 = this.bitrev[var37++];
         int var47 = this.bitrev[var37++];
         float var48 = var2[var45] - var2[var47 + 1];
         float var49 = var2[var45 - 1] + var2[var47];
         float var50 = var2[var45] + var2[var47 + 1];
         float var51 = var2[var45 - 1] - var2[var47];
         float var52 = var48 * this.trig[var33];
         float var23 = var49 * this.trig[var33++];
         float var24 = var48 * this.trig[var33];
         float var25 = var49 * this.trig[var33++];
         var1[var40++] = (var50 + var24 + var23) * 0.5F;
         var1[var42--] = (-var51 + var25 - var52) * 0.5F;
         var1[var40++] = (var51 + var25 - var52) * 0.5F;
         var1[var42--] = (var50 - var24 - var23) * 0.5F;
      }

      return var1;
   }
}
