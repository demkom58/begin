package net.minecraft.entity;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.hypnosis.util.math.MathConstants;
import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.material.Material;
import net.minecraft.nbt.TagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.hypnosis.util.math.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.hypnosis.util.math.Vec3d;

import java.util.List;

public abstract class EntityLiving extends Entity {
    public int heartsHalvesLife = 20;
    public float field1 = (float) Math.random() * 12398.0F;
    public float field2 = (float) (Math.random() + 1.0D) * 0.01F;
    public float renderYawOffset = 0.0F;
    public float prevRenderYawOffset = 0.0F;
    public boolean isMultiplayerEntity = false;
    public float prevSwingProgress;
    public float swingProgress;
    public int health = 10;
    public int prevHealth;
    public int hurtTime;
    public int maxHurtTime;
    public float attackedAtYaw = 0.0F;
    public int deathTime = 0;
    public int attackTime = 0;
    public float prevCameraPitch;
    public float cameraPitch;
    public int field3 = -1;
    public float field4 = (float) (Math.random() * 0.8999999761581421D + 0.10000000149011612D);
    public float field5;
    public float field6;
    public float field7;
    protected float field8;
    protected float field9;
    protected float field10;
    protected float field11;
    protected boolean field12 = true;
    protected String texture = "/mob/char.png";
    protected boolean field13 = true;
    protected float field14 = 0.0F;
    protected String entityType = null;
    protected float field15 = 1.0F;
    protected int scoreValue = 0;
    protected float field16 = 0.0F;
    protected boolean unused_flag = false;
    protected int newPosRotationIncrements;
    protected double newPosX;
    protected double newPosY;
    protected double newPosZ;
    protected double newRotationYaw;
    protected double newRotationPitch;
    protected int naturalArmorRating = 0;
    protected int entityAge = 0;
    protected float moveStrafing;
    protected float moveForward;
    protected float randomYawVelocity;
    protected boolean isJumping = false;
    protected float defaultPitch = 0.0F;
    protected float moveSpeed = 0.7F;
    protected int numTicksToChaseTarget = 0;
    float field17 = 0.0F;
    private int livingSoundTime;
    private Entity currentTarget;

    public EntityLiving(World world) {
        super(world);
        this.preventEntitySpawning = true;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.rotationYaw = (float) (Math.random() * Math.PI * 2.0D);
        this.stepHeight = 0.5F;
    }

    @Override
    protected void entityInit() {
    }

    public boolean canEntityBeSeen(Entity var1) {
        return this.world.rayTraceBlocks(new Vec3d(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ), new Vec3d(var1.posX, var1.posY + (double) var1.getEyeHeight(), var1.posZ)) == null;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public String getEntityTexture() {
        return this.texture;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public boolean canBePushed() {
        return !this.isDead;
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.85F;
    }

    public int getTalkInterval() {
        return 80;
    }

    public void playLivingSound() {
        String var1 = this.getLivingSound();
        if (var1 != null) {
            this.world.playSoundAtEntity(this, var1, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }

    }

    @Override
    public void onEntityUpdate() {
        this.prevSwingProgress = this.swingProgress;
        super.onEntityUpdate();
        if (this.rand.nextInt(1000) < this.livingSoundTime++) {
            this.livingSoundTime = -this.getTalkInterval();
            this.playLivingSound();
        }

        if (this.isEntityAlive() && this.isEntityInsideOpaqueBlock()) {
            this.attackEntityFrom(null, 1);
        }

        if (this.isImmuneToFire || this.world.localWorld) {
            this.fire = 0;
        }

        if (this.isEntityAlive() && this.isInsideOfMaterial(Material.WATER) && !this.canBreatheUnderwater()) {
            --this.air;
            if (this.air == -20) {
                this.air = 0;

                for (int var1 = 0; var1 < 8; ++var1) {
                    float var2 = this.rand.nextFloat() - this.rand.nextFloat();
                    float var3 = this.rand.nextFloat() - this.rand.nextFloat();
                    float var4 = this.rand.nextFloat() - this.rand.nextFloat();
                    this.world.spawnParticle("bubble", this.posX + (double) var2, this.posY + (double) var3, this.posZ + (double) var4, this.motionX, this.motionY, this.motionZ);
                }

                this.attackEntityFrom(null, 2);
            }

            this.fire = 0;
        } else {
            this.air = this.maxAir;
        }

        this.prevCameraPitch = this.cameraPitch;
        if (this.attackTime > 0) {
            --this.attackTime;
        }

        if (this.hurtTime > 0) {
            --this.hurtTime;
        }

        if (this.heartsLife > 0) {
            --this.heartsLife;
        }

        if (this.health <= 0) {
            ++this.deathTime;
            if (this.deathTime > 20) {
                this.onEntityDeath();
                this.setEntityDead();

                for (int var8 = 0; var8 < 20; ++var8) {
                    double var9 = this.rand.nextGaussian() * 0.02D;
                    double var10 = this.rand.nextGaussian() * 0.02D;
                    double var6 = this.rand.nextGaussian() * 0.02D;
                    this.world.spawnParticle("explode", this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, var9, var10, var6);
                }
            }
        }

        this.field11 = this.field10;
        this.prevRenderYawOffset = this.renderYawOffset;
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    public void spawnExplosionParticle() {
        for (int var1 = 0; var1 < 20; ++var1) {
            double var2 = this.rand.nextGaussian() * 0.02D;
            double var4 = this.rand.nextGaussian() * 0.02D;
            double var6 = this.rand.nextGaussian() * 0.02D;
            double var8 = 10.0D;
            this.world.spawnParticle("explode", this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width - var2 * var8, this.posY + (double) (this.rand.nextFloat() * this.height) - var4 * var8, this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width - var6 * var8, var2, var4, var6);
        }

    }

    @Override
    public void updateRidden() {
        super.updateRidden();
        this.field8 = this.field9;
        this.field9 = 0.0F;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9) {
        this.yOffset = 0.0F;
        this.newPosX = var1;
        this.newPosY = var3;
        this.newPosZ = var5;
        this.newRotationYaw = var7;
        this.newRotationPitch = var8;
        this.newPosRotationIncrements = var9;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.onLivingUpdate();
        double var1 = this.posX - this.prevPosX;
        double var3 = this.posZ - this.prevPosZ;
        float var5 = MathHelper.sqrt(var1 * var1 + var3 * var3);
        float var6 = this.renderYawOffset;
        float var7 = 0.0F;
        this.field8 = this.field9;
        float var8 = 0.0F;
        if (var5 > 0.05F) {
            var8 = 1.0F;
            var7 = var5 * 3.0F;
            var6 = (float) Math.atan2(var3, var1) * 180.0F / MathConstants.PI - 90.0F;
        }

        if (this.swingProgress > 0.0F) {
            var6 = this.rotationYaw;
        }

        if (!this.onGround) {
            var8 = 0.0F;
        }

        this.field9 += (var8 - this.field9) * 0.3F;

        float var9;
        var9 = var6 - this.renderYawOffset;
        while (var9 < -180.0F) {
            var9 += 360.0F;
        }

        while (var9 >= 180.0F) {
            var9 -= 360.0F;
        }

        this.renderYawOffset += var9 * 0.3F;

        float var10;
        var10 = this.rotationYaw - this.renderYawOffset;
        while (var10 < -180.0F) {
            var10 += 360.0F;
        }

        while (var10 >= 180.0F) {
            var10 -= 360.0F;
        }

        boolean var11 = var10 < -90.0F || var10 >= 90.0F;
        if (var10 < -75.0F) {
            var10 = -75.0F;
        }

        if (var10 >= 75.0F) {
            var10 = 75.0F;
        }

        this.renderYawOffset = this.rotationYaw - var10;
        if (var10 * var10 > 2500.0F) {
            this.renderYawOffset += var10 * 0.2F;
        }

        if (var11) {
            var7 *= -1.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        while (this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
            this.prevRenderYawOffset -= 360.0F;
        }

        while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
            this.prevRenderYawOffset += 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
            this.prevRotationPitch -= 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        this.field10 += var7;
    }

    @Override
    protected void setSize(float width, float height) {
        super.setSize(width, height);
    }

    public void heal(int var1) {
        if (this.health > 0) {
            this.health += var1;
            if (this.health > 20) {
                this.health = 20;
            }

            this.heartsLife = this.heartsHalvesLife / 2;
        }
    }

    @Override
    public boolean attackEntityFrom(Entity var1, int var2) {
        if (this.world.localWorld) {
            return false;
        }

        this.entityAge = 0;
        if (this.health <= 0) {
            return false;
        }

        this.field6 = 1.5F;
        boolean var3 = true;
        if ((float) this.heartsLife > (float) this.heartsHalvesLife / 2.0F) {
            if (var2 <= this.naturalArmorRating) {
                return false;
            }

            this.damageEntity(var2 - this.naturalArmorRating);
            this.naturalArmorRating = var2;
            var3 = false;
        } else {
            this.naturalArmorRating = var2;
            this.prevHealth = this.health;
            this.heartsLife = this.heartsHalvesLife;
            this.damageEntity(var2);
            this.hurtTime = this.maxHurtTime = 10;
        }

        this.attackedAtYaw = 0.0F;
        if (var3) {
            this.world.sendTrackedEntityStatusUpdatePacket(this, (byte) 2);
            this.setBeenAttacked();
            if (var1 != null) {
                double var4 = var1.posX - this.posX;

                double var6;
                for (var6 = var1.posZ - this.posZ; var4 * var4 + var6 * var6 < 1.0E-4D; var6 = (Math.random() - Math.random()) * 0.01D) {
                    var4 = (Math.random() - Math.random()) * 0.01D;
                }

                this.attackedAtYaw = (float) (Math.atan2(var6, var4) * 180.0D / Math.PI) - this.rotationYaw;
                this.knockBack(var1, var2, var4, var6);
            } else {
                this.attackedAtYaw = (float) ((int) (Math.random() * 2.0D) * 180);
            }
        }

        if (this.health <= 0) {
            if (var3) {
                this.world.playSoundAtEntity(this, this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }

            this.onDeath(var1);
        } else if (var3) {
            this.world.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }

        return true;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public void performHurtAnimation() {
        this.hurtTime = this.maxHurtTime = 10;
        this.attackedAtYaw = 0.0F;
    }

    protected void damageEntity(int var1) {
        this.health -= var1;
    }

    protected float getSoundVolume() {
        return 1.0F;
    }

    protected String getLivingSound() {
        return null;
    }

    protected String getHurtSound() {
        return "random.hurt";
    }

    protected String getDeathSound() {
        return "random.hurt";
    }

    public void knockBack(Entity var1, int var2, double var3, double var5) {
        float var7 = MathHelper.sqrt(var3 * var3 + var5 * var5);
        float var8 = 0.4F;
        this.motionX /= 2.0D;
        this.motionY /= 2.0D;
        this.motionZ /= 2.0D;
        this.motionX -= var3 / (double) var7 * (double) var8;
        this.motionY += 0.4000000059604645D;
        this.motionZ -= var5 / (double) var7 * (double) var8;
        if (this.motionY > 0.4000000059604645D) {
            this.motionY = 0.4000000059604645D;
        }

    }

    public void onDeath(Entity var1) {
        if (this.scoreValue >= 0 && var1 != null) {
            var1.addToPlayerScore(this, this.scoreValue);
        }

        if (var1 != null) {
            var1.onKillEntity(this);
        }

        this.unused_flag = true;
        if (!this.world.localWorld) {
            this.dropFewItems();
        }

        this.world.sendTrackedEntityStatusUpdatePacket(this, (byte) 3);
    }

    protected void dropFewItems() {
        int var1 = this.getDropItemId();
        if (var1 > 0) {
            int var2 = this.rand.nextInt(3);

            for (int var3 = 0; var3 < var2; ++var3) {
                this.dropItem(var1, 1);
            }
        }

    }

    protected int getDropItemId() {
        return 0;
    }

    @Override
    protected void fall(float var1) {
        super.fall(var1);
        int var2 = (int) Math.ceil(var1 - 3.0F);
        if (var2 > 0) {
            this.attackEntityFrom(null, var2);
            int var3 = this.world.getBlockId(MathHelper.floor(this.posX), MathHelper.floor(this.posY - 0.20000000298023224D - (double) this.yOffset), MathHelper.floor(this.posZ));
            if (var3 > 0) {
                StepSound var4 = Block.BLOCKS_LIST[var3].stepSound;
                this.world.playSoundAtEntity(this, var4.getFormattedName(), var4.getVolume() * 0.5F, var4.getPitch() * 0.75F);
            }
        }

    }

    public void moveEntityWithHeading(float var1, float var2) {
        if (this.isInWater()) {
            double var3 = this.posY;
            this.moveFlying(var1, var2, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
            this.motionY -= 0.02D;
            if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + var3, this.motionZ)) {
                this.motionY = 0.30000001192092896D;
            }
        } else if (this.handleLavaMovement()) {
            double var8 = this.posY;
            this.moveFlying(var1, var2, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
            this.motionY -= 0.02D;
            if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + var8, this.motionZ)) {
                this.motionY = 0.30000001192092896D;
            }
        } else {
            float var9 = 0.91F;
            if (this.onGround) {
                var9 = 0.54600006F;
                int var4 = this.world.getBlockId(MathHelper.floor(this.posX), MathHelper.floor(this.boundingBox.minY) - 1, MathHelper.floor(this.posZ));
                if (var4 > 0) {
                    var9 = Block.BLOCKS_LIST[var4].slipperiness * 0.91F;
                }
            }

            float var12 = 0.16277136F / (var9 * var9 * var9);
            this.moveFlying(var1, var2, this.onGround ? 0.1F * var12 : 0.02F);
            var9 = 0.91F;
            if (this.onGround) {
                var9 = 0.54600006F;
                int var5 = this.world.getBlockId(MathHelper.floor(this.posX), MathHelper.floor(this.boundingBox.minY) - 1, MathHelper.floor(this.posZ));
                if (var5 > 0) {
                    var9 = Block.BLOCKS_LIST[var5].slipperiness * 0.91F;
                }
            }

            if (this.isOnLadder()) {
                float var13 = 0.15F;
                if (this.motionX < (double) (-var13)) {
                    this.motionX = -var13;
                }

                if (this.motionX > (double) var13) {
                    this.motionX = var13;
                }

                if (this.motionZ < (double) (-var13)) {
                    this.motionZ = -var13;
                }

                if (this.motionZ > (double) var13) {
                    this.motionZ = var13;
                }

                this.fallDistance = 0.0F;
                if (this.motionY < -0.15D) {
                    this.motionY = -0.15D;
                }

                if (this.isSneaking() && this.motionY < 0.0D) {
                    this.motionY = 0.0D;
                }
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            if (this.isCollidedHorizontally && this.isOnLadder()) {
                this.motionY = 0.2D;
            }

            this.motionY -= 0.08D;
            this.motionY *= 0.9800000190734863D;
            this.motionX *= var9;
            this.motionZ *= var9;
        }

        this.field5 = this.field6;
        double var11 = this.posX - this.prevPosX;
        double var14 = this.posZ - this.prevPosZ;
        float var7 = MathHelper.sqrt(var11 * var11 + var14 * var14) * 4.0F;
        if (var7 > 1.0F) {
            var7 = 1.0F;
        }

        this.field6 += (var7 - this.field6) * 0.4F;
        this.field7 += this.field6;
    }

    public boolean isOnLadder() {
        int var1 = MathHelper.floor(this.posX);
        int var2 = MathHelper.floor(this.boundingBox.minY);
        int var3 = MathHelper.floor(this.posZ);
        return this.world.getBlockId(var1, var2, var3) == Block.LADDER.blockID;
    }

    @Override
    public void writeEntityToNBT(TagCompound var1) {
        var1.setShort("Health", (short) this.health);
        var1.setShort("HurtTime", (short) this.hurtTime);
        var1.setShort("DeathTime", (short) this.deathTime);
        var1.setShort("AttackTime", (short) this.attackTime);
    }

    @Override
    public void readEntityFromNBT(TagCompound var1) {
        this.health = var1.getShort("Health");
        if (!var1.hasKey("Health")) {
            this.health = 10;
        }

        this.hurtTime = var1.getShort("HurtTime");
        this.deathTime = var1.getShort("DeathTime");
        this.attackTime = var1.getShort("AttackTime");
    }

    @Override
    public boolean isEntityAlive() {
        return !this.isDead && this.health > 0;
    }

    public boolean canBreatheUnderwater() {
        return false;
    }

    public void onLivingUpdate() {
        if (this.newPosRotationIncrements > 0) {
            double var1 = this.posX + (this.newPosX - this.posX) / (double) this.newPosRotationIncrements;
            double var3 = this.posY + (this.newPosY - this.posY) / (double) this.newPosRotationIncrements;
            double var5 = this.posZ + (this.newPosZ - this.posZ) / (double) this.newPosRotationIncrements;

            double var7 = this.newRotationYaw - (double) this.rotationYaw;
            while (var7 < -180.0D) {
                var7 += 360.0D;
            }

            while (var7 >= 180.0D) {
                var7 -= 360.0D;
            }

            this.rotationYaw = (float) ((double) this.rotationYaw + var7 / (double) this.newPosRotationIncrements);
            this.rotationPitch = (float) ((double) this.rotationPitch + (this.newRotationPitch - (double) this.rotationPitch) / (double) this.newPosRotationIncrements);
            --this.newPosRotationIncrements;
            this.setPosition(var1, var3, var5);
            this.setRotation(this.rotationYaw, this.rotationPitch);
            List<AxisAlignedBB> var9 = this.world.getCollidingBoundingBoxes(this, this.boundingBox.getInsetBoundingBox(0.03125D, 0.0D, 0.03125D));
            if (var9.size() > 0) {
                double var10 = 0.0D;

                for (int var12 = 0; var12 < var9.size(); ++var12) {
                    AxisAlignedBB var13 = var9.get(var12);
                    if (var13.maxY > var10) {
                        var10 = var13.maxY;
                    }
                }

                var3 = var3 + (var10 - this.boundingBox.minY);
                this.setPosition(var1, var3, var5);
            }
        }

        if (this.isMovementBlocked()) {
            this.isJumping = false;
            this.moveStrafing = 0.0F;
            this.moveForward = 0.0F;
            this.randomYawVelocity = 0.0F;
        } else if (!this.isMultiplayerEntity) {
            this.updatePlayerActionState();
        }

        boolean var14 = this.isInWater();
        boolean var2 = this.handleLavaMovement();
        if (this.isJumping) {
            if (var14) {
                this.motionY += 0.03999999910593033D;
            } else if (var2) {
                this.motionY += 0.03999999910593033D;
            } else if (this.onGround) {
                this.jump();
            }
        }

        this.moveStrafing *= 0.98F;
        this.moveForward *= 0.98F;
        this.randomYawVelocity *= 0.9F;
        this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
        List<Entity> var16 = this.world.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        if (var16 != null && var16.size() > 0) {
            for (int var4 = 0; var4 < var16.size(); ++var4) {
                Entity var17 = var16.get(var4);
                if (var17.canBePushed()) {
                    var17.applyEntityCollision(this);
                }
            }
        }

    }

    protected boolean isMovementBlocked() {
        return this.health <= 0;
    }

    protected void jump() {
        this.motionY = 0.41999998688697815D;
    }

    protected boolean canDespawn() {
        return true;
    }

    protected void tryDespawn() {
        EntityPlayer var1 = this.world.getClosestPlayerToEntity(this, -1.0D);
        if (this.canDespawn() && var1 != null) {
            double var2 = var1.posX - this.posX;
            double var4 = var1.posY - this.posY;
            double var6 = var1.posZ - this.posZ;
            double var8 = var2 * var2 + var4 * var4 + var6 * var6;
            if (var8 > 16384.0D) {
                this.setEntityDead();
            }

            if (this.entityAge > 600 && this.rand.nextInt(800) == 0) {
                if (var8 < 1024.0D) {
                    this.entityAge = 0;
                } else {
                    this.setEntityDead();
                }
            }
        }

    }

    protected void updatePlayerActionState() {
        ++this.entityAge;
        EntityPlayer var1 = this.world.getClosestPlayerToEntity(this, -1.0D);
        this.tryDespawn();
        this.moveStrafing = 0.0F;
        this.moveForward = 0.0F;
        float var2 = 8.0F;
        if (this.rand.nextFloat() < 0.02F) {
            var1 = this.world.getClosestPlayerToEntity(this, var2);
            if (var1 != null) {
                this.currentTarget = var1;
                this.numTicksToChaseTarget = 10 + this.rand.nextInt(20);
            } else {
                this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
            }
        }

        if (this.currentTarget != null) {
            this.faceEntity(this.currentTarget, 10.0F, (float) this.method1());
            if (this.numTicksToChaseTarget-- <= 0 || this.currentTarget.isDead || this.currentTarget.getDistanceSqToEntity(this) > (double) (var2 * var2)) {
                this.currentTarget = null;
            }
        } else {
            if (this.rand.nextFloat() < 0.05F) {
                this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
            }

            this.rotationYaw += this.randomYawVelocity;
            this.rotationPitch = this.defaultPitch;
        }

        boolean var3 = this.isInWater();
        boolean var4 = this.handleLavaMovement();
        if (var3 || var4) {
            this.isJumping = this.rand.nextFloat() < 0.8F;
        }

    }

    protected int method1() {
        return 40;
    }

    public void faceEntity(Entity var1, float var2, float var3) {
        double var4 = var1.posX - this.posX;
        double var8 = var1.posZ - this.posZ;
        double var6;
        if (var1 instanceof EntityLiving var10) {
            var6 = this.posY + (double) this.getEyeHeight() - (var10.posY + (double) var10.getEyeHeight());
        } else {
            var6 = (var1.boundingBox.minY + var1.boundingBox.maxY) / 2.0D - (this.posY + (double) this.getEyeHeight());
        }

        double var14 = MathHelper.sqrt(var4 * var4 + var8 * var8);
        float var12 = (float) (Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
        float var13 = (float) (-(Math.atan2(var6, var14) * 180.0D / Math.PI));
        this.rotationPitch = -this.updateRotation(this.rotationPitch, var13, var3);
        this.rotationYaw = this.updateRotation(this.rotationYaw, var12, var2);
    }

    public boolean hasCurrentTarget() {
        return this.currentTarget != null;
    }

    public Entity getCurrentTarget() {
        return this.currentTarget;
    }

    private float updateRotation(float var1, float var2, float var3) {
        float var4 = var2 - var1;
        while (var4 < -180.0F) {
            var4 += 360.0F;
        }

        while (var4 >= 180.0F) {
            var4 -= 360.0F;
        }

        if (var4 > var3) {
            var4 = var3;
        }

        if (var4 < -var3) {
            var4 = -var3;
        }

        return var1 + var4;
    }

    public void onEntityDeath() {
    }

    public boolean getCanSpawnHere() {
        return this.world.checkIfAABBIsClear(this.boundingBox) && this.world.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.world.containsLiquid(this.boundingBox);
    }

    @Override
    protected void kill() {
        this.attackEntityFrom(null, 4);
    }

    @Side(CodeSide.CLIENT)
    public float getSwingProgress(float delta) {
        float var2 = this.swingProgress - this.prevSwingProgress;
        if (var2 < 0.0F) {
            ++var2;
        }

        return this.prevSwingProgress + var2 * delta;
    }

    @Side(CodeSide.CLIENT)
    public Vec3d getPosition(float delta) {
        if (delta == 1.0F) {
            return new Vec3d(this.posX, this.posY, this.posZ);
        } else {
            double var2 = this.prevPosX + (this.posX - this.prevPosX) * (double) delta;
            double var4 = this.prevPosY + (this.posY - this.prevPosY) * (double) delta;
            double var6 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double) delta;
            return new Vec3d(var2, var4, var6);
        }
    }

    @Override
    public Vec3d getLookVec() {
        return this.getLook(1.0F);
    }

    public Vec3d getLook(float var1) {
        if (var1 == 1.0F) {
            float var8 = MathHelper.cos(-this.rotationYaw * MathConstants.RADIANS_PER_DEGREE - MathConstants.PI);
            float var9 = MathHelper.sin(-this.rotationYaw * MathConstants.RADIANS_PER_DEGREE - MathConstants.PI);
            float var10 = -MathHelper.cos(-this.rotationPitch * MathConstants.RADIANS_PER_DEGREE);
            float var11 = MathHelper.sin(-this.rotationPitch * MathConstants.RADIANS_PER_DEGREE);
            return new Vec3d(var9 * var10, var11, var8 * var10);
        } else {
            float var2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * var1;
            float var3 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * var1;
            float var4 = MathHelper.cos(-var3 * MathConstants.RADIANS_PER_DEGREE - MathConstants.PI);
            float var5 = MathHelper.sin(-var3 * MathConstants.RADIANS_PER_DEGREE - MathConstants.PI);
            float var6 = -MathHelper.cos(-var2 * MathConstants.RADIANS_PER_DEGREE);
            float var7 = MathHelper.sin(-var2 * MathConstants.RADIANS_PER_DEGREE);
            return new Vec3d(var5 * var6, var7, var4 * var6);
        }
    }

    public MovingObjectPosition rayTrace(double var1, float var3) {
        Vec3d var4 = this.getPosition(var3);
        Vec3d var5 = this.getLook(var3);
        Vec3d var6 = new Vec3d(var4).add(var5.x * var1, var5.y * var1, var5.z * var1);
        return this.world.rayTraceBlocks(var4, var6);
    }

    public int getMaxSpawnedInChunk() {
        return 4;
    }

    @Side(CodeSide.CLIENT)
    public ItemStack getHeldItem() {
        return null;
    }

    @Override
    public void handleHealthUpdate(byte var1) {
        if (var1 == 2) {
            this.field6 = 1.5F;
            this.heartsLife = this.heartsHalvesLife;
            this.hurtTime = this.maxHurtTime = 10;
            this.attackedAtYaw = 0.0F;
            this.world.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.attackEntityFrom(null, 0);
        } else if (var1 == 3) {
            this.world.playSoundAtEntity(this, this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.health = 0;
            this.onDeath(null);
        } else {
            super.handleHealthUpdate(var1);
        }

    }

    public boolean isSleeping() {
        return false;
    }

    @Side(CodeSide.CLIENT)
    public int getItemIcon(ItemStack var1) {
        return var1.getIconIndex();
    }
}
