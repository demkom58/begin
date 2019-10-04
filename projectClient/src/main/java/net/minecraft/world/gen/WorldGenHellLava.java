package net.minecraft.world.gen;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenHellLava extends WorldGenerator {
    private int field_4158_a;

    public WorldGenHellLava(int var1) {
        this.field_4158_a = var1;
    }

    @Override
    public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
        if (var1.getBlockId(var3, var4 + 1, var5) != Block.BLOOD_STONE.blockID) {
            return false;
        } else if (var1.getBlockId(var3, var4, var5) != 0 && var1.getBlockId(var3, var4, var5) != Block.BLOOD_STONE.blockID) {
            return false;
        } else {
            int var6 = 0;
            if (var1.getBlockId(var3 - 1, var4, var5) == Block.BLOOD_STONE.blockID) {
                ++var6;
            }

            if (var1.getBlockId(var3 + 1, var4, var5) == Block.BLOOD_STONE.blockID) {
                ++var6;
            }

            if (var1.getBlockId(var3, var4, var5 - 1) == Block.BLOOD_STONE.blockID) {
                ++var6;
            }

            if (var1.getBlockId(var3, var4, var5 + 1) == Block.BLOOD_STONE.blockID) {
                ++var6;
            }

            if (var1.getBlockId(var3, var4 - 1, var5) == Block.BLOOD_STONE.blockID) {
                ++var6;
            }

            int var7 = 0;
            if (var1.isAirBlock(var3 - 1, var4, var5)) {
                ++var7;
            }

            if (var1.isAirBlock(var3 + 1, var4, var5)) {
                ++var7;
            }

            if (var1.isAirBlock(var3, var4, var5 - 1)) {
                ++var7;
            }

            if (var1.isAirBlock(var3, var4, var5 + 1)) {
                ++var7;
            }

            if (var1.isAirBlock(var3, var4 - 1, var5)) {
                ++var7;
            }

            if (var6 == 4 && var7 == 1) {
                var1.setBlockWithNotify(var3, var4, var5, this.field_4158_a);
                var1.scheduledUpdatesAreImmediate = true;
                Block.BLOCKS_LIST[this.field_4158_a].updateTick(var1, var3, var4, var5, var2);
                var1.scheduledUpdatesAreImmediate = false;
            }

            return true;
        }
    }
}
