package net.minecraft.block;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.material.Material;
import net.minecraft.world.World;
import net.minecraft.util.AxisAlignedBB;

import java.util.Random;

public class BlockWeb extends Block {
    public BlockWeb(int var1, int var2) {
        super(var1, var2, Material.WEB);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int var2, int var3, int var4, Entity entity) {
        entity.isInWeb = true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean isACube() {
        return false;
    }

    @Override
    public int idDropped(int var1, Random random) {
        return Item.SILK.shiftedIndex;
    }
}
