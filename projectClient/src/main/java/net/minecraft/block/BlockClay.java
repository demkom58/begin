package net.minecraft.block;

import net.minecraft.item.Item;
import net.minecraft.material.Material;

import java.util.Random;

public class BlockClay extends Block {
    public BlockClay(int var1, int var2) {
        super(var1, var2, Material.CLAY);
    }

    public int idDropped(int var1, Random var2) {
        return Item.CLAY.shiftedIndex;
    }

    public int quantityDropped(Random var1) {
        return 4;
    }
}
