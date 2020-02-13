package net.minecraft.block;

import net.minecraft.client.render.ColorizerGrass;
import net.minecraft.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockGrass extends Block {
    protected BlockGrass(int var1) {
        super(var1, Material.GRASS_MATERIAL);
        this.blockIndexInTexture = 3;
        this.setTickOnLoad(true);
    }

    @Override
    public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
        if (side == 1) {
            return 0;
        } else if (side == 0) {
            return 2;
        } else {
            Material var6 = blockAccess.getBlockMaterial(x, y + 1, z);
            return var6 != Material.SNOW && var6 != Material.BUILT_SNOW ? 3 : 68;
        }
    }

    @Override
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
        blockAccess.getWorldChunkManager().func_4069_a(x, z, 1, 1);
        double var5 = blockAccess.getWorldChunkManager().temperature[0];
        double var7 = blockAccess.getWorldChunkManager().humidity[0];
        return ColorizerGrass.getGrassColor(var5, var7);
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        if (!var1.multiplayerWorld) {
            if (var1.getBlockLightValue(var2, var3 + 1, var4) < 4 && Block.LIGHT_OPACITY[var1.getBlockId(var2, var3 + 1, var4)] > 2) {
                if (var5.nextInt(4) != 0) {
                    return;
                }

                var1.setBlockWithNotify(var2, var3, var4, Block.DIRT.blockID);
            } else if (var1.getBlockLightValue(var2, var3 + 1, var4) >= 9) {
                int var6 = var2 + var5.nextInt(3) - 1;
                int var7 = var3 + var5.nextInt(5) - 3;
                int var8 = var4 + var5.nextInt(3) - 1;
                int var9 = var1.getBlockId(var6, var7 + 1, var8);
                if (var1.getBlockId(var6, var7, var8) == Block.DIRT.blockID && var1.getBlockLightValue(var6, var7 + 1, var8) >= 4 && Block.LIGHT_OPACITY[var9] <= 2) {
                    var1.setBlockWithNotify(var6, var7, var8, Block.GRASS.blockID);
                }
            }

        }
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return Block.DIRT.idDropped(0, var2);
    }
}
