package net.potion.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.material.Material;

public class BlockOreStorage extends Block {
    public BlockOreStorage(int var1, int var2) {
        super(var1, Material.IRON);
        this.blockIndexInTexture = var2;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getBlockTextureFromSide(int side) {
        return this.blockIndexInTexture;
    }
}
