package net.minecraft;

import java.util.Random;

public class BlockClay extends Block {
    public BlockClay(int var1, int var2) {
        super(var1, var2, Material.CLAY);
    }

    public int idDropped(int var1, Random random) {
        return Item.CLAY.shiftedIndex;
    }

    public int quantityDropped(Random random) {
        return 4;
    }
}
