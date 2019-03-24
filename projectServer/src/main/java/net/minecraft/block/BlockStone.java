package net.minecraft.block;

import net.minecraft.material.Material;

import java.util.Random;

public class BlockStone extends Block {
    public BlockStone(int var1, int var2) {
        super(var1, var2, Material.ROCK);
    }

    public int idDropped(int var1, Random random) {
        return Block.COBBLESTONE.blockID;
    }
}
