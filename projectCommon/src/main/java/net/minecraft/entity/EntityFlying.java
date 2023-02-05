package net.minecraft.entity;

import net.minecraft.block.Block;
import net.hypnosis.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityFlying extends EntityLiving {
    public EntityFlying(World var1) {
        super(var1);
    }

    @Override
    protected void fall(float var1) {
    }

    @Override
    public void moveEntityWithHeading(float var1, float var2) {
        if (this.isInWater()) {
            this.moveFlying(var1, var2, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        } else if (this.handleLavaMovement()) {
            this.moveFlying(var1, var2, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        } else {
            float var3 = 0.91F;
            if (this.onGround) {
                var3 = 0.54600006F;
                int var4 = this.world.getBlockId(MathHelper.floor(this.posX), MathHelper.floor(this.boundingBox.minY) - 1, MathHelper.floor(this.posZ));
                if (var4 > 0) {
                    var3 = Block.BLOCKS_LIST[var4].slipperiness * 0.91F;
                }
            }

            float var10 = 0.16277136F / (var3 * var3 * var3);
            this.moveFlying(var1, var2, this.onGround ? 0.1F * var10 : 0.02F);
            var3 = 0.91F;
            if (this.onGround) {
                var3 = 0.54600006F;
                int var5 = this.world.getBlockId(MathHelper.floor(this.posX), MathHelper.floor(this.boundingBox.minY) - 1, MathHelper.floor(this.posZ));
                if (var5 > 0) {
                    var3 = Block.BLOCKS_LIST[var5].slipperiness * 0.91F;
                }
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= var3;
            this.motionY *= var3;
            this.motionZ *= var3;
        }

        this.field5 = this.field6;
        double var9 = this.posX - this.prevPosX;
        double var11 = this.posZ - this.prevPosZ;
        float var7 = MathHelper.sqrt(var9 * var9 + var11 * var11) * 4.0F;
        if (var7 > 1.0F) {
            var7 = 1.0F;
        }

        this.field6 += (var7 - this.field6) * 0.4F;
        this.field7 += this.field6;
    }

    @Override
    public boolean isOnLadder() {
        return false;
    }
}
