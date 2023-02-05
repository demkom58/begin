package net.minecraft.item;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderColorizerFoliage;

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
    @Side(CodeSide.CLIENT)
    public int getIconFromDamage(int var1) {
        return Block.LEAVES.getBlockTextureFromSideAndMetadata(0, var1);
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getColorFromDamage(int var1) {
        if ((var1 & 1) == 1) {
            return RenderColorizerFoliage.getFoliageColorPine();
        } else {
            return (var1 & 2) == 2 ? RenderColorizerFoliage.getFoliageColorBirch() : RenderColorizerFoliage.func_31073_c();
        }
    }
}
