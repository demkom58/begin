package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Explosion {
   public boolean field_12257_a = false;
   private Random field_12250_h = new Random();
   private World field_12249_i;
   public double field_12256_b;
   public double field_12255_c;
   public double field_12254_d;
   public Entity field_12253_e;
   public float field_12252_f;
   public Set field_12251_g = new HashSet();

   public Explosion(World var1, Entity var2, double var3, double var5, double var7, float var9) {
      this.field_12249_i = var1;
      this.field_12253_e = var2;
      this.field_12252_f = var9;
      this.field_12256_b = var3;
      this.field_12255_c = var5;
      this.field_12254_d = var7;
   }

   public void func_12248_a() {
      float var1 = this.field_12252_f;
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
                  float var14 = this.field_12252_f * (0.7F + this.field_12249_i.field_1037_n.nextFloat() * 0.6F);
                  double var15 = this.field_12256_b;
                  double var17 = this.field_12255_c;
                  double var19 = this.field_12254_d;

                  for(float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * 0.75F) {
                     int var22 = MathHelper.func_1108_b(var15);
                     int var23 = MathHelper.func_1108_b(var17);
                     int var24 = MathHelper.func_1108_b(var19);
                     int var25 = this.field_12249_i.func_600_a(var22, var23, var24);
                     if (var25 > 0) {
                        var14 -= (Block.field_345_n[var25].func_227_a(this.field_12253_e) + 0.3F) * var21;
                     }

                     if (var14 > 0.0F) {
                        this.field_12251_g.add(new ChunkPosition(var22, var23, var24));
                     }

                     var15 += var6 * (double)var21;
                     var17 += var8 * (double)var21;
                     var19 += var10 * (double)var21;
                  }
               }
            }
         }
      }

      this.field_12252_f *= 2.0F;
      int var29 = MathHelper.func_1108_b(this.field_12256_b - (double)this.field_12252_f - 1.0D);
      int var30 = MathHelper.func_1108_b(this.field_12256_b + (double)this.field_12252_f + 1.0D);
      int var31 = MathHelper.func_1108_b(this.field_12255_c - (double)this.field_12252_f - 1.0D);
      int var33 = MathHelper.func_1108_b(this.field_12255_c + (double)this.field_12252_f + 1.0D);
      int var7 = MathHelper.func_1108_b(this.field_12254_d - (double)this.field_12252_f - 1.0D);
      int var35 = MathHelper.func_1108_b(this.field_12254_d + (double)this.field_12252_f + 1.0D);
      List var9 = this.field_12249_i.func_659_b(this.field_12253_e, AxisAlignedBB.func_1161_b((double)var29, (double)var31, (double)var7, (double)var30, (double)var33, (double)var35));
      Vec3D var37 = Vec3D.func_1248_b(this.field_12256_b, this.field_12255_c, this.field_12254_d);

      for(int var11 = 0; var11 < var9.size(); ++var11) {
         Entity var39 = (Entity)var9.get(var11);
         double var13 = var39.func_361_e(this.field_12256_b, this.field_12255_c, this.field_12254_d) / (double)this.field_12252_f;
         if (var13 <= 1.0D) {
            double var43 = var39.field_611_ak - this.field_12256_b;
            double var46 = var39.field_610_al - this.field_12255_c;
            double var49 = var39.field_609_am - this.field_12254_d;
            double var51 = (double)MathHelper.func_1109_a(var43 * var43 + var46 * var46 + var49 * var49);
            var43 = var43 / var51;
            var46 = var46 / var51;
            var49 = var49 / var51;
            double var52 = (double)this.field_12249_i.func_675_a(var37, var39.field_601_au);
            double var53 = (1.0D - var13) * var52;
            var39.func_396_a(this.field_12253_e, (int)((var53 * var53 + var53) / 2.0D * 8.0D * (double)this.field_12252_f + 1.0D));
            var39.field_608_an += var43 * var53;
            var39.field_607_ao += var46 * var53;
            var39.field_606_ap += var49 * var53;
         }
      }

      this.field_12252_f = var1;
      ArrayList var38 = new ArrayList();
      var38.addAll(this.field_12251_g);
      if (this.field_12257_a) {
         for(int var40 = var38.size() - 1; var40 >= 0; --var40) {
            ChunkPosition var41 = (ChunkPosition)var38.get(var40);
            int var42 = var41.field_1111_a;
            int var45 = var41.field_1110_b;
            int var16 = var41.field_1112_c;
            int var48 = this.field_12249_i.func_600_a(var42, var45, var16);
            int var18 = this.field_12249_i.func_600_a(var42, var45 - 1, var16);
            if (var48 == 0 && Block.field_343_p[var18] && this.field_12250_h.nextInt(3) == 0) {
               this.field_12249_i.func_690_d(var42, var45, var16, Block.field_402_as.field_376_bc);
            }
         }
      }

   }

   public void func_12247_b(boolean var1) {
      this.field_12249_i.func_684_a(this.field_12256_b, this.field_12255_c, this.field_12254_d, "random.explode", 4.0F, (1.0F + (this.field_12249_i.field_1037_n.nextFloat() - this.field_12249_i.field_1037_n.nextFloat()) * 0.2F) * 0.7F);
      ArrayList var2 = new ArrayList();
      var2.addAll(this.field_12251_g);

      for(int var3 = var2.size() - 1; var3 >= 0; --var3) {
         ChunkPosition var4 = (ChunkPosition)var2.get(var3);
         int var5 = var4.field_1111_a;
         int var6 = var4.field_1110_b;
         int var7 = var4.field_1112_c;
         int var8 = this.field_12249_i.func_600_a(var5, var6, var7);
         if (var1) {
            double var9 = (double)((float)var5 + this.field_12249_i.field_1037_n.nextFloat());
            double var11 = (double)((float)var6 + this.field_12249_i.field_1037_n.nextFloat());
            double var13 = (double)((float)var7 + this.field_12249_i.field_1037_n.nextFloat());
            double var15 = var9 - this.field_12256_b;
            double var17 = var11 - this.field_12255_c;
            double var19 = var13 - this.field_12254_d;
            double var21 = (double)MathHelper.func_1109_a(var15 * var15 + var17 * var17 + var19 * var19);
            var15 = var15 / var21;
            var17 = var17 / var21;
            var19 = var19 / var21;
            double var23 = 0.5D / (var21 / (double)this.field_12252_f + 0.1D);
            var23 = var23 * (double)(this.field_12249_i.field_1037_n.nextFloat() * this.field_12249_i.field_1037_n.nextFloat() + 0.3F);
            var15 = var15 * var23;
            var17 = var17 * var23;
            var19 = var19 * var23;
            this.field_12249_i.func_694_a("explode", (var9 + this.field_12256_b * 1.0D) / 2.0D, (var11 + this.field_12255_c * 1.0D) / 2.0D, (var13 + this.field_12254_d * 1.0D) / 2.0D, var15, var17, var19);
            this.field_12249_i.func_694_a("smoke", var9, var11, var13, var15, var17, var19);
         }

         if (var8 > 0) {
            Block.field_345_n[var8].func_216_a(this.field_12249_i, var5, var6, var7, this.field_12249_i.func_602_e(var5, var6, var7), 0.3F);
            this.field_12249_i.func_690_d(var5, var6, var7, 0);
            Block.field_345_n[var8].func_4027_c(this.field_12249_i, var5, var6, var7);
         }
      }

   }
}
