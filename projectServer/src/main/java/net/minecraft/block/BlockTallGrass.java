package net.minecraft.block;

import net.minecraft.item.Item;

import java.util.Random;

public class BlockTallGrass extends BlockFlower {
    protected BlockTallGrass(int var1, int var2) {
        super(var1, var2);
        float var3 = 0.4F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.8F, 0.5F + var3);
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        if (var2 == 1) {
            return this.blockIndexInTexture;
        } else if (var2 == 2) {
            return this.blockIndexInTexture + 16 + 1;
        } else {
            return var2 == 0 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture;
        }
    }

    @Override
    public int idDropped(int var1, Random random) {
        return random.nextInt(8) == 0 ? Item.SEEDS.shiftedIndex : -1;
    }
}
