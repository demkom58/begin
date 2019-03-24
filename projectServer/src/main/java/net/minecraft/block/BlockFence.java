package net.minecraft.block;

import net.minecraft.material.Material;
import net.minecraft.world.World;
import net.minecraft.util.AxisAlignedBB;

public class BlockFence extends Block {
    public BlockFence(int var1, int var2) {
        super(var1, var2, Material.WOOD);
    }

    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        if (world.getBlockId(var2, var3 - 1, var4) == this.blockID) {
            return true;
        } else {
            return world.getBlockMaterial(var2, var3 - 1, var4).isSolid() && super.canPlaceBlockAt(world, var2, var3, var4);
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBoxFromPool((double) x, (double) y, (double) z, (double) (x + 1), (double) ((float) y + 1.5F), (double) (z + 1));
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isACube() {
        return false;
    }
}
