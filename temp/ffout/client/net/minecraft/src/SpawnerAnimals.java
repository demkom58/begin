package net.minecraft.src;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SpawnerAnimals {
   private static Set field_6544_a = new HashSet();
   protected static final Class[] field_22391_a = new Class[]{EntitySpider.class, EntityZombie.class, EntitySkeleton.class};

   protected static ChunkPosition func_4153_a(World var0, int var1, int var2) {
      int var3 = var1 + var0.field_1037_n.nextInt(16);
      int var4 = var0.field_1037_n.nextInt(128);
      int var5 = var2 + var0.field_1037_n.nextInt(16);
      return new ChunkPosition(var3, var4, var5);
   }

   public static final int func_4154_a(World var0, boolean var1, boolean var2) {
      if (!var1 && !var2) {
         return 0;
      } else {
         field_6544_a.clear();

         for(int var3 = 0; var3 < var0.field_1040_k.size(); ++var3) {
            EntityPlayer var4 = (EntityPlayer)var0.field_1040_k.get(var3);
            int var5 = MathHelper.func_1108_b(var4.field_611_ak / 16.0D);
            int var6 = MathHelper.func_1108_b(var4.field_609_am / 16.0D);
            byte var7 = 8;

            for(int var8 = -var7; var8 <= var7; ++var8) {
               for(int var9 = -var7; var9 <= var7; ++var9) {
                  field_6544_a.add(new ChunkCoordIntPair(var8 + var5, var9 + var6));
               }
            }
         }

         int var35 = 0;
         ChunkCoordinates var36 = var0.func_22137_s();

         for(EnumCreatureType var40 : EnumCreatureType.values()) {
            if ((!var40.func_21168_d() || var2) && (var40.func_21168_d() || var1) && var0.func_621_b(var40.func_21170_a()) <= var40.func_21169_b() * field_6544_a.size() / 256) {
               label130:
               for(ChunkCoordIntPair var10 : field_6544_a) {
                  BiomeGenBase var11 = var0.func_4075_a().func_4074_a(var10);
                  List var12 = var11.func_25063_a(var40);
                  if (var12 != null && !var12.isEmpty()) {
                     int var13 = 0;

                     for(SpawnListEntry var15 : var12) {
                        var13 += var15.field_25211_b;
                     }

                     int var42 = var0.field_1037_n.nextInt(var13);
                     SpawnListEntry var43 = (SpawnListEntry)var12.get(0);

                     for(SpawnListEntry var17 : var12) {
                        var42 -= var17.field_25211_b;
                        if (var42 < 0) {
                           var43 = var17;
                           break;
                        }
                     }

                     ChunkPosition var44 = func_4153_a(var0, var10.field_189_a * 16, var10.field_188_b * 16);
                     int var45 = var44.field_1111_a;
                     int var18 = var44.field_1110_b;
                     int var19 = var44.field_1112_c;
                     if (!var0.func_28100_h(var45, var18, var19) && var0.func_599_f(var45, var18, var19) == var40.func_21171_c()) {
                        int var20 = 0;

                        for(int var21 = 0; var21 < 3; ++var21) {
                           int var22 = var45;
                           int var23 = var18;
                           int var24 = var19;
                           byte var25 = 6;

                           for(int var26 = 0; var26 < 4; ++var26) {
                              var22 += var0.field_1037_n.nextInt(var25) - var0.field_1037_n.nextInt(var25);
                              var23 += var0.field_1037_n.nextInt(1) - var0.field_1037_n.nextInt(1);
                              var24 += var0.field_1037_n.nextInt(var25) - var0.field_1037_n.nextInt(var25);
                              if (func_21203_a(var40, var0, var22, var23, var24)) {
                                 float var27 = (float)var22 + 0.5F;
                                 float var28 = (float)var23;
                                 float var29 = (float)var24 + 0.5F;
                                 if (var0.func_683_a((double)var27, (double)var28, (double)var29, 24.0D) == null) {
                                    float var30 = var27 - (float)var36.field_22395_a;
                                    float var31 = var28 - (float)var36.field_22394_b;
                                    float var32 = var29 - (float)var36.field_22396_c;
                                    float var33 = var30 * var30 + var31 * var31 + var32 * var32;
                                    if (var33 >= 576.0F) {
                                       try {
                                          var46 = (EntityLiving)var43.field_25212_a.getConstructor(World.class).newInstance(var0);
                                       } catch (Exception var34) {
                                          var34.printStackTrace();
                                          return var35;
                                       }

                                       var46.func_365_c((double)var27, (double)var28, (double)var29, var0.field_1037_n.nextFloat() * 360.0F, 0.0F);
                                       if (var46.func_433_a()) {
                                          ++var20;
                                          var0.func_674_a(var46);
                                          func_21204_a(var46, var0, var27, var28, var29);
                                          if (var20 >= var46.func_6391_i()) {
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

   private static boolean func_21203_a(EnumCreatureType var0, World var1, int var2, int var3, int var4) {
      if (var0.func_21171_c() == Material.field_1332_f) {
         return var1.func_599_f(var2, var3, var4).func_879_d() && !var1.func_28100_h(var2, var3 + 1, var4);
      } else {
         return var1.func_28100_h(var2, var3 - 1, var4) && !var1.func_28100_h(var2, var3, var4) && !var1.func_599_f(var2, var3, var4).func_879_d() && !var1.func_28100_h(var2, var3 + 1, var4);
      }
   }

   private static void func_21204_a(EntityLiving var0, World var1, float var2, float var3, float var4) {
      if (var0 instanceof EntitySpider && var1.field_1037_n.nextInt(100) == 0) {
         EntitySkeleton var5 = new EntitySkeleton(var1);
         var5.func_365_c((double)var2, (double)var3, (double)var4, var0.field_605_aq, 0.0F);
         var1.func_674_a(var5);
         var5.func_6377_h(var0);
      } else if (var0 instanceof EntitySheep) {
         ((EntitySheep)var0).func_21071_b_(EntitySheep.func_21070_a(var1.field_1037_n));
      }

   }

   public static boolean func_22390_a(World var0, List var1) {
      boolean var2 = false;
      Pathfinder var3 = new Pathfinder(var0);

      for(EntityPlayer var5 : var1) {
         Class[] var6 = field_22391_a;
         if (var6 != null && var6.length != 0) {
            boolean var7 = false;

            for(int var8 = 0; var8 < 20 && !var7; ++var8) {
               int var9 = MathHelper.func_1108_b(var5.field_611_ak) + var0.field_1037_n.nextInt(32) - var0.field_1037_n.nextInt(32);
               int var10 = MathHelper.func_1108_b(var5.field_609_am) + var0.field_1037_n.nextInt(32) - var0.field_1037_n.nextInt(32);
               int var11 = MathHelper.func_1108_b(var5.field_610_al) + var0.field_1037_n.nextInt(16) - var0.field_1037_n.nextInt(16);
               if (var11 < 1) {
                  var11 = 1;
               } else if (var11 > 128) {
                  var11 = 128;
               }

               int var12 = var0.field_1037_n.nextInt(var6.length);

               int var13;
               for(var13 = var11; var13 > 2 && !var0.func_28100_h(var9, var13 - 1, var10); --var13) {
                  ;
               }

               while(!func_21203_a(EnumCreatureType.monster, var0, var9, var13, var10) && var13 < var11 + 16 && var13 < 128) {
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

                  var17.func_365_c((double)var14, (double)var15, (double)var16, var0.field_1037_n.nextFloat() * 360.0F, 0.0F);
                  if (var17.func_433_a()) {
                     PathEntity var18 = var3.func_1137_a(var17, var5, 32.0F);
                     if (var18 != null && var18.field_1765_a > 1) {
                        PathPoint var19 = var18.func_22328_c();
                        if (Math.abs((double)var19.field_1718_a - var5.field_611_ak) < 1.5D && Math.abs((double)var19.field_1716_c - var5.field_609_am) < 1.5D && Math.abs((double)var19.field_1717_b - var5.field_610_al) < 1.5D) {
                           ChunkCoordinates var20 = BlockBed.func_22028_g(var0, MathHelper.func_1108_b(var5.field_611_ak), MathHelper.func_1108_b(var5.field_610_al), MathHelper.func_1108_b(var5.field_609_am), 1);
                           if (var20 == null) {
                              var20 = new ChunkCoordinates(var9, var13 + 1, var10);
                           }

                           var17.func_365_c((double)((float)var20.field_22395_a + 0.5F), (double)var20.field_22394_b, (double)((float)var20.field_22396_c + 0.5F), 0.0F, 0.0F);
                           var0.func_674_a(var17);
                           func_21204_a(var17, var0, (float)var20.field_22395_a + 0.5F, (float)var20.field_22394_b, (float)var20.field_22396_c + 0.5F);
                           var5.func_22056_a(true, false, false);
                           var17.func_22050_O();
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
