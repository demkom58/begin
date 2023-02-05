package net.minecraft.item;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.block.Block;

public class ItemSapling extends ItemBlock {
    public ItemSapling(int var1) {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getPlacedBlockMetadata(int var1) {
        return var1;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getIconFromDamage(int var1) {
        return Block.SAPLING.getBlockTextureFromSideAndMetadata(0, var1);
    }
}
