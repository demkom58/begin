package net.minecraft.block;

import java.util.Random;

public class BlockObsidian extends BlockStone {
    public BlockObsidian(int var1, int var2) {
        super(var1, var2);
    }

    @Override
    public int quantityDropped(Random var1) {
        return 1;
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return Block.OBSIDIAN.blockID;
    }
}
