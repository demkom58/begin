package net.potion.block;

import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.world.World;

public class BlockFence extends Block {
    public BlockFence(int var1, int var2) {
        super(var1, var2, Material.WOOD);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        if (world.getBlockId(x, y - 1, z) == this.blockID) {
            return true;
        } else {
            return world.getBlockMaterial(x, y - 1, z).isSolid() && super.canPlaceBlockAt(world, x, y, z);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        return AxisAlignedBB.getBoundingBoxFromPool(var2, var3, var4, var2 + 1, (float) var3 + 1.5F, var4 + 1);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 11;
    }
}
