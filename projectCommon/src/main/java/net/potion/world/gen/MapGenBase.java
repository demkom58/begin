package net.potion.world.gen;

import net.potion.world.World;
import net.potion.world.chunk.IOldChunkProvider;

import java.util.Random;

public class MapGenBase {
    protected int field1 = 8;
    protected Random rand = new Random();

    public void func_867_a(IOldChunkProvider var1, World var2, int var3, int var4, byte[] var5) {
        int var6 = this.field1;
        this.rand.setSeed(var2.getRandomSeed());
        long var7 = this.rand.nextLong() / 2L * 2L + 1L;
        long var9 = this.rand.nextLong() / 2L * 2L + 1L;

        for (int var11 = var3 - var6; var11 <= var3 + var6; ++var11) {
            for (int var12 = var4 - var6; var12 <= var4 + var6; ++var12) {
                this.rand.setSeed((long) var11 * var7 + (long) var12 * var9 ^ var2.getRandomSeed());
                this.method1(var2, var11, var12, var3, var4, var5);
            }
        }

    }

    protected void method1(World var1, int var2, int var3, int var4, int var5, byte[] var6) {
    }
}
