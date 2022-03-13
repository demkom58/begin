package net.potion.item;

import net.hypnosis.util.math.MathConstants;
import net.potion.block.Block;
import net.potion.entity.item.EntityBoat;
import net.potion.entity.player.EntityPlayer;
import net.potion.util.EnumMovingObjectType;
import net.hypnosis.util.math.MathHelper;
import net.potion.util.MovingObjectPosition;
import net.potion.world.World;
import net.hypnosis.util.math.Vec3d;

public class ItemBoat extends Item {
    public ItemBoat(int var1) {
        super(var1);
        this.maxStackSize = 1;
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
        MovingObjectPosition var24 = var2.rayTraceBlocks(var13, var23, true);
        if (var24 == null) {
            return var1;
        } else {
            if (var24.typeOfHit == EnumMovingObjectType.TILE) {
                int var25 = var24.blockX;
                int var26 = var24.blockY;
                int var27 = var24.blockZ;
                if (!var2.singleplayerWorld) {
                    if (var2.getBlockId(var25, var26, var27) == Block.SNOW.blockID) {
                        --var26;
                    }

                    var2.entityJoinedWorld(new EntityBoat(var2, (float) var25 + 0.5F, (float) var26 + 1.0F, (float) var27 + 0.5F));
                }

                --var1.stackSize;
            }

            return var1;
        }
    }
}
