package net.potion.block;

import net.potion.entity.EntityFallingSand;
import net.potion.material.Material;
import net.potion.world.World;

import java.util.Random;

public class BlockSand extends Block {
    public static boolean fallInstantly = false;

    public BlockSand(int var1, int var2) {
        super(var1, var2, Material.SAND);
    }

    public static boolean canFallBelow(World var0, int var1, int var2, int var3) {
        int var4 = var0.getBlockId(var1, var2, var3);
        if (var4 == 0) {
            return true;
        } else if (var4 == Block.FIRE.blockID) {
            return true;
        } else {
            Material var5 = Block.BLOCKS_LIST[var4].blockMaterial;
            if (var5 == Material.WATER) {
                return true;
            } else {
                return var5 == Material.LAVA;
            }
        }
    }

    @Override
    public void onBlockAdded(World var1, int var2, int var3, int var4) {
        var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate());
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int var5) {
        world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        this.tryToFall(var1, var2, var3, var4);
    }

    private void tryToFall(World var1, int var2, int var3, int var4) {
        if (canFallBelow(var1, var2, var3 - 1, var4) && var3 >= 0) {
            byte var8 = 32;
            if (!fallInstantly && var1.checkChunksExist(var2 - var8, var3 - var8, var4 - var8, var2 + var8, var3 + var8, var4 + var8)) {
                EntityFallingSand var9 = new EntityFallingSand(var1, (float) var2 + 0.5F, (float) var3 + 0.5F, (float) var4 + 0.5F, this.blockID);
                var1.entityJoinedWorld(var9);
            } else {
                var1.setBlockWithNotify(var2, var3, var4, 0);

                while (canFallBelow(var1, var2, var3 - 1, var4) && var3 > 0) {
                    --var3;
                }

                if (var3 > 0) {
                    var1.setBlockWithNotify(var2, var3, var4, this.blockID);
                }
            }
        }

    }

    @Override
    public int tickRate() {
        return 3;
    }
}
