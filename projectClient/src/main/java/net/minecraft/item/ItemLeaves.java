package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.client.render.ColorizerFoliage;

public class ItemLeaves extends ItemBlock {
    public ItemLeaves(int var1) {
        super(var1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getPlacedBlockMetadata(int var1) {
        return var1 | 8;
    }

    @Override
    public int getIconFromDamage(int var1) {
        return Block.LEAVES.getBlockTextureFromSideAndMetadata(0, var1);
    }

    @Override
    public int getColorFromDamage(int var1) {
        if ((var1 & 1) == 1) {
            return ColorizerFoliage.getFoliageColorPine();
        } else {
            return (var1 & 2) == 2 ? ColorizerFoliage.getFoliageColorBirch() : ColorizerFoliage.func_31073_c();
        }
    }
}
