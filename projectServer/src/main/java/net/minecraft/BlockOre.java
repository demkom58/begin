package net.minecraft;

import java.util.Random;

public class BlockOre extends Block {
    public BlockOre(int var1, int var2) {
        super(var1, var2, Material.rock);
    }

    public int idDropped(int var1, Random random) {
        if (this.blockID == Block.oreCoal.blockID) {
            return Item.COAL.shiftedIndex;
        } else if (this.blockID == Block.oreDiamond.blockID) {
            return Item.DIAMOND.shiftedIndex;
        } else {
            return this.blockID == Block.oreLapis.blockID ? Item.DYE_POWDER.shiftedIndex : this.blockID;
        }
    }

    public int quantityDropped(Random random) {
        return this.blockID == Block.oreLapis.blockID ? 4 + random.nextInt(5) : 1;
    }

    protected int damageDropped(int var1) {
        return this.blockID == Block.oreLapis.blockID ? 4 : 0;
    }
}
