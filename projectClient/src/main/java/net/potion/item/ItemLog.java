package net.potion.item;

import net.potion.block.Block;

public class ItemLog extends ItemBlock {
    public ItemLog(int var1) {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getIconFromDamage(int var1) {
        return Block.WOOD.getBlockTextureFromSideAndMetadata(2, var1);
    }

    @Override
    public int getPlacedBlockMetadata(int var1) {
        return var1;
    }
}
