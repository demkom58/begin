package net.minecraft.block;

import net.minecraft.material.Material;
import net.minecraft.world.World;

import java.util.Random;

public class BlockStationary extends BlockFluid {
    protected BlockStationary(int var1, Material var2) {
        super(var1, var2);
        this.setTickOnLoad(false);
        if (var2 == Material.LAVA) {
            this.setTickOnLoad(true);
        }

    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int var5) {
        super.onNeighborBlockChange(world, x, y, z, var5);
        if (world.getBlockId(x, y, z) == this.blockID) {
            this.update(world, x, y, z);
        }

    }

    private void update(World var1, int var2, int var3, int var4) {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        var1.editingBlocks = true;
        var1.setBlockAndMetadata(var2, var3, var4, this.blockID - 1, var5);
        var1.markBlocksDirty(var2, var3, var4, var2, var3, var4);
        var1.scheduleBlockUpdate(var2, var3, var4, this.blockID - 1, this.tickRate());
        var1.editingBlocks = false;
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        if (this.blockMaterial == Material.LAVA) {
            int var6 = var5.nextInt(3);

            for (int var7 = 0; var7 < var6; ++var7) {
                var2 += var5.nextInt(3) - 1;
                ++var3;
                var4 += var5.nextInt(3) - 1;
                int var8 = var1.getBlockId(var2, var3, var4);
                if (var8 == 0) {
                    if (this.isBurning(var1, var2 - 1, var3, var4) || this.isBurning(var1, var2 + 1, var3, var4) || this.isBurning(var1, var2, var3, var4 - 1) || this.isBurning(var1, var2, var3, var4 + 1) || this.isBurning(var1, var2, var3 - 1, var4) || this.isBurning(var1, var2, var3 + 1, var4)) {
                        var1.setBlockWithNotify(var2, var3, var4, Block.FIRE.blockID);
                        return;
                    }
                } else if (Block.BLOCKS_LIST[var8].blockMaterial.getIsSolid()) {
                    return;
                }
            }
        }

    }

    private boolean isBurning(World var1, int var2, int var3, int var4) {
        return var1.getBlockMaterial(var2, var3, var4).isBurning();
    }
}
