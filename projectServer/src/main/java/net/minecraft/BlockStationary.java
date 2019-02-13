package net.minecraft;

import java.util.Random;

public class BlockStationary extends BlockFluid {
    protected BlockStationary(int var1, Material var2) {
        super(var1, var2);
        this.setTickOnLoad(false);
        if (var2 == Material.lava) {
            this.setTickOnLoad(true);
        }

    }

    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        super.onNeighborBlockChange(world, var2, var3, var4, var5);
        if (world.getBlockId(var2, var3, var4) == this.blockID) {
            this.func_30005_i(world, var2, var3, var4);
        }

    }

    private void func_30005_i(World var1, int var2, int var3, int var4) {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        var1.editingBlocks = true;
        var1.setBlockAndMetadata(var2, var3, var4, this.blockID - 1, var5);
        var1.markBlocksDirty(var2, var3, var4, var2, var3, var4);
        var1.scheduleUpdateTick(var2, var3, var4, this.blockID - 1, this.tickRate());
        var1.editingBlocks = false;
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (this.blockMaterial == Material.lava) {
            int var6 = random.nextInt(3);

            for (int var7 = 0; var7 < var6; ++var7) {
                x += random.nextInt(3) - 1;
                ++y;
                z += random.nextInt(3) - 1;
                int var8 = world.getBlockId(x, y, z);
                if (var8 == 0) {
                    if (this.func_4033_j(world, x - 1, y, z) || this.func_4033_j(world, x + 1, y, z) || this.func_4033_j(world, x, y, z - 1) || this.func_4033_j(world, x, y, z + 1) || this.func_4033_j(world, x, y - 1, z) || this.func_4033_j(world, x, y + 1, z)) {
                        world.setBlockWithNotify(x, y, z, Block.FIRE.blockID);
                        return;
                    }
                } else if (Block.BLOCKS_LIST[var8].blockMaterial.getIsSolid()) {
                    return;
                }
            }
        }

    }

    private boolean func_4033_j(World var1, int var2, int var3, int var4) {
        return var1.getBlockMaterial(var2, var3, var4).getBurning();
    }
}
