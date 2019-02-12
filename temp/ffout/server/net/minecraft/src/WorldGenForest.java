package net.minecraft.src;

import java.util.Random;

public class WorldGenForest extends WorldGenerator {
   public boolean func_419_a(World var1, Random var2, int var3, int var4, int var5) {
      int var6 = var2.nextInt(3) + 5;
      boolean var7 = true;
      if (var4 >= 1 && var4 + var6 + 1 <= 128) {
         for(int var8 = var4; var8 <= var4 + 1 + var6; ++var8) {
            byte var9 = 1;
            if (var8 == var4) {
               var9 = 0;
            }

            if (var8 >= var4 + 1 + var6 - 2) {
               var9 = 2;
            }

            for(int var10 = var3 - var9; var10 <= var3 + var9 && var7; ++var10) {
               for(int var11 = var5 - var9; var11 <= var5 + var9 && var7; ++var11) {
                  if (var8 >= 0 && var8 < 128) {
                     int var12 = var1.func_444_a(var10, var8, var11);
                     if (var12 != 0 && var12 != Block.field_581_L.field_573_bc) {
                        var7 = false;
                     }
                  } else {
                     var7 = false;
                  }
               }
            }
         }

         if (!var7) {
            return false;
         } else {
            int var16 = var1.func_444_a(var3, var4 - 1, var5);
            if ((var16 == Block.field_534_v.field_573_bc || var16 == Block.field_533_w.field_573_bc) && var4 < 128 - var6 - 1) {
               var1.func_462_a(var3, var4 - 1, var5, Block.field_533_w.field_573_bc);

               for(int var17 = var4 - 3 + var6; var17 <= var4 + var6; ++var17) {
                  int var19 = var17 - (var4 + var6);
                  int var21 = 1 - var19 / 2;

                  for(int var22 = var3 - var21; var22 <= var3 + var21; ++var22) {
                     int var13 = var22 - var3;

                     for(int var14 = var5 - var21; var14 <= var5 + var21; ++var14) {
                        int var15 = var14 - var5;
                        if ((Math.abs(var13) != var21 || Math.abs(var15) != var21 || var2.nextInt(2) != 0 && var19 != 0) && !Block.field_540_p[var1.func_444_a(var22, var17, var14)]) {
                           var1.func_470_a(var22, var17, var14, Block.field_581_L.field_573_bc, 2);
                        }
                     }
                  }
               }

               for(int var18 = 0; var18 < var6; ++var18) {
                  int var20 = var1.func_444_a(var3, var4 + var18, var5);
                  if (var20 == 0 || var20 == Block.field_581_L.field_573_bc) {
                     var1.func_470_a(var3, var4 + var18, var5, Block.field_582_K.field_573_bc, 2);
                  }
               }

               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }
}
