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

    public int idDropped(int var1, Random random) {
        return Item.SNOWBALL.shiftedIndex;
    }

    public int quantityDropped(Random random) {
        return 4;
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (world.getSavedLightValue(EnumSkyBlock.BLOCK, x, y, z) > 11) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
            world.setBlockWithNotify(x, y, z, 0);
        }

    }
}
