package net.potion.item;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.block.Block;
import net.potion.block.BlockCloth;

public class ItemCloth extends ItemBlock {
    public ItemCloth(int var1) {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Side(CodeSide.CLIENT)
    @Override
    public int getIconFromDamage(int var1) {
        return Block.CLOTH.getBlockTextureFromSideAndMetadata(2, BlockCloth.method1(var1));
    }

    @Override
    public int getPlacedBlockMetadata(int var1) {
        return var1;
    }

    @Override
    public String getItemName(ItemStack var1) {
        return super.getItemName() + "." + ItemDye.dyeColors[BlockCloth.method1(var1.getItemDamage())];
    }
}
