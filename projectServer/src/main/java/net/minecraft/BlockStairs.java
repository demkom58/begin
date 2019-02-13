package net.minecraft;

import util.MathHelper;
import util.Vec3D;

import java.util.ArrayList;
import java.util.Random;

public class BlockStairs extends Block {
    private Block modelBlock;

    protected BlockStairs(int var1, Block var2) {
        super(var1, var2.blockIndexInTexture, var2.blockMaterial);
        this.modelBlock = var2;
        this.setHardness(var2.blockHardness);
        this.setResistance(var2.blockResistance / 3.0F);
        this.setStepSound(var2.stepSound);
        this.setLightOpacity(255);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int var2, int var3, int var4) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isACube() {
        return false;
    }

    public void getCollidingBoundingBoxes(World var1, int var2, int var3, int var4, AxisAlignedBB var5, ArrayList<AxisAlignedBB> var6) {
        int var7 = var1.getBlockMetadata(var2, var3, var4);
        if (var7 == 0) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
            this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
        } else if (var7 == 1) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
            this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
        } else if (var7 == 2) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
            this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
        } else if (var7 == 3) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
            this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void onBlockClicked(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        this.modelBlock.onBlockClicked(world, var2, var3, var4, entityPlayer);
    }

    public void onBlockDestroyedByPlayer(World world, int var2, int var3, int var4, int var5) {
        this.modelBlock.onBlockDestroyedByPlayer(world, var2, var3, var4, var5);
    }

    public float getExplosionResistance(Entity entity) {
        return this.modelBlock.getExplosionResistance(entity);
    }

    public int idDropped(int var1, Random random) {
        return this.modelBlock.idDropped(var1, random);
    }

    public int quantityDropped(Random random) {
        return this.modelBlock.quantityDropped(random);
    }

    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        return this.modelBlock.getBlockTextureFromSideAndMetadata(var1, var2);
    }

    public int getBlockTextureFromSide(int var1) {
        return this.modelBlock.getBlockTextureFromSide(var1);
    }

    public int tickRate() {
        return this.modelBlock.tickRate();
    }

    public void velocityToAddToEntity(World world, int var2, int var3, int var4, Entity entity, Vec3D vec) {
        this.modelBlock.velocityToAddToEntity(world, var2, var3, var4, entity, vec);
    }

    public boolean isCollidable() {
        return this.modelBlock.isCollidable();
    }

    public boolean canCollideCheck(int var1, boolean var2) {
        return this.modelBlock.canCollideCheck(var1, var2);
    }

    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        return this.modelBlock.canPlaceBlockAt(world, var2, var3, var4);
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        this.onNeighborBlockChange(world, x, y, z, 0);
        this.modelBlock.onBlockAdded(world, x, y, z);
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        this.modelBlock.onBlockRemoval(world, x, y, z);
    }

    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int var5, float chance) {
        this.modelBlock.dropBlockAsItemWithChance(world, x, y, z, var5, chance);
    }

    public void onEntityWalking(World world, int var2, int var3, int var4, Entity entity) {
        this.modelBlock.onEntityWalking(world, var2, var3, var4, entity);
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        this.modelBlock.updateTick(world, x, y, z, random);
    }

    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        return this.modelBlock.blockActivated(world, var2, var3, var4, entityPlayer);
    }

    public void onBlockDestroyedByExplosion(World world, int x, int y, int z) {
        this.modelBlock.onBlockDestroyedByExplosion(world, x, y, z);
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving) {
        int var6 = MathHelper.floor_double((double) (entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        if (var6 == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 2);
        }

        if (var6 == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 1);
        }

        if (var6 == 2) {
            world.setBlockMetadataWithNotify(x, y, z, 3);
        }

        if (var6 == 3) {
            world.setBlockMetadataWithNotify(x, y, z, 0);
        }

    }
}
