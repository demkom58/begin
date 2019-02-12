package com.jcraft.jorbis;

class Drft {
   int n;
   float[] trigcache;
   int[] splitcache;
   static int[] ntryh = new int[]{4, 2, 3, 5};
   static float tpi = 6.2831855F;
   static float hsqt2 = 0.70710677F;
   static float taui = 0.8660254F;
   static float taur = -0.5F;
   static float sqrt2 = 1.4142135F;

   void backward(float[] var1) {
      if (this.n != 1) {
         drftb1(this.n, var1, this.trigcache, this.trigcache, this.n, this.splitcache);
      }
   }

   void init(int var1) {
      this.n = var1;
      this.trigcache = new float[3 * var1];
      this.splitcache = new int[32];
      fdrffti(var1, this.trigcache, this.splitcache);
   }

   void clear() {
      if (this.trigcache != null) {
         this.trigcache = null;
      }

      if (this.splitcache != null) {
         this.splitcache = null;
      }

   }

   static void drfti1(int var0, float[] var1, int var2, int[] var3) {
      int var8 = 0;
      int var10 = -1;
      int var24 = var0;
      int var25 = 0;
      byte var26 = 101;

      while(true) {
         switch(var26) {
         case 101:
            ++var10;
            if (var10 < 4) {
               var8 = ntryh[var10];
            } else {
               var8 += 2;
            }
         case 104:
            int var19 = var24 / var8;
            int var20 = var24 - var8 * var19;
            if (var20 != 0) {
               var26 = 101;
               break;
            } else {
               ++var25;
               var3[var25 + 1] = var8;
               var24 = var19;
               if (var8 != 2) {
                  var26 = 107;
                  break;
               } else if (var25 == 1) {
                  var26 = 107;
                  break;
               } else {
                  for(int var9 = 1; var9 < var25; ++var9) {
                     int var14 = var25 - var9 + 1;
                     var3[var14 + 1] = var3[var14];
                  }

                  var3[2] = 2;
               }
            }
         case 107:
            if (var24 == 1) {
               var3[0] = var0;
               var3[1] = var25;
               float var5 = tpi / (float)var0;
               int var18 = 0;
               int var23 = var25 - 1;
               int var12 = 1;
               if (var23 == 0) {
                  return;
               }

               for(int var11 = 0; var11 < var23; ++var11) {
                  int var17 = var3[var11 + 2];
                  int var15 = 0;
                  int var13 = var12 * var17;
                  int var21 = var0 / var13;
                  int var22 = var17 - 1;

                  for(int var29 = 0; var29 < var22; ++var29) {
                     var15 += var12;
                     int var27 = var18;
                     float var6 = (float)var15 * var5;
                     float var7 = 0.0F;

                     for(int var16 = 2; var16 < var21; var16 += 2) {
                        ++var7;
                        float var4 = var7 * var6;
                        var1[var2 + var27++] = (float)Math.cos((double)var4);
                        var1[var2 + var27++] = (float)Math.sin((double)var4);
                     }

                     var18 += var21;
                  }

                  var12 = var13;
               }

               return;
            }

            var26 = 104;
         }
      }
   }

   static void fdrffti(int var0, float[] var1, int[] var2) {
      if (var0 != 1) {
         drfti1(var0, var1, var0, var2);
      }
   }

   static void dradf2(int var0, int var1, float[] var2, float[] var3, float[] var4, int var5) {
      int var11 = 0;
      int var12;
      int var10 = var12 = var1 * var0;
      int var13 = var0 << 1;

      for(int var7 = 0; var7 < var1; ++var7) {
         var3[var11 << 1] = var2[var11] + var2[var12];
         var3[(var11 << 1) + var13 - 1] = var2[var11] - var2[var12];
         var11 += var0;
         var12 += var0;
      }

      if (var0 >= 2) {
         if (var0 != 2) {
            var11 = 0;
            var12 = var10;

            for(int var17 = 0; var17 < var1; ++var17) {
               var13 = var12;
               int var14 = (var11 << 1) + (var0 << 1);
               int var15 = var11;
               int var16 = var11 + var11;

               for(int var6 = 2; var6 < var0; var6 += 2) {
                  var13 += 2;
                  var14 -= 2;
                  var15 += 2;
                  var16 += 2;
                  float var9 = var4[var5 + var6 - 2] * var2[var13 - 1] + var4[var5 + var6 - 1] * var2[var13];
                  float var8 = var4[var5 + var6 - 2] * var2[var13] - var4[var5 + var6 - 1] * var2[var13 - 1];
                  var3[var16] = var2[var15] + var8;
                  var3[var14] = var8 - var2[var15];
                  var3[var16 - 1] = var2[var15 - 1] + var9;
                  var3[var14 - 1] = var2[var15 - 1] - var9;
               }

               var11 += var0;
               var12 += var0;
            }

            if (var0 % 2 == 1) {
               return;
            }
         }

         var11 = var0;
         var13 = var12 = var0 - 1;
         var12 = var12 + var10;

         for(int var18 = 0; var18 < var1; ++var18) {
            var3[var11] = -var2[var12];
            var3[var11 - 1] = var2[var13];
            var11 += var0 << 1;
            var12 += var0;
            var13 += var0;
         }

      }
   }

   static void dradf4(int var0, int var1, float[] var2, float[] var3, float[] var4, int var5, float[] var6, int var7, float[] var8, int var9) {
      int var12 = var1 * var0;
      int var13 = var12;
      int var16 = var12 << 1;
      int var14 = var12 + (var12 << 1);
      int var15 = 0;

      for(int var11 = 0; var11 < var1; ++var11) {
         float var29 = var2[var13] + var2[var14];
         float var30 = var2[var15] + var2[var16];
         int var17;
         var3[var17 = var15 << 2] = var29 + var30;
         var3[(var0 << 2) + var17 - 1] = var30 - var29;
         int var45;
         var3[(var45 = var17 + (var0 << 1)) - 1] = var2[var15] - var2[var16];
         var3[var45] = var2[var14] - var2[var13];
         var13 += var0;
         var14 += var0;
         var15 += var0;
         var16 += var0;
      }

      if (var0 >= 2) {
         if (var0 != 2) {
            var13 = 0;

            for(int var33 = 0; var33 < var1; ++var33) {
               var14 = var13;
               var16 = var13 << 2;
               int var18;
               int var46 = (var18 = var0 << 1) + var16;

               for(int var10 = 2; var10 < var0; var10 += 2) {
                  var14 += 2;
                  var16 += 2;
                  var46 -= 2;
                  var15 = var14 + var12;
                  float var22 = var4[var5 + var10 - 2] * var2[var15 - 1] + var4[var5 + var10 - 1] * var2[var15];
                  float var19 = var4[var5 + var10 - 2] * var2[var15] - var4[var5 + var10 - 1] * var2[var15 - 1];
                  var15 = var15 + var12;
                  float var23 = var6[var7 + var10 - 2] * var2[var15 - 1] + var6[var7 + var10 - 1] * var2[var15];
                  float var20 = var6[var7 + var10 - 2] * var2[var15] - var6[var7 + var10 - 1] * var2[var15 - 1];
                  var15 = var15 + var12;
                  float var24 = var8[var9 + var10 - 2] * var2[var15 - 1] + var8[var9 + var10 - 1] * var2[var15];
                  float var21 = var8[var9 + var10 - 2] * var2[var15] - var8[var9 + var10 - 1] * var2[var15 - 1];
                  float var50 = var22 + var24;
                  float var32 = var24 - var22;
                  float var25 = var19 + var21;
                  float var28 = var19 - var21;
                  float var26 = var2[var14] + var20;
                  float var27 = var2[var14] - var20;
                  float var52 = var2[var14 - 1] + var23;
                  float var31 = var2[var14 - 1] - var23;
                  var3[var16 - 1] = var50 + var52;
                  var3[var16] = var25 + var26;
                  var3[var46 - 1] = var31 - var28;
                  var3[var46] = var32 - var27;
                  var3[var16 + var18 - 1] = var28 + var31;
                  var3[var16 + var18] = var32 + var27;
                  var3[var46 + var18 - 1] = var52 - var50;
                  var3[var46 + var18] = var25 - var26;
               }

               var13 += var0;
            }

            if ((var0 & 1) != 0) {
               return;
            }
         }

         var14 = (var13 = var12 + var0 - 1) + (var12 << 1);
         var15 = var0 << 2;
         var16 = var0;
         int var47 = var0 << 1;
         int var48 = var0;

         for(int var34 = 0; var34 < var1; ++var34) {
            float var49 = -hsqt2 * (var2[var13] + var2[var14]);
            float var51 = hsqt2 * (var2[var13] - var2[var14]);
            var3[var16 - 1] = var51 + var2[var48 - 1];
            var3[var16 + var47 - 1] = var2[var48 - 1] - var51;
            var3[var16] = var49 - var2[var13 + var12];
            var3[var16 + var47] = var49 + var2[var13 + var12];
            var13 += var0;
            var14 += var0;
            var16 += var15;
            var48 += var0;
         }

      }
   }

   static void dradfg(int var0, int var1, int var2, int var3, float[] var4, float[] var5, float[] var6, float[] var7, float[] var8, float[] var9, int var10) {
      int var22 = 0;
      float var38 = 0.0F;
      float var40 = 0.0F;
      float var39 = tpi / (float)var1;
      var38 = (float)Math.cos((double)var39);
      var40 = (float)Math.sin((double)var39);
      int var12 = var1 + 1 >> 1;
      int var44 = var1;
      int var43 = var0;
      int var37 = var0 - 1 >> 1;
      int var20 = var2 * var0;
      int var30 = var1 * var0;
      short var45 = 100;

      while(true) {
         switch(var45) {
         case 101:
            if (var0 == 1) {
               var45 = 119;
               break;
            } else {
               for(int var18 = 0; var18 < var3; ++var18) {
                  var8[var18] = var6[var18];
               }

               int var81 = 0;

               for(int var56 = 1; var56 < var1; ++var56) {
                  var81 += var20;
                  var22 = var81;

                  for(int var67 = 0; var67 < var2; ++var67) {
                     var7[var22] = var5[var22];
                     var22 += var0;
                  }
               }

               int var19 = -var0;
               var81 = 0;
               if (var37 > var2) {
                  for(int var58 = 1; var58 < var1; ++var58) {
                     var81 += var20;
                     var19 += var0;
                     var22 = -var0 + var81;

                     for(int var69 = 0; var69 < var2; ++var69) {
                        int var46 = var19 - 1;
                        var22 += var0;
                        int var97 = var22;

                        for(int var50 = 2; var50 < var0; var50 += 2) {
                           var46 += 2;
                           var97 += 2;
                           var7[var97 - 1] = var9[var10 + var46 - 1] * var5[var97 - 1] + var9[var10 + var46] * var5[var97];
                           var7[var97] = var9[var10 + var46 - 1] * var5[var97] - var9[var10 + var46] * var5[var97 - 1];
                        }
                     }
                  }
               } else {
                  for(int var57 = 1; var57 < var1; ++var57) {
                     var19 += var0;
                     int var11 = var19 - 1;
                     var81 += var20;
                     var22 = var81;

                     for(int var49 = 2; var49 < var0; var49 += 2) {
                        var11 += 2;
                        var22 += 2;
                        int var96 = var22;

                        for(int var68 = 0; var68 < var2; ++var68) {
                           var7[var96 - 1] = var9[var10 + var11 - 1] * var5[var96 - 1] + var9[var10 + var11] * var5[var96];
                           var7[var96] = var9[var10 + var11 - 1] * var5[var96] - var9[var10 + var11] * var5[var96 - 1];
                           var96 += var0;
                        }
                     }
                  }
               }

               var81 = 0;
               var22 = var44 * var20;
               if (var37 < var2) {
                  for(int var59 = 1; var59 < var12; ++var59) {
                     var81 += var20;
                     var22 -= var20;
                     int var98 = var81;
                     int var105 = var22;

                     for(int var51 = 2; var51 < var0; var51 += 2) {
                        var98 += 2;
                        var105 += 2;
                        int var113 = var98 - var0;
                        int var119 = var105 - var0;

                        for(int var70 = 0; var70 < var2; ++var70) {
                           var113 += var0;
                           var119 += var0;
                           var5[var113 - 1] = var7[var113 - 1] + var7[var119 - 1];
                           var5[var119 - 1] = var7[var113] - var7[var119];
                           var5[var113] = var7[var113] + var7[var119];
                           var5[var119] = var7[var119 - 1] - var7[var113 - 1];
                        }
                     }
                  }
               } else {
                  for(int var60 = 1; var60 < var12; ++var60) {
                     var81 += var20;
                     var22 -= var20;
                     int var99 = var81;
                     int var106 = var22;

                     for(int var71 = 0; var71 < var2; ++var71) {
                        int var114 = var99;
                        int var120 = var106;

                        for(int var52 = 2; var52 < var0; var52 += 2) {
                           var114 += 2;
                           var120 += 2;
                           var5[var114 - 1] = var7[var114 - 1] + var7[var120 - 1];
                           var5[var120 - 1] = var7[var114] - var7[var120];
                           var5[var114] = var7[var114] + var7[var120];
                           var5[var120] = var7[var120 - 1] - var7[var114 - 1];
                        }

                        var99 += var0;
                        var106 += var0;
                     }
                  }
               }
            }
         case 119:
            for(int var74 = 0; var74 < var3; ++var74) {
               var6[var74] = var8[var74];
            }

            int var84 = 0;
            var22 = var44 * var3;

            for(int var61 = 1; var61 < var12; ++var61) {
               var84 += var20;
               var22 -= var20;
               int var100 = var84 - var0;
               int var107 = var22 - var0;

               for(int var72 = 0; var72 < var2; ++var72) {
                  var100 += var0;
                  var107 += var0;
                  var5[var100] = var7[var100] + var7[var107];
                  var5[var107] = var7[var107] - var7[var100];
               }
            }

            float var34 = 1.0F;
            float var32 = 0.0F;
            var84 = 0;
            var22 = var44 * var3;
            int var101 = (var1 - 1) * var3;

            for(int var16 = 1; var16 < var12; ++var16) {
               var84 += var3;
               var22 -= var3;
               float var41 = var38 * var34 - var40 * var32;
               var32 = var38 * var32 + var40 * var34;
               var34 = var41;
               int var108 = var84;
               int var115 = var22;
               int var121 = var101;
               int var125 = var3;

               for(int var75 = 0; var75 < var3; ++var75) {
                  var8[var108++] = var6[var75] + var34 * var6[var125++];
                  var8[var115++] = var32 * var6[var121++];
               }

               float var31 = var34;
               float var36 = var32;
               float var35 = var34;
               float var33 = var32;
               var108 = var3;
               var115 = (var44 - 1) * var3;

               for(int var62 = 2; var62 < var12; ++var62) {
                  var108 += var3;
                  var115 -= var3;
                  float var42 = var31 * var35 - var36 * var33;
                  var33 = var31 * var33 + var36 * var35;
                  var35 = var42;
                  var121 = var84;
                  var125 = var22;
                  int var128 = var108;
                  int var130 = var115;

                  for(int var76 = 0; var76 < var3; ++var76) {
                     int var134 = var121++;
                     var8[var134] += var35 * var6[var128++];
                     var134 = var125++;
                     var8[var134] += var33 * var6[var130++];
                  }
               }
            }

            var84 = 0;

            for(int var63 = 1; var63 < var12; ++var63) {
               var84 += var3;
               var22 = var84;

               for(int var77 = 0; var77 < var3; ++var77) {
                  var8[var77] += var6[var22++];
               }
            }

            if (var0 < var2) {
               var45 = 132;
               break;
            }

            var84 = 0;
            var22 = 0;

            for(int var73 = 0; var73 < var2; ++var73) {
               var101 = var84;
               int var110 = var22;

               for(int var53 = 0; var53 < var0; ++var53) {
                  var4[var110++] = var7[var101++];
               }

               var84 += var0;
               var22 += var30;
            }

            var45 = 135;
            break;
         case 132:
            for(int var47 = 0; var47 < var0; ++var47) {
               int var78 = var47;
               var22 = var47;

               for(int var64 = 0; var64 < var2; ++var64) {
                  var4[var22] = var7[var78];
                  var78 += var0;
                  var22 += var30;
               }
            }
         case 135:
            int var79 = 0;
            var22 = var0 << 1;
            int var94 = 0;
            int var103 = var44 * var20;

            for(int var54 = 1; var54 < var12; ++var54) {
               var79 += var22;
               var94 += var20;
               var103 -= var20;
               int var111 = var79;
               int var117 = var94;
               int var123 = var103;

               for(int var65 = 0; var65 < var2; ++var65) {
                  var4[var111 - 1] = var7[var117];
                  var4[var111] = var7[var123];
                  var111 += var30;
                  var117 += var0;
                  var123 += var0;
               }
            }

            if (var0 == 1) {
               return;
            }

            if (var37 >= var2) {
               var79 = -var0;
               var94 = 0;
               var103 = 0;
               int var112 = var44 * var20;

               for(int var55 = 1; var55 < var12; ++var55) {
                  var79 += var22;
                  var94 += var22;
                  var103 += var20;
                  var112 -= var20;
                  int var118 = var79;
                  int var124 = var94;
                  int var127 = var103;
                  int var129 = var112;

                  for(int var66 = 0; var66 < var2; ++var66) {
                     for(int var48 = 2; var48 < var0; var48 += 2) {
                        int var17 = var43 - var48;
                        var4[var48 + var124 - 1] = var7[var48 + var127 - 1] + var7[var48 + var129 - 1];
                        var4[var17 + var118 - 1] = var7[var48 + var127 - 1] - var7[var48 + var129 - 1];
                        var4[var48 + var124] = var7[var48 + var127] + var7[var48 + var129];
                        var4[var17 + var118] = var7[var48 + var129] - var7[var48 + var127];
                     }

                     var118 += var30;
                     var124 += var30;
                     var127 += var0;
                     var129 += var0;
                  }
               }

               return;
            }

            var45 = 141;
            break;
         case 141:
            int var21 = -var0;
            int var23 = 0;
            int var24 = 0;
            int var25 = var44 * var20;

            for(int var14 = 1; var14 < var12; ++var14) {
               var21 += var22;
               var23 += var22;
               var24 += var20;
               var25 -= var20;

               for(int var13 = 2; var13 < var0; var13 += 2) {
                  int var26 = var43 + var21 - var13;
                  int var27 = var13 + var23;
                  int var28 = var13 + var24;
                  int var29 = var13 + var25;

                  for(int var15 = 0; var15 < var2; ++var15) {
                     var4[var27 - 1] = var7[var28 - 1] + var7[var29 - 1];
                     var4[var26 - 1] = var7[var28 - 1] - var7[var29 - 1];
                     var4[var27] = var7[var28] + var7[var29];
                     var4[var26] = var7[var29] - var7[var28];
                     var26 += var30;
                     var27 += var30;
                     var28 += var0;
                     var29 += var0;
                  }
               }
            }

            return;
         }
      }
   }

   static void drftf1(int var0, float[] var1, float[] var2, float[] var3, int[] var4) {
      int var11 = var4[1];
      int var9 = 1;
      int var8 = var0;
      int var13 = var0;

      for(int var6 = 0; var6 < var11; ++var6) {
         int var10 = var11 - var6;
         int var12 = var4[var10 + 1];
         int var7 = var8 / var12;
         int var14 = var0 / var8;
         int var15 = var14 * var7;
         var13 -= (var12 - 1) * var14;
         var9 = 1 - var9;
         byte var18 = 100;

         label62:
         while(true) {
            switch(var18) {
            case 100:
               if (var12 != 4) {
                  var18 = 102;
               } else {
                  int var16 = var13 + var14;
                  int var17 = var16 + var14;
                  if (var9 != 0) {
                     dradf4(var14, var7, var2, var1, var3, var13 - 1, var3, var16 - 1, var3, var17 - 1);
                  } else {
                     dradf4(var14, var7, var1, var2, var3, var13 - 1, var3, var16 - 1, var3, var17 - 1);
                  }

                  var18 = 110;
               }
            case 101:
            case 105:
            case 106:
            case 107:
            case 108:
            default:
               break;
            case 102:
               if (var12 != 2) {
                  var18 = 104;
               } else if (var9 != 0) {
                  var18 = 103;
               } else {
                  dradf2(var14, var7, var1, var2, var3, var13 - 1);
                  var18 = 110;
               }
               break;
            case 103:
               dradf2(var14, var7, var2, var1, var3, var13 - 1);
            case 104:
               if (var14 == 1) {
                  var9 = 1 - var9;
               }

               if (var9 != 0) {
                  var18 = 109;
               } else {
                  dradfg(var14, var12, var7, var15, var1, var1, var1, var2, var2, var3, var13 - 1);
                  var9 = 1;
                  var18 = 110;
               }
               break;
            case 109:
               dradfg(var14, var12, var7, var15, var2, var2, var2, var1, var1, var3, var13 - 1);
               var9 = 0;
            case 110:
               break label62;
            }
         }

         var8 = var7;
      }

      if (var9 != 1) {
         for(int var5 = 0; var5 < var0; ++var5) {
            var1[var5] = var2[var5];
         }

      }
   }

   static void dradb2(int var0, int var1, float[] var2, float[] var3, float[] var4, int var5) {
      int var8 = var1 * var0;
      int var9 = 0;
      int var10 = 0;
      int var11 = (var0 << 1) - 1;

      for(int var7 = 0; var7 < var1; ++var7) {
         var3[var9] = var2[var10] + var2[var11 + var10];
         var3[var9 + var8] = var2[var10] - var2[var11 + var10];
         var10 = (var9 += var0) << 1;
      }

      if (var0 >= 2) {
         if (var0 != 2) {
            var9 = 0;
            var10 = 0;

            for(int var17 = 0; var17 < var1; ++var17) {
               var11 = var9;
               int var12 = var10;
               int var13 = var10 + (var0 << 1);
               int var14 = var8 + var9;

               for(int var6 = 2; var6 < var0; var6 += 2) {
                  var11 += 2;
                  var12 += 2;
                  var13 -= 2;
                  var14 += 2;
                  var3[var11 - 1] = var2[var12 - 1] + var2[var13 - 1];
                  float var16 = var2[var12 - 1] - var2[var13 - 1];
                  var3[var11] = var2[var12] - var2[var13];
                  float var15 = var2[var12] + var2[var13];
                  var3[var14 - 1] = var4[var5 + var6 - 2] * var16 - var4[var5 + var6 - 1] * var15;
                  var3[var14] = var4[var5 + var6 - 2] * var15 + var4[var5 + var6 - 1] * var16;
               }

               var10 = (var9 += var0) << 1;
            }

            if (var0 % 2 == 1) {
               return;
            }
         }

         var9 = var0 - 1;
         var10 = var0 - 1;

         for(int var18 = 0; var18 < var1; ++var18) {
            var3[var9] = var2[var10] + var2[var10];
            var3[var9 + var8] = -(var2[var10 + 1] + var2[var10 + 1]);
            var9 += var0;
            var10 += var0 << 1;
         }

      }
   }

   static void dradb3(int var0, int var1, float[] var2, float[] var3, float[] var4, int var5, float[] var6, int var7) {
      int var10 = var1 * var0;
      int var11 = 0;
      int var12 = var10 << 1;
      int var13 = var0 << 1;
      int var14 = var0 + (var0 << 1);
      int var15 = 0;

      for(int var9 = 0; var9 < var1; ++var9) {
         float var30 = var2[var13 - 1] + var2[var13 - 1];
         float var25 = var2[var15] + taur * var30;
         var3[var11] = var2[var15] + var30;
         float var22 = taui * (var2[var13] + var2[var13]);
         var3[var11 + var10] = var25 - var22;
         var3[var11 + var12] = var25 + var22;
         var11 += var0;
         var13 += var14;
         var15 += var14;
      }

      if (var0 != 1) {
         var11 = 0;
         var13 = var0 << 1;

         for(int var31 = 0; var31 < var1; ++var31) {
            int var17 = var11 + (var11 << 1);
            int var16 = var15 = var17 + var13;
            int var18 = var11;
            int var19;
            int var20 = (var19 = var11 + var10) + var10;

            for(int var8 = 2; var8 < var0; var8 += 2) {
               var15 += 2;
               var16 -= 2;
               var17 += 2;
               var18 += 2;
               var19 += 2;
               var20 += 2;
               float var37 = var2[var15 - 1] + var2[var16 - 1];
               float var36 = var2[var17 - 1] + taur * var37;
               var3[var18 - 1] = var2[var17 - 1] + var37;
               float var29 = var2[var15] - var2[var16];
               float var21 = var2[var17] + taur * var29;
               var3[var18] = var2[var17] + var29;
               float var26 = taui * (var2[var15 - 1] - var2[var16 - 1]);
               float var35 = taui * (var2[var15] + var2[var16]);
               float var27 = var36 - var35;
               float var28 = var36 + var35;
               float var23 = var21 + var26;
               float var24 = var21 - var26;
               var3[var19 - 1] = var4[var5 + var8 - 2] * var27 - var4[var5 + var8 - 1] * var23;
               var3[var19] = var4[var5 + var8 - 2] * var23 + var4[var5 + var8 - 1] * var27;
               var3[var20 - 1] = var6[var7 + var8 - 2] * var28 - var6[var7 + var8 - 1] * var24;
               var3[var20] = var6[var7 + var8 - 2] * var24 + var6[var7 + var8 - 1] * var28;
            }

            var11 += var0;
         }

      }
   }

   static void dradb4(int var0, int var1, float[] var2, float[] var3, float[] var4, int var5, float[] var6, int var7, float[] var8, int var9) {
      int var12 = var1 * var0;
      int var13 = 0;
      int var14 = var0 << 2;
      int var15 = 0;
      int var18 = var0 << 1;

      for(int var11 = 0; var11 < var1; ++var11) {
         int var16 = var15 + var18;
         float var33 = var2[var16 - 1] + var2[var16 - 1];
         float var34 = var2[var16] + var2[var16];
         int var43;
         float var31 = var2[var15] - var2[(var43 = var16 + var18) - 1];
         float var32 = var2[var15] + var2[var43 - 1];
         var3[var13] = var32 + var33;
         int var17;
         var3[var17 = var13 + var12] = var31 - var34;
         int var46;
         var3[var46 = var17 + var12] = var32 - var33;
         var3[var46 + var12] = var31 + var34;
         var13 += var0;
         var15 += var14;
      }

      if (var0 >= 2) {
         if (var0 != 2) {
            var13 = 0;

            for(int var35 = 0; var35 < var1; ++var35) {
               int var44;
               int var47 = (var44 = var15 = (var14 = var13 << 2) + var18) + var18;
               int var19 = var13;

               for(int var10 = 2; var10 < var0; var10 += 2) {
                  var14 += 2;
                  var15 += 2;
                  var44 -= 2;
                  var47 -= 2;
                  var19 += 2;
                  float var27 = var2[var14] + var2[var47];
                  float var28 = var2[var14] - var2[var47];
                  float var29 = var2[var15] - var2[var44];
                  float var59 = var2[var15] + var2[var44];
                  float var54 = var2[var14 - 1] - var2[var47 - 1];
                  float var56 = var2[var14 - 1] + var2[var47 - 1];
                  float var30 = var2[var15 - 1] - var2[var44 - 1];
                  float var58 = var2[var15 - 1] + var2[var44 - 1];
                  var3[var19 - 1] = var56 + var58;
                  float var25 = var56 - var58;
                  var3[var19] = var28 + var29;
                  float var22 = var28 - var29;
                  float var24 = var54 - var59;
                  float var26 = var54 + var59;
                  float var21 = var27 + var30;
                  float var23 = var27 - var30;
                  int var20;
                  var3[(var20 = var19 + var12) - 1] = var4[var5 + var10 - 2] * var24 - var4[var5 + var10 - 1] * var21;
                  var3[var20] = var4[var5 + var10 - 2] * var21 + var4[var5 + var10 - 1] * var24;
                  int var50;
                  var3[(var50 = var20 + var12) - 1] = var6[var7 + var10 - 2] * var25 - var6[var7 + var10 - 1] * var22;
                  var3[var50] = var6[var7 + var10 - 2] * var22 + var6[var7 + var10 - 1] * var25;
                  var3[(var20 = var50 + var12) - 1] = var8[var9 + var10 - 2] * var26 - var8[var9 + var10 - 1] * var23;
                  var3[var20] = var8[var9 + var10 - 2] * var23 + var8[var9 + var10 - 1] * var26;
               }

               var13 += var0;
            }

            if (var0 % 2 == 1) {
               return;
            }
         }

         var13 = var0;
         var14 = var0 << 2;
         var15 = var0 - 1;
         int var45 = var0 + (var0 << 1);

         for(int var36 = 0; var36 < var1; ++var36) {
            float var52 = var2[var13] + var2[var45];
            float var53 = var2[var45] - var2[var13];
            float var55 = var2[var13 - 1] - var2[var45 - 1];
            float var57 = var2[var13 - 1] + var2[var45 - 1];
            var3[var15] = var57 + var57;
            int var48;
            var3[var48 = var15 + var12] = sqrt2 * (var55 - var52);
            int var49;
            var3[var49 = var48 + var12] = var53 + var53;
            var3[var49 + var12] = -sqrt2 * (var55 + var52);
            var15 += var0;
            var13 += var14;
            var45 += var14;
         }

      }
   }

   static void dradbg(int var0, int var1, int var2, int var3, float[] var4, float[] var5, float[] var6, float[] var7, float[] var8, float[] var9, int var10) {
      int var12 = 0;
      int var19 = 0;
      int var29 = 0;
      int var38 = 0;
      float var39 = 0.0F;
      float var41 = 0.0F;
      int var44 = 0;
      short var45 = 100;

      while(true) {
         switch(var45) {
         case 100:
            var29 = var1 * var0;
            var19 = var2 * var0;
            float var40 = tpi / (float)var1;
            var39 = (float)Math.cos((double)var40);
            var41 = (float)Math.sin((double)var40);
            var38 = var0 - 1 >>> 1;
            var44 = var1;
            var12 = var1 + 1 >>> 1;
            if (var0 < var2) {
               var45 = 103;
               break;
            }

            int var89 = 0;
            int var101 = 0;

            for(int var73 = 0; var73 < var2; ++var73) {
               int var111 = var89;
               int var119 = var101;

               for(int var53 = 0; var53 < var0; ++var53) {
                  var7[var111] = var4[var119];
                  ++var111;
                  ++var119;
               }

               var89 += var0;
               var101 += var29;
            }

            var45 = 106;
            break;
         case 103:
            int var86 = 0;

            for(int var51 = 0; var51 < var0; ++var51) {
               int var98 = var86;
               int var108 = var86;

               for(int var70 = 0; var70 < var2; ++var70) {
                  var7[var98] = var4[var108];
                  var98 += var0;
                  var108 += var29;
               }

               ++var86;
            }
         case 106:
            int var87 = 0;
            int var99 = var44 * var19;
            int var124;
            int var134 = var124 = var0 << 1;

            for(int var62 = 1; var62 < var12; ++var62) {
               var87 += var19;
               var99 -= var19;
               int var109 = var87;
               int var117 = var99;
               int var130 = var124;

               for(int var71 = 0; var71 < var2; ++var71) {
                  var7[var109] = var4[var130 - 1] + var4[var130 - 1];
                  var7[var117] = var4[var130] + var4[var130];
                  var109 += var0;
                  var117 += var0;
                  var130 += var29;
               }

               var124 += var134;
            }

            if (var0 == 1) {
               var45 = 116;
            } else {
               if (var38 < var2) {
                  var45 = 112;
                  break;
               }

               var87 = 0;
               var99 = var44 * var19;
               var134 = 0;

               for(int var63 = 1; var63 < var12; ++var63) {
                  var87 += var19;
                  var99 -= var19;
                  int var110 = var87;
                  int var118 = var99;
                  var134 += var0 << 1;
                  int var137 = var134;

                  for(int var72 = 0; var72 < var2; ++var72) {
                     var124 = var110;
                     int var131 = var118;
                     int var139 = var137;
                     int var141 = var137;

                     for(int var52 = 2; var52 < var0; var52 += 2) {
                        var124 += 2;
                        var131 += 2;
                        var139 += 2;
                        var141 -= 2;
                        var7[var124 - 1] = var4[var139 - 1] + var4[var141 - 1];
                        var7[var131 - 1] = var4[var139 - 1] - var4[var141 - 1];
                        var7[var124] = var4[var139] - var4[var141];
                        var7[var131] = var4[var139] + var4[var141];
                     }

                     var110 += var0;
                     var118 += var0;
                     var137 += var29;
                  }
               }

               var45 = 116;
            }
            break;
         case 112:
            int var81 = 0;
            int var93 = var44 * var19;
            int var26 = 0;

            for(int var57 = 1; var57 < var12; ++var57) {
               var81 += var19;
               var93 -= var19;
               int var104 = var81;
               int var112 = var93;
               var26 += var0 << 1;
               int var27 = var26;
               int var28 = var26;

               for(int var49 = 2; var49 < var0; var49 += 2) {
                  var104 += 2;
                  var112 += 2;
                  var27 += 2;
                  var28 -= 2;
                  int var120 = var104;
                  int var126 = var112;
                  int var30 = var27;
                  int var31 = var28;

                  for(int var67 = 0; var67 < var2; ++var67) {
                     var7[var120 - 1] = var4[var30 - 1] + var4[var31 - 1];
                     var7[var126 - 1] = var4[var30 - 1] - var4[var31 - 1];
                     var7[var120] = var4[var30] - var4[var31];
                     var7[var126] = var4[var30] + var4[var31];
                     var120 += var0;
                     var126 += var0;
                     var30 += var29;
                     var31 += var29;
                  }
               }
            }
         case 116:
            float var35 = 1.0F;
            float var33 = 0.0F;
            int var82 = 0;
            int var94;
            int var138 = var94 = var44 * var3;
            int var105 = (var1 - 1) * var3;

            for(int var16 = 1; var16 < var12; ++var16) {
               var82 += var3;
               var94 -= var3;
               float var42 = var39 * var35 - var41 * var33;
               var33 = var39 * var33 + var41 * var35;
               var35 = var42;
               int var113 = var82;
               int var121 = var94;
               int var127 = 0;
               int var132 = var3;
               int var136 = var105;

               for(int var74 = 0; var74 < var3; ++var74) {
                  var6[var113++] = var8[var127++] + var35 * var8[var132++];
                  var6[var121++] = var33 * var8[var136++];
               }

               float var32 = var35;
               float var37 = var33;
               float var36 = var35;
               float var34 = var33;
               var127 = var3;
               var132 = var138 - var3;

               for(int var58 = 2; var58 < var12; ++var58) {
                  var127 += var3;
                  var132 -= var3;
                  float var43 = var32 * var36 - var37 * var34;
                  var34 = var32 * var34 + var37 * var36;
                  var36 = var43;
                  var113 = var82;
                  var121 = var94;
                  int var140 = var127;
                  int var142 = var132;

                  for(int var75 = 0; var75 < var3; ++var75) {
                     int var144 = var113++;
                     var6[var144] += var36 * var8[var140++];
                     var144 = var121++;
                     var6[var144] += var34 * var8[var142++];
                  }
               }
            }

            var82 = 0;

            for(int var59 = 1; var59 < var12; ++var59) {
               var82 += var3;
               var94 = var82;

               for(int var76 = 0; var76 < var3; ++var76) {
                  var8[var76] += var8[var94++];
               }
            }

            var82 = 0;
            var94 = var44 * var19;

            for(int var60 = 1; var60 < var12; ++var60) {
               var82 += var19;
               var94 -= var19;
               var105 = var82;
               int var115 = var94;

               for(int var68 = 0; var68 < var2; ++var68) {
                  var7[var105] = var5[var105] - var5[var115];
                  var7[var115] = var5[var105] + var5[var115];
                  var105 += var0;
                  var115 += var0;
               }
            }

            if (var0 == 1) {
               var45 = 132;
            } else {
               if (var38 < var2) {
                  var45 = 128;
                  break;
               }

               var82 = 0;
               var94 = var44 * var19;

               for(int var61 = 1; var61 < var12; ++var61) {
                  var82 += var19;
                  var94 -= var19;
                  var105 = var82;
                  int var116 = var94;

                  for(int var69 = 0; var69 < var2; ++var69) {
                     int var123 = var105;
                     int var129 = var116;

                     for(int var50 = 2; var50 < var0; var50 += 2) {
                        var123 += 2;
                        var129 += 2;
                        var7[var123 - 1] = var5[var123 - 1] - var5[var129];
                        var7[var129 - 1] = var5[var123 - 1] + var5[var129];
                        var7[var123] = var5[var123] + var5[var129 - 1];
                        var7[var129] = var5[var123] - var5[var129 - 1];
                     }

                     var105 += var0;
                     var116 += var0;
                  }
               }

               var45 = 132;
            }
            break;
         case 128:
            int var78 = 0;
            int var90 = var44 * var19;

            for(int var54 = 1; var54 < var12; ++var54) {
               var78 += var19;
               var90 -= var19;
               int var102 = var78;
               int var23 = var90;

               for(int var47 = 2; var47 < var0; var47 += 2) {
                  var102 += 2;
                  var23 += 2;
                  int var24 = var102;
                  int var25 = var23;

                  for(int var64 = 0; var64 < var2; ++var64) {
                     var7[var24 - 1] = var5[var24 - 1] - var5[var25];
                     var7[var25 - 1] = var5[var24 - 1] + var5[var25];
                     var7[var24] = var5[var24] + var5[var25 - 1];
                     var7[var25] = var5[var24] - var5[var25 - 1];
                     var24 += var0;
                     var25 += var0;
                  }
               }
            }
         case 132:
            if (var0 == 1) {
               return;
            }

            for(int var17 = 0; var17 < var3; ++var17) {
               var6[var17] = var8[var17];
            }

            int var79 = 0;

            for(int var55 = 1; var55 < var1; ++var55) {
               int var91 = var79 += var19;

               for(int var65 = 0; var65 < var2; ++var65) {
                  var5[var91] = var7[var91];
                  var91 += var0;
               }
            }

            if (var38 <= var2) {
               int var77 = -var0 - 1;
               var79 = 0;

               for(int var56 = 1; var56 < var1; ++var56) {
                  var77 += var0;
                  var79 += var19;
                  int var46 = var77;
                  int var92 = var79;

                  for(int var48 = 2; var48 < var0; var48 += 2) {
                     var92 += 2;
                     var46 += 2;
                     int var103 = var92;

                     for(int var66 = 0; var66 < var2; ++var66) {
                        var5[var103 - 1] = var9[var10 + var46 - 1] * var7[var103 - 1] - var9[var10 + var46] * var7[var103];
                        var5[var103] = var9[var10 + var46 - 1] * var7[var103] + var9[var10 + var46] * var7[var103 - 1];
                        var103 += var0;
                     }
                  }
               }

               return;
            }

            var45 = 139;
            break;
         case 139:
            int var18 = -var0 - 1;
            int var20 = 0;

            for(int var14 = 1; var14 < var1; ++var14) {
               var18 += var0;
               var20 += var19;
               int var21 = var20;

               for(int var15 = 0; var15 < var2; ++var15) {
                  int var11 = var18;
                  int var22 = var21;

                  for(int var13 = 2; var13 < var0; var13 += 2) {
                     var11 += 2;
                     var22 += 2;
                     var5[var22 - 1] = var9[var10 + var11 - 1] * var7[var22 - 1] - var9[var10 + var11] * var7[var22];
                     var5[var22] = var9[var10 + var11 - 1] * var7[var22] + var9[var10 + var11] * var7[var22 - 1];
                  }

                  var21 += var0;
               }
            }

            return;
         }
      }
   }

   static void drftb1(int var0, float[] var1, float[] var2, float[] var3, int var4, int[] var5) {
      int var9 = 0;
      int var12 = 0;
      int var16 = 0;
      int var17 = 0;
      int var11 = var5[1];
      int var10 = 0;
      int var8 = 1;
      int var13 = 1;

      for(int var7 = 0; var7 < var11; ++var7) {
         byte var18 = 100;

         label71:
         while(true) {
            switch(var18) {
            case 100:
               var12 = var5[var7 + 2];
               var9 = var12 * var8;
               var16 = var0 / var9;
               var17 = var16 * var8;
               if (var12 != 4) {
                  var18 = 103;
               } else {
                  int var19 = var13 + var16;
                  int var15 = var19 + var16;
                  if (var10 != 0) {
                     dradb4(var16, var8, var2, var1, var3, var4 + var13 - 1, var3, var4 + var19 - 1, var3, var4 + var15 - 1);
                  } else {
                     dradb4(var16, var8, var1, var2, var3, var4 + var13 - 1, var3, var4 + var19 - 1, var3, var4 + var15 - 1);
                  }

                  var10 = 1 - var10;
                  var18 = 115;
               }
               break;
            case 103:
               if (var12 != 2) {
                  var18 = 106;
               } else {
                  if (var10 != 0) {
                     dradb2(var16, var8, var2, var1, var3, var4 + var13 - 1);
                  } else {
                     dradb2(var16, var8, var1, var2, var3, var4 + var13 - 1);
                  }

                  var10 = 1 - var10;
                  var18 = 115;
               }
               break;
            case 106:
               if (var12 != 3) {
                  var18 = 109;
               } else {
                  int var14 = var13 + var16;
                  if (var10 != 0) {
                     dradb3(var16, var8, var2, var1, var3, var4 + var13 - 1, var3, var4 + var14 - 1);
                  } else {
                     dradb3(var16, var8, var1, var2, var3, var4 + var13 - 1, var3, var4 + var14 - 1);
                  }

                  var10 = 1 - var10;
                  var18 = 115;
               }
               break;
            case 109:
               if (var10 != 0) {
                  dradbg(var16, var12, var8, var17, var2, var2, var2, var1, var1, var3, var4 + var13 - 1);
               } else {
                  dradbg(var16, var12, var8, var17, var1, var1, var1, var2, var2, var3, var4 + var13 - 1);
               }

               if (var16 == 1) {
                  var10 = 1 - var10;
               }
            case 115:
               break label71;
            }
         }

         var8 = var9;
         var13 += (var12 - 1) * var16;
      }

      if (var10 != 0) {
         for(int var6 = 0; var6 < var0; ++var6) {
            var1[var6] = var2[var6];
         }

      }
   }
}
