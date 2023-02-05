package net.minecraft.block;

import net.minecraft.material.Material;
import net.minecraft.world.World;

public class BlockSponge extends Block {
    protected BlockSponge(int var1) {
        super(var1, Material.SPONGE);
        this.blockIndexInTexture = 48;
    }

    @Override
    public void onBlockAdded(World world, int var2, int var3, int var4) {
        byte var5 = 2;

        for (int iX = var2 - var5; iX <= var2 + var5; ++iX) {
            for (int iY = var3 - var5; iY <= var3 + var5; ++iY) {
                for (int iZ = var4 - var5; iZ <= var4 + var5; ++iZ) {
                    if (world.getBlockMaterial(iX, iY, iZ) == Material.WATER) {
                        world.setBlock(iX, iY, iZ, 0);
                    }
                }
            }
        }

    }

    @Override
    public void onBlockRemoval(World world, int var2, int var3, int var4) {
        byte var5 = 2;

        for (int iX = var2 - var5; iX <= var2 + var5; ++iX) {
            for (int iY = var3 - var5; iY <= var3 + var5; ++iY) {
                for (int iZ = var4 - var5; iZ <= var4 + var5; ++iZ) {
                    world.notifyBlocksOfNeighborChange(iX, iY, iZ, world.getBlockId(iX, iY, iZ));
                }
            }
        }

    }
}
