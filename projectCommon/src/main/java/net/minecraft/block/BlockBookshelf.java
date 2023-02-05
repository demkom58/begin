package net.minecraft.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.material.Material;

import java.util.Random;

public class BlockBookshelf extends Block {
    public BlockBookshelf(int var1, int var2) {
        super(var1, var2, Material.WOOD);
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getBlockTextureFromSide(int side) {
        return side <= 1 ? 4 : this.blockIndexInTexture;
    }

    @Override
    public int quantityDropped(Random var1) {
        return 0;
    }
}
