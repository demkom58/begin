package net.potion.block;

import net.potion.material.Material;

public class BlockOreStorage extends Block {
    public BlockOreStorage(int var1, int var2) {
        super(var1, Material.IRON);
        this.blockIndexInTexture = var2;
    }

    @Override
    public int getBlockTextureFromSide(int var1) {
        return this.blockIndexInTexture;
    }
}
