package net.minecraft.block;

import net.minecraft.item.Item;

import java.util.Random;

public class BlockGravel extends BlockSand {
    public BlockGravel(int var1, int var2) {
        super(var1, var2);
    }

    public int idDropped(int var1, Random random) {
        return random.nextInt(10) == 0 ? Item.FLINT.shiftedIndex : this.blockID;
    }
}
