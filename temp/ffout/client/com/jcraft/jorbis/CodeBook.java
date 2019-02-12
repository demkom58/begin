package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class CodeBook {
   int dim;
   int entries;
   StaticCodeBook c = new StaticCodeBook();
   float[] valuelist;
   int[] codelist;
   CodeBook$DecodeAux decode_tree;
   private int[] t = new int[15];

   int encode(int var1, Buffer var2) {
      var2.write(this.codelist[var1], this.c.lengthlist[var1]);
      return this.c.lengthlist[var1];
   }

   int errorv(float[] var1) {
      int var2 = this.best(var1, 1);

      for(int var3 = 0; var3 < this.dim; ++var3) {
         var1[var3] = this.valuelist[var2 * this.dim + var3];
      }

      return var2;
   }

   int encodev(int var1, float[] var2, Buffer var3) {
      for(int var4 = 0; var4 < this.dim; ++var4) {
         var2[var4] = this.valuelist[var1 * this.dim + var4];
      }

      return this.encode(var1, var3);
   }

   int encodevs(float[] var1, Buffer var2, int var3, int var4) {
      int var5 = this.besterror(var1, var3, var4);
      return this.encode(var5, var2);
   }

   synchronized int decodevs_add(float[] var1, int var2, Buffer var3, int var4) {
      int var5 = var4 / this.dim;
      if (this.t.length < var5) {
         this.t = new int[var5];
      }

      for(int var7 = 0; var7 < var5; ++var7) {
         int var6 = this.decode(var3);
         if (var6 == -1) {
            return -1;
         }

         this.t[var7] = var6 * this.dim;
      }

      int var10 = 0;

      for(int var9 = 0; var10 < this.dim; var9 += var5) {
         for(int var8 = 0; var8 < var5; ++var8) {
            var1[var2 + var9 + var8] += this.valuelist[this.t[var8] + var10];
         }

         ++var10;
      }

      return 0;
   }

   int decodev_add(float[] var1, int var2, Buffer var3, int var4) {
      if (this.dim > 8) {
         int var5 = 0;

         while(var5 < var4) {
            int var7 = this.decode(var3);
            if (var7 == -1) {
               return -1;
            }

            int var8 = var7 * this.dim;

            int var10002;
            for(int var6 = 0; var6 < this.dim; var1[var2 + var10002] += this.valuelist[var8 + var6++]) {
               var10002 = var5++;
            }
         }
      } else {
         int var9 = 0;

         while(var9 < var4) {
            int var12 = this.decode(var3);
            if (var12 == -1) {
               return -1;
            }

            int var13 = var12 * this.dim;
            int var10 = 0;
            switch(this.dim) {
            case 0:
            default:
               break;
            case 8:
               int var23 = var9++;
               var1[var2 + var23] += this.valuelist[var13 + var10++];
            case 7:
               int var25 = var9++;
               var1[var2 + var25] += this.valuelist[var13 + var10++];
            case 6:
               int var27 = var9++;
               var1[var2 + var27] += this.valuelist[var13 + var10++];
            case 5:
               int var29 = var9++;
               var1[var2 + var29] += this.valuelist[var13 + var10++];
            case 4:
               int var31 = var9++;
               var1[var2 + var31] += this.valuelist[var13 + var10++];
            case 3:
               int var33 = var9++;
               var1[var2 + var33] += this.valuelist[var13 + var10++];
            case 2:
               int var35 = var9++;
               var1[var2 + var35] += this.valuelist[var13 + var10++];
            case 1:
               int var37 = var9++;
               var1[var2 + var37] += this.valuelist[var13 + var10++];
            }
         }
      }

      return 0;
   }

   int decodev_set(float[] var1, int var2, Buffer var3, int var4) {
      int var5 = 0;

      while(var5 < var4) {
         int var7 = this.decode(var3);
         if (var7 == -1) {
            return -1;
         }

         int var8 = var7 * this.dim;

         for(int var6 = 0; var6 < this.dim; var1[var2 + var5++] = this.valuelist[var8 + var6++]) {
            ;
         }
      }

      return 0;
   }

   int decodevv_add(float[][] var1, int var2, int var3, Buffer var4, int var5) {
      int var9 = 0;
      int var6 = var2 / var3;

      while(var6 < (var2 + var5) / var3) {
         int var8 = this.decode(var4);
         if (var8 == -1) {
            return -1;
         }

         int var10 = var8 * this.dim;

         for(int var7 = 0; var7 < this.dim; ++var7) {
            int var10001 = var9++;
            var1[var10001][var6] += this.valuelist[var10 + var7];
            if (var9 == var3) {
               var9 = 0;
               ++var6;
            }
         }
      }

      return 0;
   }

   int decode(Buffer var1) {
      int var2 = 0;
      CodeBook$DecodeAux var3 = this.decode_tree;
      int var4 = var1.look(var3.tabn);
      if (var4 >= 0) {
         var2 = var3.tab[var4];
         var1.adv(var3.tabl[var4]);
         if (var2 <= 0) {
            return -var2;
         }
      }

      while(true) {
         switch(var1.read1()) {
         case -1:
         default:
            return -1;
         case 0:
            var2 = var3.ptr0[var2];
            break;
         case 1:
            var2 = var3.ptr1[var2];
         }

         if (var2 <= 0) {
            break;
         }
      }

      return -var2;
   }

   int decodevs(float[] var1, int var2, Buffer var3, int var4, int var5) {
      int var6 = this.decode(var3);
      if (var6 == -1) {
         return -1;
      } else {
         switch(var5) {
         case -1:
            int var10 = 0;

            for(int var12 = 0; var10 < this.dim; var12 += var4) {
               var1[var2 + var12] = this.valuelist[var6 * this.dim + var10];
               ++var10;
            }

            return var6;
         case 0:
            int var9 = 0;

            for(int var11 = 0; var9 < this.dim; var11 += var4) {
               var1[var2 + var11] += this.valuelist[var6 * this.dim + var9];
               ++var9;
            }

            return var6;
         case 1:
            int var7 = 0;

            for(int var8 = 0; var7 < this.dim; var8 += var4) {
               var1[var2 + var8] *= this.valuelist[var6 * this.dim + var7];
               ++var7;
            }
         }

         return var6;
      }
   }

   int best(float[] var1, int var2) {
      int var3 = -1;
      float var4 = 0.0F;
      int var5 = 0;

      for(int var6 = 0; var6 < this.entries; ++var6) {
         if (this.c.lengthlist[var6] > 0) {
            float var7 = dist(this.dim, this.valuelist, var5, var1, var2);
            if (var3 == -1 || var7 < var4) {
               var4 = var7;
               var3 = var6;
            }
         }

         var5 += this.dim;
      }

      return var3;
   }

   int besterror(float[] var1, int var2, int var3) {
      int var4 = this.best(var1, var2);
      switch(var3) {
      case 0:
         int var8 = 0;

         for(int var9 = 0; var8 < this.dim; var9 += var2) {
            var1[var9] -= this.valuelist[var4 * this.dim + var8];
            ++var8;
         }

         return var4;
      case 1:
         int var5 = 0;

         for(int var6 = 0; var5 < this.dim; var6 += var2) {
            float var7 = this.valuelist[var4 * this.dim + var5];
            if (var7 == 0.0F) {
               var1[var6] = 0.0F;
            } else {
               var1[var6] /= var7;
            }

            ++var5;
         }
      }

      return var4;
   }

   void clear() {
   }

   private static float dist(int var0, float[] var1, int var2, float[] var3, int var4) {
      float var5 = 0.0F;

      for(int var6 = 0; var6 < var0; ++var6) {
         float var7 = var1[var2 + var6] - var3[var6 * var4];
         var5 += var7 * var7;
      }

      return var5;
   }

   int init_decode(StaticCodeBook var1) {
      this.c = var1;
      this.entries = var1.entries;
      this.dim = var1.dim;
      this.valuelist = var1.unquantize();
      this.decode_tree = this.make_decode_tree();
      if (this.decode_tree == null) {
         this.clear();
         return -1;
      } else {
         return 0;
      }
   }

   static int[] make_words(int[] var0, int var1) {
      int[] var2 = new int[33];
      int[] var3 = new int[var1];

      for(int var4 = 0; var4 < var1; ++var4) {
         int var5 = var0[var4];
         if (var5 > 0) {
            int var6 = var2[var5];
            if (var5 < 32 && var6 >>> var5 != 0) {
               return null;
            }

            var3[var4] = var6;

            for(int var7 = var5; var7 > 0; --var7) {
               if ((var2[var7] & 1) != 0) {
                  if (var7 == 1) {
                     ++var2[1];
                  } else {
                     var2[var7] = var2[var7 - 1] << 1;
                  }
                  break;
               }

               ++var2[var7];
            }

            for(int var12 = var5 + 1; var12 < 33 && var2[var12] >>> 1 == var6; ++var12) {
               var6 = var2[var12];
               var2[var12] = var2[var12 - 1] << 1;
            }
         }
      }

      for(int var8 = 0; var8 < var1; ++var8) {
         int var9 = 0;

         for(int var11 = 0; var11 < var0[var8]; ++var11) {
            var9 = var9 << 1;
            var9 = var9 | var3[var8] >>> var11 & 1;
         }

         var3[var8] = var9;
      }

      return var3;
   }

   CodeBook$DecodeAux make_decode_tree() {
      int var1 = 0;
      CodeBook$DecodeAux var2 = new CodeBook$DecodeAux(this);
      int[] var3 = var2.ptr0 = new int[this.entries * 2];
      int[] var4 = var2.ptr1 = new int[this.entries * 2];
      int[] var5 = make_words(this.c.lengthlist, this.c.entries);
      if (var5 == null) {
         return null;
      } else {
         var2.aux = this.entries * 2;

         for(int var6 = 0; var6 < this.entries; ++var6) {
            if (this.c.lengthlist[var6] > 0) {
               int var7 = 0;

               int var8;
               for(var8 = 0; var8 < this.c.lengthlist[var6] - 1; ++var8) {
                  int var9 = var5[var6] >>> var8 & 1;
                  if (var9 == 0) {
                     if (var3[var7] == 0) {
                        ++var1;
                        var3[var7] = var1;
                     }

                     var7 = var3[var7];
                  } else {
                     if (var4[var7] == 0) {
                        ++var1;
                        var4[var7] = var1;
                     }

                     var7 = var4[var7];
                  }
               }

               if ((var5[var6] >>> var8 & 1) == 0) {
                  var3[var7] = -var6;
               } else {
                  var4[var7] = -var6;
               }
            }
         }

         var2.tabn = Util.ilog(this.entries) - 4;
         if (var2.tabn < 5) {
            var2.tabn = 5;
         }

         int var10 = 1 << var2.tabn;
         var2.tab = new int[var10];
         var2.tabl = new int[var10];

         for(int var11 = 0; var11 < var10; ++var11) {
            int var12 = 0;
            int var13 = 0;

            for(var13 = 0; var13 < var2.tabn && (var12 > 0 || var13 == 0); ++var13) {
               if ((var11 & 1 << var13) != 0) {
                  var12 = var4[var12];
               } else {
                  var12 = var3[var12];
               }
            }

            var2.tab[var11] = var12;
            var2.tabl[var11] = var13;
         }

         return var2;
      }
   }
}
