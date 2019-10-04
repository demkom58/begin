package net.minecraft.block;

import net.minecraft.material.Material;

import java.util.Random;

public class BlockGlass extends BlockBreakable {
    public BlockGlass(int var1, int var2, Material var3, boolean var4) {
        super(var1, var2, var3, var4);
    }

    @Override
    public int quantityDropped(Random var1) {
        return 0;
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }
}
