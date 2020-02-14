package net.potion.item;

public class ItemLog extends ItemBlock {
    public ItemLog(int var1) {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int var1) {
        return var1;
    }
}
