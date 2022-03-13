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
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        return super.getCollisionBoundingBoxFromPool(var1, var2, var3, var4);
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
        return 10;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return super.shouldSideBeRendered(blockAccess, x, y, z, side);
    }

    @Override
    public void getCollidingBoundingBoxes(World var1, int var2, int var3, int var4, AxisAlignedBB var5, List<AxisAlignedBB> bbs) {
        int var7 = var1.getBlockMetadata(var2, var3, var4);
        if (var7 == 0) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
            this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
        } else if (var7 == 1) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
            this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
        } else if (var7 == 2) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
            this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
        } else if (var7 == 3) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
            this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
            super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
        this.modelBlock.randomDisplayTick(var1, var2, var3, var4, var5);
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        this.modelBlock.onBlockClicked(world, x, y, z, player);
    }

    @Override
    public void onBlockDestroyedByPlayer(World var1, int var2, int var3, int var4, int var5) {
        this.modelBlock.onBlockDestroyedByPlayer(var1, var2, var3, var4, var5);
    }

    @Override
    public float getBlockBrightness(IBlockAccess blockAccess, int x, int y, int z) {
        return this.modelBlock.getBlockBrightness(blockAccess, x, y, z);
    }

    @Override
    public float getExplosionResistance(Entity var1) {
        return this.modelBlock.getExplosionResistance(var1);
    }

    @Override
    public int getRenderBlockPass() {
        return this.modelBlock.getRenderBlockPass();
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return this.modelBlock.idDropped(var1, var2);
    }

    @Override
    public int quantityDropped(Random var1) {
        return this.modelBlock.quantityDropped(var1);
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        return this.modelBlock.getBlockTextureFromSideAndMetadata(side, metadata);
    }

    @Override
    public int getBlockTextureFromSide(int side) {
        return this.modelBlock.getBlockTextureFromSide(side);
    }

    @Override
    public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return this.modelBlock.getBlockTexture(blockAccess, x, y, z, side);
    }

    @Override
    public int tickRate() {
        return this.modelBlock.tickRate();
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return this.modelBlock.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public Vec3d velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3d velocity) {
        return this.modelBlock.velocityToAddToEntity(world, x, y, z, entity, velocity);
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
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return this.modelBlock.canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public void onBlockAdded(World var1, int var2, int var3, int var4) {
        this.onNeighborBlockChange(var1, var2, var3, var4, 0);
        this.modelBlock.onBlockAdded(var1, var2, var3, var4);
    }

    @Override
    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        this.modelBlock.onBlockRemoval(var1, var2, var3, var4);
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int var5, float failChance) {
        this.modelBlock.dropBlockAsItemWithChance(world, x, y, z, var5, failChance);
    }

    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        this.modelBlock.onEntityWalking(world, x, y, z, entity);
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        this.modelBlock.updateTick(var1, var2, var3, var4, var5);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        return this.modelBlock.blockActivated(world, x, y, z, player);
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z) {
        this.modelBlock.onBlockDestroyedByExplosion(world, x, y, z);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity) {
        int var6 = MathHelper.floor((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
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
