package net.minecraft.block;

import net.minecraft.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockLockedChest extends Block {
    protected BlockLockedChest(int var1) {
        super(var1, Material.WOOD);
        this.blockIndexInTexture = 26;
    }

    @Override
    public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5) {
        if (var5 == 1) {
            return this.blockIndexInTexture - 1;
        } else if (var5 == 0) {
            return this.blockIndexInTexture - 1;
        } else {
            int var6 = var1.getBlockId(var2, var3, var4 - 1);
            int var7 = var1.getBlockId(var2, var3, var4 + 1);
            int var8 = var1.getBlockId(var2 - 1, var3, var4);
            int var9 = var1.getBlockId(var2 + 1, var3, var4);
            byte var10 = 3;
            if (Block.OPAQUE_CUBE_LOOKUP[var6] && !Block.OPAQUE_CUBE_LOOKUP[var7]) {
                var10 = 3;
            }

            if (Block.OPAQUE_CUBE_LOOKUP[var7] && !Block.OPAQUE_CUBE_LOOKUP[var6]) {
                var10 = 2;
            }

            if (Block.OPAQUE_CUBE_LOOKUP[var8] && !Block.OPAQUE_CUBE_LOOKUP[var9]) {
                var10 = 5;
            }

            if (Block.OPAQUE_CUBE_LOOKUP[var9] && !Block.OPAQUE_CUBE_LOOKUP[var8]) {
                var10 = 4;
            }

            return var5 == var10 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
        }
    }

    @Override
    public int getBlockTextureFromSide(int side) {
        if (side == 1) {
            return this.blockIndexInTexture - 1;
        } else if (side == 0) {
            return this.blockIndexInTexture - 1;
        } else {
            return side == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
        }
    }

    @Override
    public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
        return true;
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        var1.setBlockWithNotify(var2, var3, var4, 0);
    }
}
