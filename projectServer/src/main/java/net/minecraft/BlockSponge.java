package net.minecraft;

public class BlockSponge extends Block {
    protected BlockSponge(int var1) {
        super(var1, Material.SPONGE);
        this.blockIndexInTexture = 48;
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        byte var5 = 2;

        for (int var6 = x - var5; var6 <= x + var5; ++var6) {
            for (int var7 = y - var5; var7 <= y + var5; ++var7) {
                for (int var8 = z - var5; var8 <= z + var5; ++var8) {
                    if (world.getBlockMaterial(var6, var7, var8) == Material.WATER) {
                    }
                }
            }
        }

    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        byte var5 = 2;

        for (int var6 = x - var5; var6 <= x + var5; ++var6) {
            for (int var7 = y - var5; var7 <= y + var5; ++var7) {
                for (int var8 = z - var5; var8 <= z + var5; ++var8) {
                    world.notifyBlocksOfNeighborChange(var6, var7, var8, world.getBlockId(var6, var7, var8));
                }
            }
        }

    }
}
