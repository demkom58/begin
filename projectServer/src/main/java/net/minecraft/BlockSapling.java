package net.minecraft;

import java.util.Random;

public class BlockSapling extends BlockFlower {
    protected BlockSapling(int var1, int var2) {
        super(var1, var2);
        float var3 = 0.4F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 2.0F, 0.5F + var3);
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.singleplayerWorld) {
            super.updateTick(world, x, y, z, random);
            if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(30) == 0) {
                int var6 = world.getBlockMetadata(x, y, z);
                if ((var6 & 8) == 0) {
                    world.setBlockMetadataWithNotify(x, y, z, var6 | 8);
                } else {
                    this.growTree(world, x, y, z, random);
                }
            }

        }
    }

    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        var2 = var2 & 3;
        if (var2 == 1) {
            return 63;
        } else {
            return var2 == 2 ? 79 : super.getBlockTextureFromSideAndMetadata(var1, var2);
        }
    }

    public void growTree(World var1, int var2, int var3, int var4, Random var5) {
        int var6 = var1.getBlockMetadata(var2, var3, var4) & 3;
        var1.setBlock(var2, var3, var4, 0);
        Object var7 = null;
        if (var6 == 1) {
            var7 = new WorldGenTaiga2();
        } else if (var6 == 2) {
            var7 = new WorldGenForest();
        } else {
            var7 = new WorldGenTrees();
            if (var5.nextInt(10) == 0) {
                var7 = new WorldGenBigTree();
            }
        }

        if (!((WorldGenerator) var7).generate(var1, var5, var2, var3, var4)) {
            var1.setBlockAndMetadata(var2, var3, var4, this.blockID, var6);
        }

    }

    protected int damageDropped(int var1) {
        return var1 & 3;
    }
}
