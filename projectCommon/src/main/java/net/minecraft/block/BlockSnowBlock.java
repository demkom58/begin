package net.minecraft.block;

import net.minecraft.item.Item;
import net.minecraft.material.Material;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSnowBlock extends Block {
    protected BlockSnowBlock(int var1, int var2) {
        super(var1, var2, Material.BUILT_SNOW);
        this.setTickOnLoad(true);
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return Item.SNOWBALL.shiftedIndex;
    }

    @Override
    public int quantityDropped(Random var1) {
        return 4;
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        if (var1.getSavedLightValue(EnumSkyBlock.BLOCK, var2, var3, var4) > 11) {
            this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
            var1.setBlockWithNotify(var2, var3, var4, 0);
        }

    }
}
