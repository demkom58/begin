package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Mapping0 extends FuncMapping {
   static int seq = 0;
   float[][] pcmbundle = (float[][])null;
   int[] zerobundle = null;
   int[] nonzero = null;
   Object[] floormemo = null;

   void free_info(Object var1) {
   }

   void free_look(Object var1) {
   }

   Object look(DspState var1, InfoMode var2, Object var3) {
      Info var4 = var1.vi;
      Mapping0$LookMapping0 var5 = new Mapping0$LookMapping0(this);
      Mapping0$InfoMapping0 var6 = var5.map = (Mapping0$InfoMapping0)var3;
      var5.mode = var2;
      var5.time_look = new Object[var6.submaps];
      var5.floor_look = new Object[var6.submaps];
      var5.residue_look = new Object[var6.submaps];
      var5.time_func = new FuncTime[var6.submaps];
      var5.floor_func = new FuncFloor[var6.submaps];
      var5.residue_func = new FuncResidue[var6.submaps];

      for(int var7 = 0; var7 < var6.submaps; ++var7) {
         int var8 = var6.timesubmap[var7];
         int var9 = var6.floorsubmap[var7];
         int var10 = var6.residuesubmap[var7];
         var5.time_func[var7] = FuncTime.time_P[var4.time_type[var8]];
         var5.time_look[var7] = var5.time_func[var7].look(var1, var2, var4.time_param[var8]);
         var5.floor_func[var7] = FuncFloor.floor_P[var4.floor_type[var9]];
         var5.floor_look[var7] = var5.floor_func[var7].look(var1, var2, var4.floor_param[var9]);
         var5.residue_func[var7] = FuncResidue.residue_P[var4.residue_type[var10]];
         var5.residue_look[var7] = var5.residue_func[var7].look(var1, var2, var4.residue_param[var10]);
      }

      if (var4.psys != 0 && var1.analysisp != 0) {
         ;
      }

      var5.ch = var4.channels;
      return var5;
   }

   void pack(Info var1, Object var2, Buffer var3) {
      Mapping0$InfoMapping0 var4 = (Mapping0$InfoMapping0)var2;
      if (var4.submaps > 1) {
         var3.write(1, 1);
         var3.write(var4.submaps - 1, 4);
      } else {
         var3.write(0, 1);
      }

      if (var4.coupling_steps > 0) {
         var3.write(1, 1);
         var3.write(var4.coupling_steps - 1, 8);

         for(int var5 = 0; var5 < var4.coupling_steps; ++var5) {
            var3.write(var4.coupling_mag[var5], Util.ilog2(var1.channels));
            var3.write(var4.coupling_ang[var5], Util.ilog2(var1.channels));
         }
      } else {
         var3.write(0, 1);
      }

      var3.write(0, 2);
      if (var4.submaps > 1) {
         for(int var6 = 0; var6 < var1.channels; ++var6) {
            var3.write(var4.chmuxlist[var6], 4);
         }
      }

      for(int var7 = 0; var7 < var4.submaps; ++var7) {
         var3.write(var4.timesubmap[var7], 8);
         var3.write(var4.floorsubmap[var7], 8);
         var3.write(var4.residuesubmap[var7], 8);
      }

   }

   Object unpack(Info var1, Buffer var2) {
      Mapping0$InfoMapping0 var3 = new Mapping0$InfoMapping0(this);
      if (var2.read(1) != 0) {
         var3.submaps = var2.read(4) + 1;
      } else {
         var3.submaps = 1;
      }

      if (var2.read(1) != 0) {
         var3.coupling_steps = var2.read(8) + 1;

         for(int var4 = 0; var4 < var3.coupling_steps; ++var4) {
            int var5 = var3.coupling_mag[var4] = var2.read(Util.ilog2(var1.channels));
            int var6 = var3.coupling_ang[var4] = var2.read(Util.ilog2(var1.channels));
            if (var5 < 0 || var6 < 0 || var5 == var6 || var5 >= var1.channels || var6 >= var1.channels) {
               var3.free();
               return null;
            }
         }
      }

      if (var2.read(2) > 0) {
         var3.free();
         return null;
      } else {
         if (var3.submaps > 1) {
            for(int var7 = 0; var7 < var1.channels; ++var7) {
               var3.chmuxlist[var7] = var2.read(4);
               if (var3.chmuxlist[var7] >= var3.submaps) {
                  var3.free();
                  return null;
               }
            }
         }

         for(int var8 = 0; var8 < var3.submaps; ++var8) {
            var3.timesubmap[var8] = var2.read(8);
            if (var3.timesubmap[var8] >= var1.times) {
               var3.free();
               return null;
            }

            var3.floorsubmap[var8] = var2.read(8);
            if (var3.floorsubmap[var8] >= var1.floors) {
               var3.free();
               return null;
            }

            var3.residuesubmap[var8] = var2.read(8);
            if (var3.residuesubmap[var8] >= var1.residues) {
               var3.free();
               return null;
            }
         }

         return var3;
      }
   }

   synchronized int inverse(Block var1, Object var2) {
      DspState var3 = var1.vd;
      Info var4 = var3.vi;
      Mapping0$LookMapping0 var5 = (Mapping0$LookMapping0)var2;
      Mapping0$InfoMapping0 var6 = var5.map;
      InfoMode var7 = var5.mode;
      int var8 = var1.pcmend = var4.blocksizes[var1.W];
      float[] var9 = var3.window[var1.W][var1.lW][var1.nW][var7.windowtype];
      if (this.pcmbundle == null || this.pcmbundle.length < var4.channels) {
         this.pcmbundle = new float[var4.channels][];
         this.nonzero = new int[var4.channels];
         this.zerobundle = new int[var4.channels];
         this.floormemo = new Object[var4.channels];
      }

      for(int var10 = 0; var10 < var4.channels; ++var10) {
         float[] var11 = var1.pcm[var10];
         int var12 = var6.chmuxlist[var10];
         this.floormemo[var10] = var5.floor_func[var12].inverse1(var1, var5.floor_look[var12], this.floormemo[var10]);
         if (this.floormemo[var10] != null) {
            this.nonzero[var10] = 1;
         } else {
            this.nonzero[var10] = 0;
         }

         for(int var13 = 0; var13 < var8 / 2; ++var13) {
            var11[var13] = 0.0F;
         }
      }

      for(int var16 = 0; var16 < var6.coupling_steps; ++var16) {
         if (this.nonzero[var6.coupling_mag[var16]] != 0 || this.nonzero[var6.coupling_ang[var16]] != 0) {
            this.nonzero[var6.coupling_mag[var16]] = 1;
            this.nonzero[var6.coupling_ang[var16]] = 1;
         }
      }

      for(int var17 = 0; var17 < var6.submaps; ++var17) {
         int var22 = 0;

         for(int var27 = 0; var27 < var4.channels; ++var27) {
            if (var6.chmuxlist[var27] == var17) {
               if (this.nonzero[var27] != 0) {
                  this.zerobundle[var22] = 1;
               } else {
                  this.zerobundle[var22] = 0;
               }

               this.pcmbundle[var22++] = var1.pcm[var27];
            }
         }

         var5.residue_func[var17].inverse(var1, var5.residue_look[var17], this.pcmbundle, this.zerobundle, var22);
      }

      for(int var18 = var6.coupling_steps - 1; var18 >= 0; --var18) {
         float[] var23 = var1.pcm[var6.coupling_mag[var18]];
         float[] var28 = var1.pcm[var6.coupling_ang[var18]];

         for(int var32 = 0; var32 < var8 / 2; ++var32) {
            float var14 = var23[var32];
            float var15 = var28[var32];
            if (var14 > 0.0F) {
               if (var15 > 0.0F) {
                  var23[var32] = var14;
                  var28[var32] = var14 - var15;
               } else {
                  var28[var32] = var14;
                  var23[var32] = var14 + var15;
               }
            } else if (var15 > 0.0F) {
               var23[var32] = var14;
               var28[var32] = var14 + var15;
            } else {
               var28[var32] = var14;
               var23[var32] = var14 - var15;
            }
         }
      }

      for(int var19 = 0; var19 < var4.channels; ++var19) {
         float[] var24 = var1.pcm[var19];
         int var29 = var6.chmuxlist[var19];
         var5.floor_func[var29].inverse2(var1, var5.floor_look[var29], this.floormemo[var19], var24);
      }

      for(int var20 = 0; var20 < var4.channels; ++var20) {
         float[] var25 = var1.pcm[var20];
         ((Mdct)var3.transform[var1.W][0]).backward(var25, var25);
      }

      for(int var21 = 0; var21 < var4.channels; ++var21) {
         float[] var26 = var1.pcm[var21];
         if (this.nonzero[var21] != 0) {
            for(int var31 = 0; var31 < var8; ++var31) {
               var26[var31] *= var9[var31];
            }
         } else {
            for(int var30 = 0; var30 < var8; ++var30) {
               var26[var30] = 0.0F;
            }
         }
      }

      return 0;
   }
}
