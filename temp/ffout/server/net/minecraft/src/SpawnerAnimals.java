package net.minecraft.src;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SpawnerAnimals {
   private static Set field_4311_a = new HashSet();
   protected static final Class[] field_22213_a = new Class[]{EntitySpider.class, EntityZombie.class, EntitySkeleton.class};

   protected static ChunkPosition func_4112_a(World var0, int var1, int var2) {
      int var3 = var1 + var0.field_803_m.nextInt(16);
      int var4 = var0.field_803_m.nextInt(128);
      int var5 = var2 + var0.field_803_m.nextInt(16);
      return new ChunkPosition(var3, var4, var5);
   }

   public static final int func_4111_a(World var0, boolean var1, boolean var2) {
      if (!var1 && !var2) {
         return 0;
      } else {
         field_4311_a.clear();

         for(int var3 = 0; var3 < var0.field_805_k.size(); ++var3) {
            EntityPlayer var4 = (EntityPlayer)var0.field_805_k.get(var3);
            int var5 = MathHelper.func_584_b(var4.field_322_l / 16.0D);
            int var6 = MathHelper.func_584_b(var4.field_320_n / 16.0D);
            byte var7 = 8;

            for(int var8 = -var7; var8 <= var7; ++var8) {
               for(int var9 = -var7; var9 <= var7; ++var9) {
                  field_4311_a.add(new ChunkCoordIntPair(var8 + var5, var9 + var6));
               }
            }
         }

         int var35 = 0;
         ChunkCoordinates var36 = var0.func_22078_l();

         for(EnumCreatureType var40 : EnumCreatureType.values()) {
            if ((!var40.func_21103_d() || var2) && (var40.func_21103_d() || var1) && var0.func_487_a(var40.func_21105_a()) <= var40.func_21104_b() * field_4311_a.size() / 256) {
               label130:
               for(ChunkCoordIntPair var10 : field_4311_a) {
                  BiomeGenBase var11 = var0.func_4077_a().func_4066_a(var10);
                  List var12 = var11.func_25055_a(var40);
                  if (var12 != null && !var12.isEmpty()) {
                     int var13 = 0;

                     for(SpawnListEntry var15 : var12) {
                        var13 += var15.field_25144_b;
                     }

                     int var42 = var0.field_803_m.nextInt(var13);
                     SpawnListEntry var43 = (SpawnListEntry)var12.get(0);

                     for(SpawnListEntry var17 : var12) {
                        var42 -= var17.field_25144_b;
                        if (var42 < 0) {
                           var43 = var17;
                           break;
                        }
                     }

                     ChunkPosition var44 = func_4112_a(var0, var10.field_152_a * 16, var10.field_151_b * 16);
                     int var45 = var44.field_846_a;
                     int var18 = var44.field_845_b;
                     int var19 = var44.field_847_c;
                     if (!var0.func_445_d(var45, var18, var19) && var0.func_443_c(var45, var18, var19) == var40.func_21102_c()) {
                        int var20 = 0;

                        for(int var21 = 0; var21 < 3; ++var21) {
                           int var22 = var45;
                           int var23 = var18;
                           int var24 = var19;
                           byte var25 = 6;

                           for(int var26 = 0; var26 < 4; ++var26) {
                              var22 += var0.field_803_m.nextInt(var25) - var0.field_803_m.nextInt(var25);
                              var23 += var0.field_803_m.nextInt(1) - var0.field_803_m.nextInt(1);
                              var24 += var0.field_803_m.nextInt(var25) - var0.field_803_m.nextInt(var25);
                              if (func_21167_a(var40, var0, var22, var23, var24)) {
                                 float var27 = (float)var22 + 0.5F;
                                 float var28 = (float)var23;
                                 float var29 = (float)var24 + 0.5F;
                                 if (var0.func_456_a((double)var27, (double)var28, (double)var29, 24.0D) == null) {
                                    float var30 = var27 - (float)var36.field_22216_a;
                                    float var31 = var28 - (float)var36.field_529_a;
                                    float var32 = var29 - (float)var36.field_528_b;
                                    float var33 = var30 * var30 + var31 * var31 + var32 * var32;
                                    if (var33 >= 576.0F) {
                                       try {
                                          var46 = (EntityLiving)var43.field_25145_a.getConstructor(World.class).newInstance(var0);
                                       } catch (Exception var34) {
                                          var34.printStackTrace();
                                          return var35;
                                       }

                                       var46.func_107_c((double)var27, (double)var28, (double)var29, var0.field_803_m.nextFloat() * 360.0F, 0.0F);
                                       if (var46.func_155_a()) {
                                          ++var20;
                                          var0.func_526_a(var46);
                                          func_21166_a(var46, var0, var27, var28, var29);
                                          if (var20 >= var46.func_4045_i()) {
                                             continue label130;
                                          }
                                       }

                                       var35 += var20;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return var35;
      }
   }

   private static boolean func_21167_a(EnumCreatureType var0, World var1, int var2, int var3, int var4) {
      if (var0.func_21102_c() == Material.field_521_f) {
         return var1.func_443_c(var2, var3, var4).func_217_d() && !var1.func_445_d(var2, var3 + 1, var4);
      } else {
         return var1.func_445_d(var2, var3 - 1, var4) && !var1.func_445_d(var2, var3, var4) && !var1.func_443_c(var2, var3, var4).func_217_d() && !var1.func_445_d(var2, var3 + 1, var4);
      }
   }

   private static void func_21166_a(EntityLiving var0, World var1, float var2, float var3, float var4) {
      if (var0 instanceof EntitySpider && var1.field_803_m.nextInt(100) == 0) {
         EntitySkeleton var5 = new EntitySkeleton(var1);
         var5.func_107_c((double)var2, (double)var3, (double)var4, var0.field_316_r, 0.0F);
         var1.func_526_a(var5);
         var5.func_6094_e(var0);
      } else if (var0 instanceof EntitySheep) {
         ((EntitySheep)var0).func_21070_a(EntitySheep.func_21066_a(var1.field_803_m));
      }

   }

   public static boolean func_22212_a(World var0, List var1) {
      boolean var2 = false;
      Pathfinder var3 = new Pathfinder(var0);

      for(EntityPlayer var5 : var1) {
         Class[] var6 = field_22213_a;
         if (var6 != null && var6.length != 0) {
            boolean var7 = false;

            for(int var8 = 0; var8 < 20 && !var7; ++var8) {
               int var9 = MathHelper.func_584_b(var5.field_322_l) + var0.field_803_m.nextInt(32) - var0.field_803_m.nextInt(32);
               int var10 = MathHelper.func_584_b(var5.field_320_n) + var0.field_803_m.nextInt(32) - var0.field_803_m.nextInt(32);
               int var11 = MathHelper.func_584_b(var5.field_321_m) + var0.field_803_m.nextInt(16) - var0.field_803_m.nextInt(16);
               if (var11 < 1) {
                  var11 = 1;
               } else if (var11 > 128) {
                  var11 = 128;
               }

               int var12 = var0.field_803_m.nextInt(var6.length);

               int var13;
               for(var13 = var11; var13 > 2 && !var0.func_445_d(var9, var13 - 1, var10); --var13) {
                  ;
               }

               while(!func_21167_a(EnumCreatureType.monster, var0, var9, var13, var10) && var13 < var11 + 16 && var13 < 128) {
                  ++var13;
               }

               if (var13 < var11 + 16 && var13 < 128) {
                  float var14 = (float)var9 + 0.5F;
                  float var15 = (float)var13;
                  float var16 = (float)var10 + 0.5F;

                  EntityLiving var17;
                  try {
                     var17 = (EntityLiving)var6[var12].getConstructor(World.class).newInstance(var0);
                  } catch (Exception var21) {
                     var21.printStackTrace();
                     return var2;
                  }

                  var17.func_107_c((double)var14, (double)var15, (double)var16, var0.field_803_m.nextFloat() * 360.0F, 0.0F);
                  if (var17.func_155_a()) {
                     PathEntity var18 = var3.func_657_a(var17, var5, 32.0F);
                     if (var18 != null && var18.field_1005_a > 1) {
                        PathPoint var19 = var18.func_22211_c();
                        if (Math.abs((double)var19.field_1016_a - var5.field_322_l) < 1.5D && Math.abs((double)var19.field_1014_c - var5.field_320_n) < 1.5D && Math.abs((double)var19.field_1015_b - var5.field_321_m) < 1.5D) {
                           ChunkCoordinates var20 = BlockBed.func_22021_g(var0, MathHelper.func_584_b(var5.field_322_l), MathHelper.func_584_b(var5.field_321_m), MathHelper.func_584_b(var5.field_320_n), 1);
                           if (var20 == null) {
                              var20 = new ChunkCoordinates(var9, var13 + 1, var10);
                           }

                           var17.func_107_c((double)((float)var20.field_22216_a + 0.5F), (double)var20.field_529_a, (double)((float)var20.field_528_b + 0.5F), 0.0F, 0.0F);
                           var0.func_526_a(var17);
                           func_21166_a(var17, var0, (float)var20.field_22216_a + 0.5F, (float)var20.field_529_a, (float)var20.field_528_b + 0.5F);
                           var5.func_22062_a(true, false, false);
                           var17.func_22056_G();
                           var2 = true;
                           var7 = true;
                        }
                     }
                  }
               }
            }
         }
      }

      return var2;
   }
}
