package net.minecraft.block;

import net.minecraft.material.Material;

import java.util.Random;

public class BlockBookshelf extends Block {
    public BlockBookshelf(int var1, int var2) {
        super(var1, var2, Material.WOOD);
    }

    @Override
    public int getBlockTextureFromSide(int var1) {
        return var1 <= 1 ? 4 : this.blockIndexInTexture;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
}
