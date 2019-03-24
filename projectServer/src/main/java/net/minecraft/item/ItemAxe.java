package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemAxe extends ItemTool {
    private static Block[] blocksEffectiveAgainst = new Block[]{Block.PLANKS, Block.BOOKSHELF, Block.WOOD, Block.CHEST};

    protected ItemAxe(int var1, EnumToolMaterial var2) {
        super(var1, 3, var2, blocksEffectiveAgainst);
    }
}
