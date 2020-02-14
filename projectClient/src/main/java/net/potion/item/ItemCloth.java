package net.potion.item;

import net.potion.block.Block;
import net.potion.block.BlockCloth;

public class ItemCloth extends ItemBlock {
    public ItemCloth(int var1) {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getIconFromDamage(int var1) {
        return Block.CLOTH.getBlockTextureFromSideAndMetadata(2, BlockCloth.func_21034_c(var1));
    }

    @Override
    public int getPlacedBlockMetadata(int var1) {
        return var1;
    }

    @Override
    public String getItemNameIS(ItemStack var1) {
        return super.getItemName() + "." + ItemDye.dyeColors[BlockCloth.func_21034_c(var1.getItemDamage())];
    }
}
