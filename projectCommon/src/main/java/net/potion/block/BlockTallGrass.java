package net.potion.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.client.render.RenderColorizerGrass;
import net.potion.item.Item;
import net.potion.world.IBlockAccess;

import java.util.Random;

public class BlockTallGrass extends BlockFlower {
    protected BlockTallGrass(int var1, int var2) {
        super(var1, var2);
        float var3 = 0.4F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.8F, 0.5F + var3);
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        if (metadata == 1) {
            return this.blockIndexInTexture;
        } else if (metadata == 2) {
            return this.blockIndexInTexture + 16 + 1;
        } else {
            return metadata == 0 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture;
        }
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
        int var5 = blockAccess.getBlockMetadata(x, y, z);
        if (var5 == 0) {
            return 16777215;
        } else {
            long var6 = x * 3129871 + z * 6129781 + y;
            var6 = var6 * var6 * 42317861L + var6 * 11L;
            x = (int) ((long) x + (var6 >> 14 & 31L));
            y = (int) ((long) y + (var6 >> 19 & 31L));
            z = (int) ((long) z + (var6 >> 24 & 31L));
            blockAccess.getWorldChunkManager().getBiomeGensAt(x, z, 1, 1);
            double var8 = blockAccess.getWorldChunkManager().temperature[0];
            double var10 = blockAccess.getWorldChunkManager().humidity[0];
            return RenderColorizerGrass.getGrassColor(var8, var10);
        }
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return var2.nextInt(8) == 0 ? Item.SEEDS.shiftedIndex : -1;
    }
}
