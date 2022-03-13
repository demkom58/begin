package net.potion.block;

import net.potion.entity.Entity;
import net.potion.entity.EntityLiving;
import net.potion.entity.player.EntityPlayer;
import net.potion.util.AxisAlignedBB;
import net.hypnosis.util.math.MathHelper;
import net.potion.world.IBlockAccess;
import net.potion.world.World;
import net.hypnosis.util.math.Vec3d;

import java.util.List;
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

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int var2, int var3, int var4) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isACube() {
        return false;
    }

    @Override
    public void getCollidingBoundingBoxes(World var1, int var2, int var3, int var4, AxisAlignedBB var5, List<AxisAlignedBB> var6) {
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

    @Override
    public void onBlockClicked(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        this.modelBlock.onBlockClicked(world, var2, var3, var4, entityPlayer);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int var2, int var3, int var4, int var5) {
        this.modelBlock.onBlockDestroyedByPlayer(world, var2, var3, var4, var5);
    }

    @Override
    public float getExplosionResistance(Entity entity) {
        return this.modelBlock.getExplosionResistance(entity);
    }

    @Override
    public int idDropped(int var1, Random random) {
        return this.modelBlock.idDropped(var1, random);
    }

    @Override
    public int quantityDropped(Random random) {
        return this.modelBlock.quantityDropped(random);
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        return this.modelBlock.getBlockTextureFromSideAndMetadata(var1, var2);
    }

    @Override
    public int getBlockTextureFromSide(int var1) {
        return this.modelBlock.getBlockTextureFromSide(var1);
    }

    @Override
    public int tickRate() {
        return this.modelBlock.tickRate();
    }

    @Override
    public Vec3d velocityToAddToEntity(World world, int var2, int var3, int var4, Entity entity, Vec3d vec) {
        return this.modelBlock.velocityToAddToEntity(world, var2, var3, var4, entity, vec);
    }

    @Override
    public boolean isCollidable() {
        return this.modelBlock.isCollidable();
    }

    @Override
    public boolean canCollideCheck(int var1, boolean var2) {
        return this.modelBlock.canCollideCheck(var1, var2);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        return this.modelBlock.canPlaceBlockAt(world, var2, var3, var4);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        this.onNeighborBlockChange(world, x, y, z, 0);
        this.modelBlock.onBlockAdded(world, x, y, z);
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        this.modelBlock.onBlockRemoval(world, x, y, z);
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int var5, float chance) {
        this.modelBlock.dropBlockAsItemWithChance(world, x, y, z, var5, chance);
    }

    @Override
    public void onEntityWalking(World world, int var2, int var3, int var4, Entity entity) {
        this.modelBlock.onEntityWalking(world, var2, var3, var4, entity);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        this.modelBlock.updateTick(world, x, y, z, random);
    }

    @Override
    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        return this.modelBlock.blockActivated(world, var2, var3, var4, entityPlayer);
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z) {
        this.modelBlock.onBlockDestroyedByExplosion(world, x, y, z);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving) {
        int var6 = MathHelper.floor((double) (entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
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
