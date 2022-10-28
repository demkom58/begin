package net.potion.block;

import net.potion.material.Material;
import net.potion.tileentity.TileEntity;
import net.potion.tileentity.TileEntityMobSpawner;

import java.util.Random;

public class BlockMobSpawner extends BlockContainer {
    protected BlockMobSpawner(int var1, int var2) {
        super(var1, var2, Material.ROCK);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityMobSpawner();
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return 0;
    }

    @Override
    public int quantityDropped(Random var1) {
        return 0;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
