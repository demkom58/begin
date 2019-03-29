package net.minecraft.block;

import net.minecraft.material.Material;
import net.minecraft.world.World;

public class BlockSponge extends Block {
    protected BlockSponge(int var1) {
        super(var1, Material.SPONGE);
        this.blockIndexInTexture = 48;
    }

    public void onBlockAdded(World var1, int var2, int var3, int var4) {
        byte var5 = 2;

        for (int var6 = var2 - var5; var6 <= var2 + var5; ++var6) {
            for (int var7 = var3 - var5; var7 <= var3 + var5; ++var7) {
                for (int var8 = var4 - var5; var8 <= var4 + var5; ++var8) {
                    if (var1.getBlockMaterial(var6, var7, var8) == Material.WATER) {
                    }
                }
            }
        }

    }

    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        byte var5 = 2;

        for (int var6 = var2 - var5; var6 <= var2 + var5; ++var6) {
            for (int var7 = var3 - var5; var7 <= var3 + var5; ++var7) {
                for (int var8 = var4 - var5; var8 <= var4 + var5; ++var8) {
                    var1.notifyBlocksOfNeighborChange(var6, var7, var8, var1.getBlockId(var6, var7, var8));
                }
            }
        }

    }
}
