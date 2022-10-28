package net.potion.item;

import net.potion.block.Block;

public class ItemSpade extends ItemTool {
    private static final Block[] BLOCKS_EFFECTIVE_AGAINST = new Block[]{Block.GRASS, Block.DIRT, Block.SAND, Block.GRAVEL, Block.SNOW, Block.BLOCK_SNOW, Block.BLOCK_CLAY, Block.FARMLAND};

    public ItemSpade(int var1, MaterialGrade var2) {
        super(var1, 1, var2, BLOCKS_EFFECTIVE_AGAINST);
    }

    @Override
    public boolean canHarvestBlock(Block var1) {
        if (var1 == Block.SNOW) {
            return true;
        }

        return var1 == Block.BLOCK_SNOW;
    }
}
