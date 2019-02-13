package net.minecraft;

import java.util.Random;

public class BlockGlowStone extends Block {
    public BlockGlowStone(int var1, int var2, Material var3) {
        super(var1, var2, var3);
    }

    public int quantityDropped(Random random) {
        return 2 + random.nextInt(3);
    }

    public int idDropped(int var1, Random random) {
        return Item.LIGHT_STONE_DUST.shiftedIndex;
    }
}
