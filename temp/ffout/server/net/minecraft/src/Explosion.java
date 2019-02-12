package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Explosion {
   public boolean field_12031_a = false;
   private Random field_12024_h = new Random();
   private World field_4310_a;
   public double field_12030_b;
   public double field_12029_c;
   public double field_12028_d;
   public Entity field_12027_e;
   public float field_12026_f;
   public Set field_12025_g = new HashSet();

   public Explosion(World var1, Entity var2, double var3, double var5, double var7, float var9) {
      this.field_4310_a = var1;
      this.field_12027_e = var2;
      this.field_12026_f = var9;
      this.field_12030_b = var3;
      this.field_12029_c = var5;
      this.field_12028_d = var7;
   }

   public void func_12023_a() {
      float var1 = this.field_12026_f;
      byte var2 = 16;

      for(int var3 = 0; var3 < var2; ++var3) {
         for(int var4 = 0; var4 < var2; ++var4) {
            for(int var5 = 0; var5 < var2; ++var5) {
               if (var3 == 0 || var3 == var2 - 1 || var4 == 0 || var4 == var2 - 1 || var5 == 0 || var5 == var2 - 1) {
                  double var6 = (double)((float)var3 / ((float)var2 - 1.0F) * 2.0F - 1.0F);
                  double var8 = (double)((float)var4 / ((float)var2 - 1.0F) * 2.0F - 1.0F);
                  double var10 = (double)((float)var5 / ((float)var2 - 1.0F) * 2.0F - 1.0F);
                  double var12 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
                  var6 = var6 / var12;
                  var8 = var8 / var12;
                  var10 = var10 / var12;
                  float var14 = this.field_12026_f * (0.7F + this.field_4310_a.field_803_m.nextFloat() * 0.6F);
                  double var15 = this.field_12030_b;
                  double var17 = this.field_12029_c;
                  double var19 = this.field_12028_d;

                  for(float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * 0.75F) {
                     int var22 = MathHelper.func_584_b(var15);
                     int var23 = MathHelper.func_584_b(var17);
                     int var24 = MathHelper.func_584_b(var19);
                     int var25 = this.field_4310_a.func_444_a(var22, var23, var24);
                     if (var25 > 0) {
                        var14 -= (Block.field_542_n[var25].func_226_a(this.field_12027_e) + 0.3F) * var21;
                     }

                     if (var14 > 0.0F) {
                        this.field_12025_g.add(new ChunkPosition(var22, var23, var24));
                     }

                     var15 += var6 * (double)var21;
                     var17 += var8 * (double)var21;
                     var19 += var10 * (double)var21;
                  }
               }
            }
         }
      }

      this.field_12026_f *= 2.0F;
      int var29 = MathHelper.func_584_b(this.field_12030_b - (double)this.field_12026_f - 1.0D);
      int var30 = MathHelper.func_584_b(this.field_12030_b + (double)this.field_12026_f + 1.0D);
      int var31 = MathHelper.func_584_b(this.field_12029_c - (double)this.field_12026_f - 1.0D);
      int var33 = MathHelper.func_584_b(this.field_12029_c + (double)this.field_12026_f + 1.0D);
      int var7 = MathHelper.func_584_b(this.field_12028_d - (double)this.field_12026_f - 1.0D);
      int var35 = MathHelper.func_584_b(this.field_12028_d + (double)this.field_12026_f + 1.0D);
      List var9 = this.field_4310_a.func_450_b(this.field_12027_e, AxisAlignedBB.func_693_b((double)var29, (double)var31, (double)var7, (double)var30, (double)var33, (double)var35));
      Vec3D var37 = Vec3D.func_768_b(this.field_12030_b, this.field_12029_c, this.field_12028_d);

      for(int var11 = 0; var11 < var9.size(); ++var11) {
         Entity var39 = (Entity)var9.get(var11);
         double var13 = var39.func_103_e(this.field_12030_b, this.field_12029_c, this.field_12028_d) / (double)this.field_12026_f;
         if (var13 <= 1.0D) {
            double var43 = var39.field_322_l - this.field_12030_b;
            double var46 = var39.field_321_m - this.field_12029_c;
            double var49 = var39.field_320_n - this.field_12028_d;
            double var51 = (double)MathHelper.func_583_a(var43 * var43 + var46 * var46 + var49 * var49);
            var43 = var43 / var51;
            var46 = var46 / var51;
            var49 = var49 / var51;
            double var52 = (double)this.field_4310_a.func_494_a(var37, var39.field_312_v);
            double var53 = (1.0D - var13) * var52;
            var39.func_121_a(this.field_12027_e, (int)((var53 * var53 + var53) / 2.0D * 8.0D * (double)this.field_12026_f + 1.0D));
            var39.field_319_o += var43 * var53;
            var39.field_318_p += var46 * var53;
            var39.field_317_q += var49 * var53;
         }
      }

      this.field_12026_f = var1;
      ArrayList var38 = new ArrayList();
      var38.addAll(this.field_12025_g);
      if (this.field_12031_a) {
         for(int var40 = var38.size() - 1; var40 >= 0; --var40) {
            ChunkPosition var41 = (ChunkPosition)var38.get(var40);
            int var42 = var41.field_846_a;
            int var45 = var41.field_845_b;
            int var16 = var41.field_847_c;
            int var48 = this.field_4310_a.func_444_a(var42, var45, var16);
            int var18 = this.field_4310_a.func_444_a(var42, var45 - 1, var16);
            if (var48 == 0 && Block.field_540_p[var18] && this.field_12024_h.nextInt(3) == 0) {
               this.field_4310_a.func_508_d(var42, var45, var16, Block.field_599_as.field_573_bc);
            }
         }
      }

   }

   public void func_732_a(boolean var1) {
      this.field_4310_a.func_502_a(this.field_12030_b, this.field_12029_c, this.field_12028_d, "random.explode", 4.0F, (1.0F + (this.field_4310_a.field_803_m.nextFloat() - this.field_4310_a.field_803_m.nextFloat()) * 0.2F) * 0.7F);
      ArrayList var2 = new ArrayList();
      var2.addAll(this.field_12025_g);

      for(int var3 = var2.size() - 1; var3 >= 0; --var3) {
         ChunkPosition var4 = (ChunkPosition)var2.get(var3);
         int var5 = var4.field_846_a;
         int var6 = var4.field_845_b;
         int var7 = var4.field_847_c;
         int var8 = this.field_4310_a.func_444_a(var5, var6, var7);
         if (var1) {
            double var9 = (double)((float)var5 + this.field_4310_a.field_803_m.nextFloat());
            double var11 = (double)((float)var6 + this.field_4310_a.field_803_m.nextFloat());
            double var13 = (double)((float)var7 + this.field_4310_a.field_803_m.nextFloat());
            double var15 = var9 - this.field_12030_b;
            double var17 = var11 - this.field_12029_c;
            double var19 = var13 - this.field_12028_d;
            double var21 = (double)MathHelper.func_583_a(var15 * var15 + var17 * var17 + var19 * var19);
            var15 = var15 / var21;
            var17 = var17 / var21;
            var19 = var19 / var21;
            double var23 = 0.5D / (var21 / (double)this.field_12026_f + 0.1D);
            var23 = var23 * (double)(this.field_4310_a.field_803_m.nextFloat() * this.field_4310_a.field_803_m.nextFloat() + 0.3F);
            var15 = var15 * var23;
            var17 = var17 * var23;
            var19 = var19 * var23;
            this.field_4310_a.func_514_a("explode", (var9 + this.field_12030_b * 1.0D) / 2.0D, (var11 + this.field_12029_c * 1.0D) / 2.0D, (var13 + this.field_12028_d * 1.0D) / 2.0D, var15, var17, var19);
            this.field_4310_a.func_514_a("smoke", var9, var11, var13, var15, var17, var19);
         }

         if (var8 > 0) {
            Block.field_542_n[var8].func_227_a(this.field_4310_a, var5, var6, var7, this.field_4310_a.func_446_b(var5, var6, var7), 0.3F);
            this.field_4310_a.func_508_d(var5, var6, var7, 0);
            Block.field_542_n[var8].func_4029_c(this.field_4310_a, var5, var6, var7);
         }
      }

   }
}
