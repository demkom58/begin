package net.minecraft.item;

import net.hypnosis.util.math.MathConstants;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.material.Material;
import net.minecraft.util.EnumMovingObjectType;
import net.hypnosis.util.math.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.hypnosis.util.math.Vec3d;

public class ItemBucket extends Item {
    private int isFull;

    public ItemBucket(int var1, int var2) {
        super(var1);
        this.maxStackSize = 1;
        this.isFull = var2;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
        float var4 = 1.0F;
        float var5 = var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * var4;
        float var6 = var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * var4;
        double var7 = var3.prevPosX + (var3.posX - var3.prevPosX) * (double) var4;
        double var9 = var3.prevPosY + (var3.posY - var3.prevPosY) * (double) var4 + 1.62D - (double) var3.yOffset;
        double var11 = var3.prevPosZ + (var3.posZ - var3.prevPosZ) * (double) var4;
        Vec3d var13 = new Vec3d(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * MathConstants.RADIANS_PER_DEGREE - MathConstants.PI);
        float var15 = MathHelper.sin(-var6 * MathConstants.RADIANS_PER_DEGREE - MathConstants.PI);
        float var16 = -MathHelper.cos(-var5 * MathConstants.RADIANS_PER_DEGREE);
        float var17 = MathHelper.sin(-var5 * MathConstants.RADIANS_PER_DEGREE);
        float var18 = var15 * var16;
        float var20 = var14 * var16;
        double var21 = 5.0D;
        Vec3d var23 = new Vec3d(var13).add((double) var18 * var21, (double) var17 * var21, (double) var20 * var21);
        MovingObjectPosition var24 = var2.rayTraceBlocks(var13, var23, this.isFull == 0);

        if (var24 != null) {
            if (var24.typeOfHit == EnumMovingObjectType.TILE) {
                int var25 = var24.blockX;
                int var26 = var24.blockY;
                int var27 = var24.blockZ;
                if (!var2.canMineBlock(var3, var25, var26, var27)) {
                    return var1;
                }

                if (this.isFull == 0) {
                    if (var2.getBlockMaterial(var25, var26, var27) == Material.WATER && var2.getBlockMetadata(var25, var26, var27) == 0) {
                        var2.setBlockWithNotify(var25, var26, var27, 0);
                        return new ItemStack(Item.BUCKET_WATER);
                    }

                    if (var2.getBlockMaterial(var25, var26, var27) == Material.LAVA && var2.getBlockMetadata(var25, var26, var27) == 0) {
                        var2.setBlockWithNotify(var25, var26, var27, 0);
                        return new ItemStack(Item.BUCKET_LAVA);
                    }
                } else {
                    if (this.isFull < 0) {
                        return new ItemStack(Item.BUCKET_EMPTY);
                    }

                    if (var24.sideHit == 0) {
                        --var26;
                    }

                    if (var24.sideHit == 1) {
                        ++var26;
                    }

                    if (var24.sideHit == 2) {
                        --var27;
                    }

                    if (var24.sideHit == 3) {
                        ++var27;
                    }

                    if (var24.sideHit == 4) {
                        --var25;
                    }

                    if (var24.sideHit == 5) {
                        ++var25;
                    }

                    if (var2.isAirBlock(var25, var26, var27) || !var2.getBlockMaterial(var25, var26, var27).isSolid()) {
                        if (var2.worldProvider.isHellWorld && this.isFull == Block.WATER_MOVING.blockID) {
                            var2.playSoundEffect(var7 + 0.5D, var9 + 0.5D, var11 + 0.5D, "random.fizz", 0.5F, 2.6F + (var2.rand.nextFloat() - var2.rand.nextFloat()) * 0.8F);

                            for (int var28 = 0; var28 < 8; ++var28) {
                                var2.spawnParticle("largesmoke", (double) var25 + Math.random(), (double) var26 + Math.random(), (double) var27 + Math.random(), 0.0D, 0.0D, 0.0D);
                            }
                        } else {
                            var2.setBlockAndMetadataWithNotify(var25, var26, var27, this.isFull, 0);
                        }

                        return new ItemStack(Item.BUCKET_EMPTY);
                    }
                }
            } else if (this.isFull == 0 && var24.entityHit instanceof EntityCow) {
                return new ItemStack(Item.BUCKET_MILK);
            }

        }
        return var1;
    }
}
