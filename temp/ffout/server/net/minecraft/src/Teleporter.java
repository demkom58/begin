package net.minecraft.src;

import java.util.Random;

public class Teleporter {
   private Random field_28117_a = new Random();

   public void func_28115_a(World var1, Entity var2) {
      if (!this.func_28114_b(var1, var2)) {
         this.func_28116_c(var1, var2);
         this.func_28114_b(var1, var2);
      }
   }

   public boolean func_28114_b(World var1, Entity var2) {
      short var3 = 128;
      double var4 = -1.0D;
      int var6 = 0;
      int var7 = 0;
      int var8 = 0;
      int var9 = MathHelper.func_584_b(var2.field_322_l);
      int var10 = MathHelper.func_584_b(var2.field_320_n);

      for(int var11 = var9 - var3; var11 <= var9 + var3; ++var11) {
         double var12 = (double)var11 + 0.5D - var2.field_322_l;

         for(int var14 = var10 - var3; var14 <= var10 + var3; ++var14) {
            double var15 = (double)var14 + 0.5D - var2.field_320_n;

            for(int var17 = 127; var17 >= 0; --var17) {
               if (var1.func_444_a(var11, var17, var14) == Block.field_4044_bf.field_573_bc) {
                  while(var1.func_444_a(var11, var17 - 1, var14) == Block.field_4044_bf.field_573_bc) {
                     --var17;
                  }

                  double var18 = (double)var17 + 0.5D - var2.field_321_m;
                  double var20 = var12 * var12 + var18 * var18 + var15 * var15;
                  if (var4 < 0.0D || var20 < var4) {
                     var4 = var20;
                     var6 = var11;
                     var7 = var17;
                     var8 = var14;
                  }
               }
            }
         }
      }

      if (var4 >= 0.0D) {
         double var22 = (double)var6 + 0.5D;
         double var16 = (double)var7 + 0.5D;
         double var23 = (double)var8 + 0.5D;
         if (var1.func_444_a(var6 - 1, var7, var8) == Block.field_4044_bf.field_573_bc) {
            var22 -= 0.5D;
         }

         if (var1.func_444_a(var6 + 1, var7, var8) == Block.field_4044_bf.field_573_bc) {
            var22 += 0.5D;
         }

         if (var1.func_444_a(var6, var7, var8 - 1) == Block.field_4044_bf.field_573_bc) {
            var23 -= 0.5D;
         }

         if (var1.func_444_a(var6, var7, var8 + 1) == Block.field_4044_bf.field_573_bc) {
            var23 += 0.5D;
         }

         var2.func_107_c(var22, var16, var23, var2.field_316_r, 0.0F);
         var2.field_319_o = var2.field_318_p = var2.field_317_q = 0.0D;
         return true;
      } else {
         return false;
      }
   }

   public boolean func_28116_c(World var1, Entity var2) {
      byte var3 = 16;
      double var4 = -1.0D;
      int var6 = MathHelper.func_584_b(var2.field_322_l);
      int var7 = MathHelper.func_584_b(var2.field_321_m);
      int var8 = MathHelper.func_584_b(var2.field_320_n);
      int var9 = var6;
      int var10 = var7;
      int var11 = var8;
      int var12 = 0;
      int var13 = this.field_28117_a.nextInt(4);

      for(int var14 = var6 - var3; var14 <= var6 + var3; ++var14) {
         double var15 = (double)var14 + 0.5D - var2.field_322_l;

         for(int var17 = var8 - var3; var17 <= var8 + var3; ++var17) {
            double var18 = (double)var17 + 0.5D - var2.field_320_n;

            label293:
            for(int var20 = 127; var20 >= 0; --var20) {
               if (var1.func_20111_e(var14, var20, var17)) {
                  while(var20 > 0 && var1.func_20111_e(var14, var20 - 1, var17)) {
                     --var20;
                  }

                  for(int var21 = var13; var21 < var13 + 4; ++var21) {
                     int var22 = var21 % 2;
                     int var23 = 1 - var22;
                     if (var21 % 4 >= 2) {
                        var22 = -var22;
                        var23 = -var23;
                     }

                     for(int var24 = 0; var24 < 3; ++var24) {
                        for(int var25 = 0; var25 < 4; ++var25) {
                           for(int var26 = -1; var26 < 4; ++var26) {
                              int var27 = var14 + (var25 - 1) * var22 + var24 * var23;
                              int var28 = var20 + var26;
                              int var29 = var17 + (var25 - 1) * var23 - var24 * var22;
                              if (var26 < 0 && !var1.func_443_c(var27, var28, var29).func_216_a() || var26 >= 0 && !var1.func_20111_e(var27, var28, var29)) {
                                 continue label293;
                              }
                           }
                        }
                     }

                     double var52 = (double)var20 + 0.5D - var2.field_321_m;
                     double var62 = var15 * var15 + var52 * var52 + var18 * var18;
                     if (var4 < 0.0D || var62 < var4) {
                        var4 = var62;
                        var9 = var14;
                        var10 = var20;
                        var11 = var17;
                        var12 = var21 % 4;
                     }
                  }
               }
            }
         }
      }

      if (var4 < 0.0D) {
         for(int var30 = var6 - var3; var30 <= var6 + var3; ++var30) {
            double var31 = (double)var30 + 0.5D - var2.field_322_l;

            for(int var33 = var8 - var3; var33 <= var8 + var3; ++var33) {
               double var35 = (double)var33 + 0.5D - var2.field_320_n;

               label231:
               for(int var37 = 127; var37 >= 0; --var37) {
                  if (var1.func_20111_e(var30, var37, var33)) {
                     while(var1.func_20111_e(var30, var37 - 1, var33)) {
                        --var37;
                     }

                     for(int var40 = var13; var40 < var13 + 2; ++var40) {
                        int var44 = var40 % 2;
                        int var48 = 1 - var44;

                        for(int var53 = 0; var53 < 4; ++var53) {
                           for(int var58 = -1; var58 < 4; ++var58) {
                              int var63 = var30 + (var53 - 1) * var44;
                              int var67 = var37 + var58;
                              int var68 = var33 + (var53 - 1) * var48;
                              if (var58 < 0 && !var1.func_443_c(var63, var67, var68).func_216_a() || var58 >= 0 && !var1.func_20111_e(var63, var67, var68)) {
                                 continue label231;
                              }
                           }
                        }

                        double var54 = (double)var37 + 0.5D - var2.field_321_m;
                        double var64 = var31 * var31 + var54 * var54 + var35 * var35;
                        if (var4 < 0.0D || var64 < var4) {
                           var4 = var64;
                           var9 = var30;
                           var10 = var37;
                           var11 = var33;
                           var12 = var40 % 2;
                        }
                     }
                  }
               }
            }
         }
      }

      int var32 = var9;
      int var16 = var10;
      int var34 = var11;
      int var36 = var12 % 2;
      int var19 = 1 - var36;
      if (var12 % 4 >= 2) {
         var36 = -var36;
         var19 = -var19;
      }

      if (var4 < 0.0D) {
         if (var10 < 70) {
            var10 = 70;
         }

         if (var10 > 118) {
            var10 = 118;
         }

         var16 = var10;

         for(int var38 = -1; var38 <= 1; ++var38) {
            for(int var41 = 1; var41 < 3; ++var41) {
               for(int var45 = -1; var45 < 3; ++var45) {
                  int var49 = var32 + (var41 - 1) * var36 + var38 * var19;
                  int var55 = var16 + var45;
                  int var59 = var34 + (var41 - 1) * var19 - var38 * var36;
                  boolean var65 = var45 < 0;
                  var1.func_508_d(var49, var55, var59, var65 ? Block.field_602_aq.field_573_bc : 0);
               }
            }
         }
      }

      for(int var39 = 0; var39 < 4; ++var39) {
         var1.field_808_h = true;

         for(int var42 = 0; var42 < 4; ++var42) {
            for(int var46 = -1; var46 < 4; ++var46) {
               int var50 = var32 + (var42 - 1) * var36;
               int var56 = var16 + var46;
               int var60 = var34 + (var42 - 1) * var19;
               boolean var66 = var42 == 0 || var42 == 3 || var46 == -1 || var46 == 3;
               var1.func_508_d(var50, var56, var60, var66 ? Block.field_602_aq.field_573_bc : Block.field_4044_bf.field_573_bc);
            }
         }

         var1.field_808_h = false;

         for(int var43 = 0; var43 < 4; ++var43) {
            for(int var47 = -1; var47 < 4; ++var47) {
               int var51 = var32 + (var43 - 1) * var36;
               int var57 = var16 + var47;
               int var61 = var34 + (var43 - 1) * var19;
               var1.func_449_g(var51, var57, var61, var1.func_444_a(var51, var57, var61));
            }
         }
      }

      return true;
   }
}
