package net.potion.entity.passive;

import net.hypnosis.util.math.MathConstants;
import net.potion.entity.Entity;
import net.potion.entity.item.EntityItem;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.Item;
import net.potion.item.ItemStack;
import net.potion.material.Material;
import net.potion.nbt.TagCompound;
import net.potion.stats.StatList;
import net.potion.util.AxisAlignedBB;
import net.hypnosis.util.math.MathHelper;
import net.potion.util.MovingObjectPosition;
import net.potion.world.World;
import net.hypnosis.util.math.Vec3d;

import java.util.List;

public class EntityFish extends Entity {
    public int shake = 0;
    public EntityPlayer angler;
    public Entity bobber = null;
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private int inTile = 0;
    private boolean inGround = false;
    private int ticksInGround;
    private int ticksInAir = 0;
    private int ticksCatchable = 0;
    private int field_6149_an;
    private double field_6148_ao;
    private double field_6147_ap;
    private double field_6146_aq;
    private double field_6145_ar;
    private double field_6144_as;

    public EntityFish(World var1) {
        super(var1);
        this.setSize(0.25F, 0.25F);
        this.ignoreFrustumCheck = true;
    }

    public EntityFish(World var1, EntityPlayer var2) {
        super(var1);
        this.ignoreFrustumCheck = true;
        this.angler = var2;
        this.angler.fishEntity = this;
        this.setSize(0.25F, 0.25F);
        this.setLocationAndAngles(var2.posX, var2.posY + 1.62D - (double) var2.yOffset, var2.posZ, var2.rotationYaw, var2.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * MathConstants.PI) * 0.16F;
        this.posY -= 0.10000000149011612D;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * MathConstants.PI) * 0.16F;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        float var3 = 0.4F;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * MathConstants.PI) * MathHelper.cos(this.rotationPitch / 180.0F * MathConstants.PI) * var3;
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * MathConstants.PI) * MathHelper.cos(this.rotationPitch / 180.0F * MathConstants.PI) * var3;
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * MathConstants.PI) * var3;
        this.func_6142_a(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
    }

    @Override
    protected void entityInit() {
    }

    public void func_6142_a(double var1, double var3, double var5, float var7, float var8) {
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
    public void onUpdate() {
        super.onUpdate();
        if (this.field_6149_an > 0) {
            double var22 = this.posX + (this.field_6148_ao - this.posX) / (double) this.field_6149_an;
            double var24 = this.posY + (this.field_6147_ap - this.posY) / (double) this.field_6149_an;
            double var25 = this.posZ + (this.field_6146_aq - this.posZ) / (double) this.field_6149_an;

            double var7;
            for (var7 = this.field_6145_ar - (double) this.rotationYaw; var7 < -180.0D; var7 += 360.0D) {
            }

            while (var7 >= 180.0D) {
                var7 -= 360.0D;
            }

            this.rotationYaw = (float) ((double) this.rotationYaw + var7 / (double) this.field_6149_an);
            this.rotationPitch = (float) ((double) this.rotationPitch + (this.field_6144_as - (double) this.rotationPitch) / (double) this.field_6149_an);
            --this.field_6149_an;
            this.setPosition(var22, var24, var25);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        } else {
            if (!this.worldObj.singleplayerWorld) {
                ItemStack var1 = this.angler.getCurrentEquippedItem();
                if (this.angler.isDead || !this.angler.isEntityAlive() || var1 == null || var1.getItem() != Item.FISHING_ROD || this.getDistanceSqToEntity(this.angler) > 1024.0D) {
                    this.setEntityDead();
                    this.angler.fishEntity = null;
                    return;
                }

                if (this.bobber != null) {
                    if (!this.bobber.isDead) {
                        this.posX = this.bobber.posX;
                        this.posY = this.bobber.boundingBox.minY + (double) this.bobber.height * 0.8D;
                        this.posZ = this.bobber.posZ;
                        return;
                    }

                    this.bobber = null;
                }
            }

            if (this.shake > 0) {
                --this.shake;
            }

            if (this.inGround) {
                int var19 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
                if (var19 == this.inTile) {
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

            Vec3d var20 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d var2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var20, var2);
            var20 = new Vec3d(this.posX, this.posY, this.posZ);
            var2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (var3 != null) {
                var2 = new Vec3d(var3.hitVec.x, var3.hitVec.y, var3.hitVec.z);
            }

            Entity var4 = null;
            List<Entity> var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double var6 = 0.0D;

            for (int var8 = 0; var8 < var5.size(); ++var8) {
                Entity var9 = var5.get(var8);
                if (var9.canBeCollidedWith() && (var9 != this.angler || this.ticksInAir >= 5)) {
                    float var10 = 0.3F;
                    AxisAlignedBB var11 = var9.boundingBox.expand(var10, var10, var10);
                    MovingObjectPosition var12 = var11.raycast(var20, var2);
                    if (var12 != null) {
                        double var13 = var20.distanceTo(var12.hitVec);
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

            if (var3 != null) {
                if (var3.entityHit != null) {
                    if (var3.entityHit.attackEntityFrom(this.angler, 0)) {
                        this.bobber = var3.entityHit;
                    }
                } else {
                    this.inGround = true;
                }
            }

            if (!this.inGround) {
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                float var26 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

                for (this.rotationPitch = (float) (Math.atan2(this.motionY, var26) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
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
                float var27 = 0.92F;
                if (this.onGround || this.isCollidedHorizontally) {
                    var27 = 0.5F;
                }

                byte var28 = 5;
                double var29 = 0.0D;

                for (int var30 = 0; var30 < var28; ++var30) {
                    double var14 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double) (var30) / (double) var28 - 0.125D + 0.125D;
                    double var16 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double) (var30 + 1) / (double) var28 - 0.125D + 0.125D;
                    AxisAlignedBB var18 = AxisAlignedBB.getBoundingBoxFromPool(this.boundingBox.minX, var14, this.boundingBox.minZ, this.boundingBox.maxX, var16, this.boundingBox.maxZ);
                    if (this.worldObj.isAABBInMaterial(var18, Material.WATER)) {
                        var29 += 1.0D / (double) var28;
                    }
                }

                if (var29 > 0.0D) {
                    if (this.ticksCatchable > 0) {
                        --this.ticksCatchable;
                    } else {
                        short var31 = 500;
                        if (this.worldObj.canLightningStrikeAt(MathHelper.floor(this.posX), MathHelper.floor(this.posY) + 1, MathHelper.floor(this.posZ))) {
                            var31 = 300;
                        }

                        if (this.rand.nextInt(var31) == 0) {
                            this.ticksCatchable = this.rand.nextInt(30) + 10;
                            this.motionY -= 0.20000000298023224D;
                            this.worldObj.playSoundAtEntity(this, "random.splash", 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                            float var33 = (float) MathHelper.floor(this.boundingBox.minY);

                            for (int var15 = 0; (float) var15 < 1.0F + this.width * 20.0F; ++var15) {
                                float var35 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                                float var17 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                                this.worldObj.spawnParticle("bubble", this.posX + (double) var35, var33 + 1.0F, this.posZ + (double) var17, this.motionX, this.motionY - (double) (this.rand.nextFloat() * 0.2F), this.motionZ);
                            }

                            for (int var34 = 0; (float) var34 < 1.0F + this.width * 20.0F; ++var34) {
                                float var36 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                                float var37 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                                this.worldObj.spawnParticle("splash", this.posX + (double) var36, var33 + 1.0F, this.posZ + (double) var37, this.motionX, this.motionY, this.motionZ);
                            }
                        }
                    }
                }

                if (this.ticksCatchable > 0) {
                    this.motionY -= (double) (this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat()) * 0.2D;
                }

                double var32 = var29 * 2.0D - 1.0D;
                this.motionY += 0.03999999910593033D * var32;
                if (var29 > 0.0D) {
                    var27 = (float) ((double) var27 * 0.9D);
                    this.motionY *= 0.8D;
                }

                this.motionX *= var27;
                this.motionY *= var27;
                this.motionZ *= var27;
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
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

    public int catchFish() {
        byte var1 = 0;
        if (this.bobber != null) {
            double var2 = this.angler.posX - this.posX;
            double var4 = this.angler.posY - this.posY;
            double var6 = this.angler.posZ - this.posZ;
            double var8 = MathHelper.sqrt(var2 * var2 + var4 * var4 + var6 * var6);
            double var10 = 0.1D;
            this.bobber.motionX += var2 * var10;
            this.bobber.motionY += var4 * var10 + (double) MathHelper.sqrt(var8) * 0.08D;
            this.bobber.motionZ += var6 * var10;
            var1 = 3;
        } else if (this.ticksCatchable > 0) {
            EntityItem var13 = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.FISH_RAW));
            double var3 = this.angler.posX - this.posX;
            double var5 = this.angler.posY - this.posY;
            double var7 = this.angler.posZ - this.posZ;
            double var9 = MathHelper.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
            double var11 = 0.1D;
            var13.motionX = var3 * var11;
            var13.motionY = var5 * var11 + (double) MathHelper.sqrt(var9) * 0.08D;
            var13.motionZ = var7 * var11;
            this.worldObj.entityJoinedWorld(var13);
            this.angler.addStat(StatList.fishCaughtStat, 1);
            var1 = 1;
        }

        if (this.inGround) {
            var1 = 2;
        }

        this.setEntityDead();
        this.angler.fishEntity = null;
        return var1;
    }
}
