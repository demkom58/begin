package net.minecraft.block;

import net.minecraft.entity.Entity;
import net.minecraft.material.Material;
import net.minecraft.world.World;
import net.minecraft.util.AxisAlignedBB;

public class BlockSoulSand extends Block {
    public BlockSoulSand(int var1, int var2) {
        super(var1, var2, Material.SAND);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        float var5 = 0.125F;
        return AxisAlignedBB.getBoundingBoxFromPool((double) x, (double) y, (double) z, (double) (x + 1), (double) ((float) (y + 1) - var5), (double) (z + 1));
    }

    public void onEntityCollidedWithBlock(World world, int var2, int var3, int var4, Entity entity) {
        entity.motionX *= 0.4D;
        entity.motionZ *= 0.4D;
    }
}
