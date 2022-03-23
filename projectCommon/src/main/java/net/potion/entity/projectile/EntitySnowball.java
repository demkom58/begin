package net.potion.entity.projectile;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.hypnosis.util.math.MathConstants;
import net.potion.entity.Entity;
import net.potion.entity.EntityLiving;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.Item;
import net.potion.item.ItemStack;
import net.potion.nbt.TagCompound;
import net.potion.util.AxisAlignedBB;
import net.hypnosis.util.math.MathHelper;
import net.potion.util.MovingObjectPosition;
import net.potion.world.World;
import net.hypnosis.util.math.Vec3d;

import java.util.List;

public class EntitySnowball extends Entity {
    public int shake = 0;
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private int inTile = 0;
    private boolean inGround = false;
    private EntityLiving owner;
    private int ticksInGround;
    private int ticksInAir = 0;

    public EntitySnowball(World var1) {
        super(var1);
        this.setSize(0.25F, 0.25F);
    }

    public EntitySnowball(World var1, EntityLiving var2) {
        super(var1);
        this.owner = var2;
        this.setSize(0.25F, 0.25F);
        this.setLocationAndAngles(var2.posX, var2.posY + (double) var2.getEyeHeight(), var2.posZ, var2.rotationYaw, var2.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * MathConstants.PI) * 0.16F;
        this.posY -= 0.10000000149011612D;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * MathConstants.PI) * 0.16F;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        float var3 = 0.4F;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * MathConstants.PI) * MathHelper.cos(this.rotationPitch / 180.0F * MathConstants.PI) * var3;
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * MathConstants.PI) * MathHelper.cos(this.rotationPitch / 180.0F * MathConstants.PI) * var3;
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * MathConstants.PI) * var3;
        this.setSnowballHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
    }

    public EntitySnowball(World var1, double var2, double var4, double var6) {
        super(var1);
        this.ticksInGround = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(var2, var4, var6);
        this.yOffset = 0.0F;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    @Side(CodeSide.CLIENT)
    public boolean isInRangeToRenderDist(double var1) {
        double var3 = this.boundingBox.getAverageEdgeLength() * 4.0D;
        var3 = var3 * 64.0D;
        return var1 < var3 * var3;
    }

    public void setSnowballHeading(double var1, double var3, double var5, float var7, float var8) {
        float var9 = MathHelper.sqrt(var1 * var1 + var3 * var3 + var5 * var5);
        var1 = var1 / (double) var9;
        var3 = var3 / (double) var9;
        var5 = var5 / (double) var9;
        var1 = var1 + this.rand.nextGaussian() * 0.007499999832361937D * (double) var8;
        var3 = var3 + this.rand.nextGaussian() * 0.007499999832361937D * (double) var8;
        var5 = var5 + this.rand.nextGaussian() * 0.007499999832361937D * (double) var8;
        var1 = var1 * (double) var7;
        var3 = var3 * (double) var7;
        var5 = var5 * (double) var7;
        this.motionX = var1;
        this.motionY = var3;
        this.motionZ = var5;
        float var10 = MathHelper.sqrt(var1 * var1 + var5 * var5);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(var1, var5) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(var3, var10) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public void setVelocity(double var1, double var3, double var5) {
        this.motionX = var1;
        this.motionY = var3;
        this.motionZ = var5;
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float var7 = MathHelper.sqrt(var1 * var1 + var5 * var5);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(var1, var5) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(var3, var7) * 180.0D / Math.PI);
        }

    }

    @Override
    public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        if (this.shake > 0) {
            --this.shake;
        }

        if (this.inGround) {
            int var1 = this.world.getBlockId(this.xTile, this.yTile, this.zTile);
            if (var1 == this.inTile) {
                ++this.ticksInGround;
                if (this.ticksInGround == 1200) {
                    this.setEntityDead();
                }

                return;
            }

            this.inGround = false;
            this.motionX *= this.rand.nextFloat() * 0.2F;
            this.motionY *= this.rand.nextFloat() * 0.2F;
            this.motionZ *= this.rand.nextFloat() * 0.2F;
            this.ticksInGround = 0;
            this.ticksInAir = 0;
        } else {
            ++this.ticksInAir;
        }

        Vec3d var15 = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d var2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition var3 = this.world.rayTraceBlocks(var15, var2);
        var15 = new Vec3d(this.posX, this.posY, this.posZ);
        var2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (var3 != null) {
            var2 = new Vec3d(var3.hitVec.x, var3.hitVec.y, var3.hitVec.z);
        }

        if (!this.world.localWorld) {
            Entity var4 = null;
            List<Entity> var5 = this.world.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double var6 = 0.0D;

            for (int var8 = 0; var8 < var5.size(); ++var8) {
                Entity var9 = var5.get(var8);
                if (var9.canBeCollidedWith() && (var9 != this.owner || this.ticksInAir >= 5)) {
                    float var10 = 0.3F;
                    AxisAlignedBB var11 = var9.boundingBox.expand(var10, var10, var10);
                    MovingObjectPosition var12 = var11.raycast(var15, var2);
                    if (var12 != null) {
                        double var13 = var15.distanceTo(var12.hitVec);
                        if (var13 < var6 || var6 == 0.0D) {
                            var4 = var9;
                            var6 = var13;
                        }
                    }
                }
            }

            if (var4 != null) {
                var3 = new MovingObjectPosition(var4);
            }
        }

        if (var3 != null) {
            if (var3.entityHit != null) {
                var3.entityHit.attackEntityFrom(this.owner, 0);
            }

            for (int var18 = 0; var18 < 8; ++var18) {
                this.world.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            }

            this.setEntityDead();
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float var19 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        this.rotationPitch = (float) (Math.atan2(this.motionY, var19) * 180.0D / Math.PI);
        while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
            this.prevRotationPitch -= 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float var20 = 0.99F;
        float var21 = 0.03F;
        if (this.isInWater()) {
            for (int var7 = 0; var7 < 4; ++var7) {
                float var22 = 0.25F;
                this.world.spawnParticle("bubble", this.posX - this.motionX * (double) var22, this.posY - this.motionY * (double) var22, this.posZ - this.motionZ * (double) var22, this.motionX, this.motionY, this.motionZ);
            }

            var20 = 0.8F;
        }

        this.motionX *= var20;
        this.motionY *= var20;
        this.motionZ *= var20;
        this.motionY -= var21;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    @Override
    public void writeEntityToNBT(TagCompound var1) {
        var1.setShort("xTile", (short) this.xTile);
        var1.setShort("yTile", (short) this.yTile);
        var1.setShort("zTile", (short) this.zTile);
        var1.setByte("inTile", (byte) this.inTile);
        var1.setByte("shake", (byte) this.shake);
        var1.setByte("inGround", (byte) (this.inGround ? 1 : 0));
    }

    @Override
    public void readEntityFromNBT(TagCompound var1) {
        this.xTile = var1.getShort("xTile");
        this.yTile = var1.getShort("yTile");
        this.zTile = var1.getShort("zTile");
        this.inTile = var1.getByte("inTile") & 255;
        this.shake = var1.getByte("shake") & 255;
        this.inGround = var1.getByte("inGround") == 1;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer var1) {
        if (this.inGround && this.owner == var1 && this.shake <= 0 && var1.inventory.addItemStackToInventory(new ItemStack(Item.ARROW, 1))) {
            this.world.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            var1.onItemPickup(this, 1);
            this.setEntityDead();
        }

    }

    @Override
    @Side(CodeSide.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }
}
