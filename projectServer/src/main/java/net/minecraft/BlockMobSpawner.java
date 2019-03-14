package net.minecraft;

import java.util.Random;

public class BlockMobSpawner extends BlockContainer {
    protected BlockMobSpawner(int var1, int var2) {
        super(var1, var2, Material.ROCK);
    }

    protected TileEntity getBlockEntity() {
        return new TileEntityMobSpawner();
    }

    public int idDropped(int var1, Random random) {
        return 0;
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public boolean isOpaqueCube() {
        return false;
    }
}
