package net.potion.block;

import net.potion.material.Material;
import net.potion.world.World;

import java.util.Random;

public class BlockLockedChest extends Block {
    protected BlockLockedChest(int var1) {
        super(var1, Material.WOOD);
        this.blockIndexInTexture = 26;
    }

    @Override
    public int getBlockTextureFromSide(int var1) {
        if (var1 == 1) {
            return this.blockIndexInTexture - 1;
        } else if (var1 == 0) {
            return this.blockIndexInTexture - 1;
        } else {
            return var1 == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        return true;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        world.setBlockWithNotify(x, y, z, 0);
    }
}
