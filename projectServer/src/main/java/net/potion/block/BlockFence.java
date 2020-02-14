package net.potion.block;

import net.potion.material.Material;
import net.potion.world.World;
import net.potion.util.AxisAlignedBB;

public class BlockFence extends Block {
    public BlockFence(int var1, int var2) {
        super(var1, var2, Material.WOOD);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        if (world.getBlockId(var2, var3 - 1, var4) == this.blockID) {
            return true;
        } else {
            return world.getBlockMaterial(var2, var3 - 1, var4).isSolid() && super.canPlaceBlockAt(world, var2, var3, var4);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBoxFromPool(x, y, z, x + 1, (float) y + 1.5F, z + 1);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isACube() {
        return false;
    }
}
