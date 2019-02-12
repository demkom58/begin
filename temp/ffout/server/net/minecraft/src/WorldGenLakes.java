package net.minecraft.src;

import java.util.Random;

public class WorldGenLakes extends WorldGenerator {
   private int field_15005_a;

   public WorldGenLakes(int var1) {
      this.field_15005_a = var1;
   }

   public boolean func_419_a(World var1, Random var2, int var3, int var4, int var5) {
      var3 = var3 - 8;

      for(var5 = var5 - 8; var4 > 0 && var1.func_20111_e(var3, var4, var5); --var4) {
         ;
      }

      var4 = var4 - 4;
      boolean[] var6 = new boolean[2048];
      int var7 = var2.nextInt(4) + 4;

      for(int var8 = 0; var8 < var7; ++var8) {
         double var9 = var2.nextDouble() * 6.0D + 3.0D;
         double var11 = var2.nextDouble() * 4.0D + 2.0D;
         double var13 = var2.nextDouble() * 6.0D + 3.0D;
         double var15 = var2.nextDouble() * (16.0D - var9 - 2.0D) + 1.0D + var9 / 2.0D;
         double var17 = var2.nextDouble() * (8.0D - var11 - 4.0D) + 2.0D + var11 / 2.0D;
         double var19 = var2.nextDouble() * (16.0D - var13 - 2.0D) + 1.0D + var13 / 2.0D;

         for(int var21 = 1; var21 < 15; ++var21) {
            for(int var22 = 1; var22 < 15; ++var22) {
               for(int var23 = 1; var23 < 7; ++var23) {
                  double var24 = ((double)var21 - var15) / (var9 / 2.0D);
                  double var26 = ((double)var23 - var17) / (var11 / 2.0D);
                  double var28 = ((double)var22 - var19) / (var13 / 2.0D);
                  double var30 = var24 * var24 + var26 * var26 + var28 * var28;
                  if (var30 < 1.0D) {
                     var6[(var21 * 16 + var22) * 8 + var23] = true;
                  }
               }
            }
         }
      }

      for(int var35 = 0; var35 < 16; ++var35) {
         for(int var39 = 0; var39 < 16; ++var39) {
            for(int var10 = 0; var10 < 8; ++var10) {
               boolean var46 = !var6[(var35 * 16 + var39) * 8 + var10] && (var35 < 15 && var6[((var35 + 1) * 16 + var39) * 8 + var10] || var35 > 0 && var6[((var35 - 1) * 16 + var39) * 8 + var10] || var39 < 15 && var6[(var35 * 16 + var39 + 1) * 8 + var10] || var39 > 0 && var6[(var35 * 16 + (var39 - 1)) * 8 + var10] || var10 < 7 && var6[(var35 * 16 + var39) * 8 + var10 + 1] || var10 > 0 && var6[(var35 * 16 + var39) * 8 + (var10 - 1)]);
               if (var46) {
                  Material var12 = var1.func_443_c(var3 + var35, var4 + var10, var5 + var39);
                  if (var10 >= 4 && var12.func_217_d()) {
                     return false;
                  }

                  if (var10 < 4 && !var12.func_216_a() && var1.func_444_a(var3 + var35, var4 + var10, var5 + var39) != this.field_15005_a) {
                     return false;
                  }
               }
            }
         }
      }

      for(int var36 = 0; var36 < 16; ++var36) {
         for(int var40 = 0; var40 < 16; ++var40) {
            for(int var43 = 0; var43 < 8; ++var43) {
               if (var6[(var36 * 16 + var40) * 8 + var43]) {
                  var1.func_462_a(var3 + var36, var4 + var43, var5 + var40, var43 >= 4 ? 0 : this.field_15005_a);
               }
            }
         }
      }

      for(int var37 = 0; var37 < 16; ++var37) {
         for(int var41 = 0; var41 < 16; ++var41) {
            for(int var44 = 4; var44 < 8; ++var44) {
               if (var6[(var37 * 16 + var41) * 8 + var44] && var1.func_444_a(var3 + var37, var4 + var44 - 1, var5 + var41) == Block.field_533_w.field_573_bc && var1.func_512_a(EnumSkyBlock.Sky, var3 + var37, var4 + var44, var5 + var41) > 0) {
                  var1.func_462_a(var3 + var37, var4 + var44 - 1, var5 + var41, Block.field_534_v.field_573_bc);
               }
            }
         }
      }

      if (Block.field_542_n[this.field_15005_a].field_553_bn == Material.field_520_g) {
         for(int var38 = 0; var38 < 16; ++var38) {
            for(int var42 = 0; var42 < 16; ++var42) {
               for(int var45 = 0; var45 < 8; ++var45) {
                  boolean var47 = !var6[(var38 * 16 + var42) * 8 + var45] && (var38 < 15 && var6[((var38 + 1) * 16 + var42) * 8 + var45] || var38 > 0 && var6[((var38 - 1) * 16 + var42) * 8 + var45] || var42 < 15 && var6[(var38 * 16 + var42 + 1) * 8 + var45] || var42 > 0 && var6[(var38 * 16 + (var42 - 1)) * 8 + var45] || var45 < 7 && var6[(var38 * 16 + var42) * 8 + var45 + 1] || var45 > 0 && var6[(var38 * 16 + var42) * 8 + (var45 - 1)]);
                  if (var47 && (var45 < 4 || var2.nextInt(2) != 0) && var1.func_443_c(var3 + var38, var4 + var45, var5 + var42).func_216_a()) {
                     var1.func_462_a(var3 + var38, var4 + var45, var5 + var42, Block.field_535_u.field_573_bc);
                  }
               }
            }
         }
      }

      return true;
   }
}
