package net.minecraft;

import java.util.Random;

public class BlockObsidian extends BlockStone {
    public BlockObsidian(int var1, int var2) {
        super(var1, var2);
    }

    public int quantityDropped(Random random) {
        return 1;
    }

    public int idDropped(int var1, Random random) {
        return Block.OBSIDIAN.blockID;
    }
}
