package net.minecraft.src;

import java.util.Random;

public class NoiseGeneratorPerlin extends NoiseGenerator {
   private int[] field_1189_d;
   public double field_1188_a;
   public double field_1187_b;
   public double field_1190_c;

   public NoiseGeneratorPerlin() {
      this(new Random());
   }

   public NoiseGeneratorPerlin(Random var1) {
      this.field_1189_d = new int[512];
      this.field_1188_a = var1.nextDouble() * 256.0D;
      this.field_1187_b = var1.nextDouble() * 256.0D;
      this.field_1190_c = var1.nextDouble() * 256.0D;

      for(int var2 = 0; var2 < 256; this.field_1189_d[var2] = var2++) {
         ;
      }

      for(int var5 = 0; var5 < 256; ++var5) {
         int var3 = var1.nextInt(256 - var5) + var5;
         int var4 = this.field_1189_d[var5];
         this.field_1189_d[var5] = this.field_1189_d[var3];
         this.field_1189_d[var3] = var4;
         this.field_1189_d[var5 + 256] = this.field_1189_d[var5];
      }

   }

   public double func_802_a(double var1, double var3, double var5) {
      double var7 = var1 + this.field_1188_a;
      double var9 = var3 + this.field_1187_b;
      double var11 = var5 + this.field_1190_c;
      int var13 = (int)var7;
      int var14 = (int)var9;
      int var15 = (int)var11;
      if (var7 < (double)var13) {
         --var13;
      }

      if (var9 < (double)var14) {
         --var14;
      }

      if (var11 < (double)var15) {
         --var15;
      }

      int var16 = var13 & 255;
      int var17 = var14 & 255;
      int var18 = var15 & 255;
      var7 = var7 - (double)var13;
      var9 = var9 - (double)var14;
      var11 = var11 - (double)var15;
      double var19 = var7 * var7 * var7 * (var7 * (var7 * 6.0D - 15.0D) + 10.0D);
      double var21 = var9 * var9 * var9 * (var9 * (var9 * 6.0D - 15.0D) + 10.0D);
      double var23 = var11 * var11 * var11 * (var11 * (var11 * 6.0D - 15.0D) + 10.0D);
      int var25 = this.field_1189_d[var16] + var17;
      int var26 = this.field_1189_d[var25] + var18;
      int var27 = this.field_1189_d[var25 + 1] + var18;
      int var28 = this.field_1189_d[var16 + 1] + var17;
      int var29 = this.field_1189_d[var28] + var18;
      int var30 = this.field_1189_d[var28 + 1] + var18;
      return this.func_804_b(var23, this.func_804_b(var21, this.func_804_b(var19, this.func_803_a(this.field_1189_d[var26], var7, var9, var11), this.func_803_a(this.field_1189_d[var29], var7 - 1.0D, var9, var11)), this.func_804_b(var19, this.func_803_a(this.field_1189_d[var27], var7, var9 - 1.0D, var11), this.func_803_a(this.field_1189_d[var30], var7 - 1.0D, var9 - 1.0D, var11))), this.func_804_b(var21, this.func_804_b(var19, this.func_803_a(this.field_1189_d[var26 + 1], var7, var9, var11 - 1.0D), this.func_803_a(this.field_1189_d[var29 + 1], var7 - 1.0D, var9, var11 - 1.0D)), this.func_804_b(var19, this.func_803_a(this.field_1189_d[var27 + 1], var7, var9 - 1.0D, var11 - 1.0D), this.func_803_a(this.field_1189_d[var30 + 1], var7 - 1.0D, var9 - 1.0D, var11 - 1.0D))));
   }

   public final double func_804_b(double var1, double var3, double var5) {
      return var3 + var1 * (var5 - var3);
   }

   public final double func_4110_a(int var1, double var2, double var4) {
      int var6 = var1 & 15;
      double var7 = (double)(1 - ((var6 & 8) >> 3)) * var2;
      double var9 = var6 < 4 ? 0.0D : (var6 != 12 && var6 != 14 ? var4 : var2);
      return ((var6 & 1) == 0 ? var7 : -var7) + ((var6 & 2) == 0 ? var9 : -var9);
   }

   public final double func_803_a(int var1, double var2, double var4, double var6) {
      int var8 = var1 & 15;
      double var9 = var8 < 8 ? var2 : var4;
      double var11 = var8 < 4 ? var4 : (var8 != 12 && var8 != 14 ? var6 : var2);
      return ((var8 & 1) == 0 ? var9 : -var9) + ((var8 & 2) == 0 ? var11 : -var11);
   }

   public double func_801_a(double var1, double var3) {
      return this.func_802_a(var1, var3, 0.0D);
   }

   public void func_805_a(double[] var1, double var2, double var4, double var6, int var8, int var9, int var10, double var11, double var13, double var15, double var17) {
      if (var9 == 1) {
         int var64 = 0;
         int var66 = 0;
         int var21 = 0;
         int var69 = 0;
         double var72 = 0.0D;
         double var76 = 0.0D;
         int var80 = 0;
         double var82 = 1.0D / var17;

         for(int var30 = 0; var30 < var8; ++var30) {
            double var83 = (var2 + (double)var30) * var11 + this.field_1188_a;
            int var85 = (int)var83;
            if (var83 < (double)var85) {
               --var85;
            }

            int var34 = var85 & 255;
            var83 = var83 - (double)var85;
            double var86 = var83 * var83 * var83 * (var83 * (var83 * 6.0D - 15.0D) + 10.0D);

            for(int var87 = 0; var87 < var10; ++var87) {
               double var89 = (var6 + (double)var87) * var15 + this.field_1190_c;
               int var91 = (int)var89;
               if (var89 < (double)var91) {
                  --var91;
               }

               int var92 = var91 & 255;
               var89 = var89 - (double)var91;
               double var93 = var89 * var89 * var89 * (var89 * (var89 * 6.0D - 15.0D) + 10.0D);
               var64 = this.field_1189_d[var34] + 0;
               var66 = this.field_1189_d[var64] + var92;
               var21 = this.field_1189_d[var34 + 1] + 0;
               var69 = this.field_1189_d[var21] + var92;
               var72 = this.func_804_b(var86, this.func_4110_a(this.field_1189_d[var66], var83, var89), this.func_803_a(this.field_1189_d[var69], var83 - 1.0D, 0.0D, var89));
               var76 = this.func_804_b(var86, this.func_803_a(this.field_1189_d[var66 + 1], var83, 0.0D, var89 - 1.0D), this.func_803_a(this.field_1189_d[var69 + 1], var83 - 1.0D, 0.0D, var89 - 1.0D));
               double var94 = this.func_804_b(var93, var72, var76);
               int var97 = var80++;
               var1[var97] += var94 * var82;
            }
         }

      } else {
         int var19 = 0;
         double var20 = 1.0D / var17;
         int var22 = -1;
         int var23 = 0;
         int var24 = 0;
         int var25 = 0;
         int var26 = 0;
         int var27 = 0;
         int var28 = 0;
         double var29 = 0.0D;
         double var31 = 0.0D;
         double var33 = 0.0D;
         double var35 = 0.0D;

         for(int var37 = 0; var37 < var8; ++var37) {
            double var38 = (var2 + (double)var37) * var11 + this.field_1188_a;
            int var40 = (int)var38;
            if (var38 < (double)var40) {
               --var40;
            }

            int var41 = var40 & 255;
            var38 = var38 - (double)var40;
            double var42 = var38 * var38 * var38 * (var38 * (var38 * 6.0D - 15.0D) + 10.0D);

            for(int var44 = 0; var44 < var10; ++var44) {
               double var45 = (var6 + (double)var44) * var15 + this.field_1190_c;
               int var47 = (int)var45;
               if (var45 < (double)var47) {
                  --var47;
               }

               int var48 = var47 & 255;
               var45 = var45 - (double)var47;
               double var49 = var45 * var45 * var45 * (var45 * (var45 * 6.0D - 15.0D) + 10.0D);

               for(int var51 = 0; var51 < var9; ++var51) {
                  double var52 = (var4 + (double)var51) * var13 + this.field_1187_b;
                  int var54 = (int)var52;
                  if (var52 < (double)var54) {
                     --var54;
                  }

                  int var55 = var54 & 255;
                  var52 = var52 - (double)var54;
                  double var56 = var52 * var52 * var52 * (var52 * (var52 * 6.0D - 15.0D) + 10.0D);
                  if (var51 == 0 || var55 != var22) {
                     var22 = var55;
                     var23 = this.field_1189_d[var41] + var55;
                     var24 = this.field_1189_d[var23] + var48;
                     var25 = this.field_1189_d[var23 + 1] + var48;
                     var26 = this.field_1189_d[var41 + 1] + var55;
                     var27 = this.field_1189_d[var26] + var48;
                     var28 = this.field_1189_d[var26 + 1] + var48;
                     var29 = this.func_804_b(var42, this.func_803_a(this.field_1189_d[var24], var38, var52, var45), this.func_803_a(this.field_1189_d[var27], var38 - 1.0D, var52, var45));
                     var31 = this.func_804_b(var42, this.func_803_a(this.field_1189_d[var25], var38, var52 - 1.0D, var45), this.func_803_a(this.field_1189_d[var28], var38 - 1.0D, var52 - 1.0D, var45));
                     var33 = this.func_804_b(var42, this.func_803_a(this.field_1189_d[var24 + 1], var38, var52, var45 - 1.0D), this.func_803_a(this.field_1189_d[var27 + 1], var38 - 1.0D, var52, var45 - 1.0D));
                     var35 = this.func_804_b(var42, this.func_803_a(this.field_1189_d[var25 + 1], var38, var52 - 1.0D, var45 - 1.0D), this.func_803_a(this.field_1189_d[var28 + 1], var38 - 1.0D, var52 - 1.0D, var45 - 1.0D));
                  }

                  double var58 = this.func_804_b(var56, var29, var31);
                  double var60 = this.func_804_b(var56, var33, var35);
                  double var62 = this.func_804_b(var49, var58, var60);
                  int var10001 = var19++;
                  var1[var10001] += var62 * var20;
               }
            }
         }

      }
   }
}
