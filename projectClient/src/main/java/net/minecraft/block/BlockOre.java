package net.minecraft.block;

import net.minecraft.item.Item;
import net.minecraft.material.Material;

import java.util.Random;

public class BlockOre extends Block {
    public BlockOre(int var1, int var2) {
        super(var1, var2, Material.ROCK);
    }

    @Override
    public int idDropped(int var1, Random var2) {
        if (this.blockID == Block.ORE_COAL.blockID) {
            return Item.COAL.shiftedIndex;
        } else if (this.blockID == Block.ORE_DIAMOND.blockID) {
            return Item.DIAMOND.shiftedIndex;
        } else {
            return this.blockID == Block.ORE_LAPIS.blockID ? Item.DYE_POWDER.shiftedIndex : this.blockID;
        }
    }

    @Override
    public int quantityDropped(Random var1) {
        return this.blockID == Block.ORE_LAPIS.blockID ? 4 + var1.nextInt(5) : 1;
    }

    @Override
    protected int damageDropped(int var1) {
        return this.blockID == Block.ORE_LAPIS.blockID ? 4 : 0;
    }
}
