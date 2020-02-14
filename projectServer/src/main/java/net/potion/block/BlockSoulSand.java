package net.potion.block;

import net.potion.entity.Entity;
import net.potion.material.Material;
import net.potion.world.World;
import net.potion.util.AxisAlignedBB;

public class BlockSoulSand extends Block {
    public BlockSoulSand(int var1, int var2) {
        super(var1, var2, Material.SAND);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        float var5 = 0.125F;
        return AxisAlignedBB.getBoundingBoxFromPool(x, y, z, x + 1, (float) (y + 1) - var5, z + 1);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int var2, int var3, int var4, Entity entity) {
        entity.motionX *= 0.4D;
        entity.motionZ *= 0.4D;
    }
}
