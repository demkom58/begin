package net.minecraft.block;

import net.minecraft.material.Material;

import java.util.Random;

public class BlockBookshelf extends Block {
    public BlockBookshelf(int var1, int var2) {
        super(var1, var2, Material.WOOD);
    }

    public int getBlockTextureFromSide(int side) {
        return side <= 1 ? 4 : this.blockIndexInTexture;
    }

    public int quantityDropped(Random var1) {
        return 0;
    }
}
