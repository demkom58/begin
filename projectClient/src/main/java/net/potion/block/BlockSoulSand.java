package net.potion.block;

import net.potion.entity.Entity;
import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.world.World;

public class BlockSoulSand extends Block {
    public BlockSoulSand(int var1, int var2) {
        super(var1, var2, Material.SAND);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        float var5 = 0.125F;
        return AxisAlignedBB.getBoundingBoxFromPool(var2, var3, var4, var2 + 1, (float) (var3 + 1) - var5, var4 + 1);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        entity.motionX *= 0.4D;
        entity.motionZ *= 0.4D;
    }
}
