package net.minecraft.item;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStep;

public class ItemSlab extends ItemBlock {
    public ItemSlab(int var1) {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getIconFromDamage(int var1) {
        return Block.STAIR_SINGLE.getBlockTextureFromSideAndMetadata(2, var1);
    }

    @Override
    public int getPlacedBlockMetadata(int var1) {
        return var1;
    }

    @Override
    public String getItemName(ItemStack var1) {
        return super.getItemName() + "." + BlockStep.BLOCK_MATERIALS[var1.getItemDamage()];
    }
}
