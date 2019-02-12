package com.jcraft.jorbis;

class Residue2 extends Residue0 {
   int inverse(Block var1, Object var2, float[][] var3, int[] var4, int var5) {
      int var6 = 0;

      for(var6 = 0; var6 < var5 && var4[var6] == 0; ++var6) {
         ;
      }

      return var6 == var5 ? 0 : _2inverse(var1, var2, var3, var5);
   }
}
