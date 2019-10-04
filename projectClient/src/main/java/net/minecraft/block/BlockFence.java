package net.minecraft.block;

import net.minecraft.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockFence extends Block {
    public BlockFence(int var1, int var2) {
        super(var1, var2, Material.WOOD);
    }

    @Override
    public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
        if (var1.getBlockId(var2, var3 - 1, var4) == this.blockID) {
            return true;
        } else {
            return var1.getBlockMaterial(var2, var3 - 1, var4).isSolid() && super.canPlaceBlockAt(var1, var2, var3, var4);
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
