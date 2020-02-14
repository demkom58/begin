package net.potion.item;

public class ItemPiston extends ItemBlock {
    public ItemPiston(int var1) {
        super(var1);
    }

    @Override
    public int getPlacedBlockMetadata(int var1) {
        return 7;
    }
}
