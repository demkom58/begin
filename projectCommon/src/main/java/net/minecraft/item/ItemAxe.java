package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemAxe extends ItemTool {
    private static final Block[] BLOCKS_EFFECTIVE_AGAINST = new Block[]{
            Block.PLANKS,
            Block.BOOKSHELF,
            Block.WOOD,
            Block.CHEST,
            Block.DOOR_WOOD,
            Block.FENCE,
            Block.SIGN,
            Block.SIGN_WALL,
            Block.LADDER,
            Block.TRAPDOOR,
    };

    protected ItemAxe(int var1, MaterialGrade var2) {
        super(var1, 3, var2, BLOCKS_EFFECTIVE_AGAINST);
    }
}
