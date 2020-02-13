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
    public int quantityDropped(Random var1) {
        return 1;
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return Block.WOOD.blockID;
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int blockId) {
        super.harvestBlock(world, player, x, y, z, blockId);
    }

    @Override
    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        byte var5 = 4;
        int var6 = var5 + 1;
        if (var1.checkChunksExist(var2 - var6, var3 - var6, var4 - var6, var2 + var6, var3 + var6, var4 + var6)) {
            for (int var7 = -var5; var7 <= var5; ++var7) {
                for (int var8 = -var5; var8 <= var5; ++var8) {
                    for (int var9 = -var5; var9 <= var5; ++var9) {
                        int var10 = var1.getBlockId(var2 + var7, var3 + var8, var4 + var9);
                        if (var10 == Block.LEAVES.blockID) {
                            int var11 = var1.getBlockMetadata(var2 + var7, var3 + var8, var4 + var9);
                            if ((var11 & 8) == 0) {
                                var1.setBlockMetadata(var2 + var7, var3 + var8, var4 + var9, var11 | 8);
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        if (side == 1) {
            return 21;
        } else if (side == 0) {
            return 21;
        } else if (metadata == 1) {
            return 116;
        } else {
            return metadata == 2 ? 117 : 20;
        }
    }

    @Override
    protected int damageDropped(int var1) {
        return var1;
    }
}
