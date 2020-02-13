package net.minecraft.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.material.Material;
import net.minecraft.world.World;

import java.util.Random;

public class BlockLog extends Block {
    protected BlockLog(int var1) {
        super(var1, Material.WOOD);
        this.blockIndexInTexture = 20;
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    public int idDropped(int var1, Random random) {
        return Block.WOOD.blockID;
    }

    @Override
    public void harvestBlock(World world, EntityPlayer entityPlayer, int var3, int var4, int var5, int var6) {
        super.harvestBlock(world, entityPlayer, var3, var4, var5, var6);
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        byte var5 = 4;
        int var6 = var5 + 1;
        if (world.checkChunksExist(x - var6, y - var6, z - var6, x + var6, y + var6, z + var6)) {
            for (int var7 = -var5; var7 <= var5; ++var7) {
                for (int var8 = -var5; var8 <= var5; ++var8) {
                    for (int var9 = -var5; var9 <= var5; ++var9) {
                        int var10 = world.getBlockId(x + var7, y + var8, z + var9);
                        if (var10 == Block.LEAVES.blockID) {
                            int var11 = world.getBlockMetadata(x + var7, y + var8, z + var9);
                            if ((var11 & 8) == 0) {
                                world.setBlockMetadata(x + var7, y + var8, z + var9, var11 | 8);
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        if (var1 == 1) {
            return 21;
        } else if (var1 == 0) {
            return 21;
        } else if (var2 == 1) {
            return 116;
        } else {
            return var2 == 2 ? 117 : 20;
        }
    }

    @Override
    protected int damageDropped(int var1) {
        return var1;
    }
}
