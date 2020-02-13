package net.minecraft.block;

import net.minecraft.material.Material;

public class BlockSandStone extends Block {
    public BlockSandStone(int var1) {
        super(var1, 192, Material.ROCK);
    }

    @Override
    public int getBlockTextureFromSide(int var1) {
        if (var1 == 1) {
            return this.blockIndexInTexture - 16;
        } else {
            return var1 == 0 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture;
        }
    }
}
