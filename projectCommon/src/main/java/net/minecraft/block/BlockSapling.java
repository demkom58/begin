package net.minecraft.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.world.World;
import net.minecraft.world.gen.struct.*;

import java.util.Random;

public class BlockSapling extends BlockFlower {
    protected BlockSapling(int var1, int var2) {
        super(var1, var2);
        float var3 = 0.4F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 2.0F, 0.5F + var3);
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        if (!var1.localWorld) {
            super.updateTick(var1, var2, var3, var4, var5);
            if (var1.getBlockLightValue(var2, var3 + 1, var4) >= 9 && var5.nextInt(30) == 0) {
                int var6 = var1.getBlockMetadata(var2, var3, var4);
                if ((var6 & 8) == 0) {
                    var1.setBlockMetadataWithNotify(var2, var3, var4, var6 | 8);
                } else {
                    this.growTree(var1, var2, var3, var4, var5);
                }
            }

        }
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        metadata = metadata & 3;
        if (metadata == 1) {
            return 63;
        } else {
            return metadata == 2 ? 79 : super.getBlockTextureFromSideAndMetadata(side, metadata);
        }
    }

    public void growTree(World var1, int var2, int var3, int var4, Random var5) {
        int var6 = var1.getBlockMetadata(var2, var3, var4) & 3;
        var1.setBlock(var2, var3, var4, 0);
        WorldGenerator var7;
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

        if (!var7.generate(var1, var5, var2, var3, var4)) {
            var1.setBlockAndMetadata(var2, var3, var4, this.blockID, var6);
        }

    }

    @Override
    protected int damageDropped(int var1) {
        return var1 & 3;
    }
}
