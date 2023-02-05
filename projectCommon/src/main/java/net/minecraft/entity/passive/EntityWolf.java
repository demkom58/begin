package net.minecraft.entity.passive;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.hypnosis.util.math.MathConstants;
import net.hypnosis.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAnimal;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.PathEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.TagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class EntityWolf extends EntityAnimal {
    private boolean looksWithInterest = false;
    private float field1;
    private float field2;
    private boolean isWolfShaking;
    private boolean field3;
    private float timeShaking;
    private float prevTimeShaking;

    public EntityWolf(World var1) {
        super(var1);
        this.texture = "/mob/wolf.png";
        this.setSize(0.8F, 0.8F);
        this.moveSpeed = 1.1F;
        this.health = 8;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte) 0);
        this.dataWatcher.addObject(17, "");
        this.dataWatcher.addObject(18, this.health);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public String getEntityTexture() {
        if (this.isWolfTamed()) {
            return "/mob/wolf_tame.png";
        } else {
            return this.isAngry() ? "/mob/wolf_angry.png" : super.getEntityTexture();
        }
    }

    @Override
    public void writeEntityToNBT(TagCompound var1) {
        super.writeEntityToNBT(var1);
        var1.setBoolean("Angry", this.isAngry());
        var1.setBoolean("Sitting", this.isSitting());
        if (this.getOwner() == null) {
            var1.setString("Owner", "");
        } else {
            var1.setString("Owner", this.getOwner());
        }

    }

    @Override
    public void readEntityFromNBT(TagCompound var1) {
        super.readEntityFromNBT(var1);
        this.setAngry(var1.getBoolean("Angry"));
        this.setSitting(var1.getBoolean("Sitting"));
        String var2 = var1.getString("Owner");
        if (var2.length() > 0) {
            this.setOwner(var2);
            this.setWolfTamed(true);
        }

    }

    @Override
    protected boolean canDespawn() {
        return !this.isWolfTamed();
    }

    @Override
    protected String getLivingSound() {
        if (this.isAngry()) {
            return "mob.wolf.growl";
        } else if (this.rand.nextInt(3) == 0) {
            return this.isWolfTamed() && this.dataWatcher.getWatchableObjectInteger(18) < 10 ? "mob.wolf.whine" : "mob.wolf.panting";
        } else {
            return "mob.wolf.bark";
        }
    }

    @Override
    protected String getHurtSound() {
        return "mob.wolf.hurt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.wolf.death";
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected int getDropItemId() {
        return -1;
    }

    @Override
    protected void updatePlayerActionState() {
        super.updatePlayerActionState();
        if (!this.hasAttacked && !this.hasPath() && this.isWolfTamed() && this.ridingEntity == null) {
            EntityPlayer var3 = this.world.getPlayerEntityByName(this.getOwner());
            if (var3 != null) {
                float var2 = var3.getDistanceToEntity(this);
                if (var2 > 5.0F) {
                    this.getPathOrWalkableBlock(var3, var2);
                }
            } else if (!this.isInWater()) {
                this.setSitting(true);
            }
        } else if (this.playerToAttack == null && !this.hasPath() && !this.isWolfTamed() && this.world.rand.nextInt(100) == 0) {
            List<Entity> var1 = this.world.getEntitiesWithinAABB(EntitySheep.class, AxisAlignedBB.getBoundingBoxFromPool(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D).expand(16.0D, 4.0D, 16.0D));
            if (!var1.isEmpty()) {
                this.setTarget(var1.get(this.world.rand.nextInt(var1.size())));
            }
        }

        if (this.isInWater()) {
            this.setSitting(false);
        }

        if (!this.world.localWorld) {
            this.dataWatcher.updateObject(18, this.health);
        }

    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.looksWithInterest = false;
        if (this.hasCurrentTarget() && !this.hasPath() && !this.isAngry()) {
            Entity var1 = this.getCurrentTarget();
            if (var1 instanceof EntityPlayer var2) {
                ItemStack var3 = var2.inventory.getCurrentItem();
                if (var3 != null) {
                    if (!this.isWolfTamed() && var3.itemID == Item.BONE.shiftedIndex) {
                        this.looksWithInterest = true;
                    } else if (this.isWolfTamed() && Item.ITEMS_LIST[var3.itemID] instanceof ItemFood) {
                        this.looksWithInterest = ((ItemFood) Item.ITEMS_LIST[var3.itemID]).getIsWolfsFavoriteMeat();
                    }
                }
            }
        }

        if (!this.isMultiplayerEntity && this.isWolfShaking && !this.field3 && !this.hasPath() && this.onGround) {
            this.field3 = true;
            this.timeShaking = 0.0F;
            this.prevTimeShaking = 0.0F;
            this.world.sendTrackedEntityStatusUpdatePacket(this, (byte) 8);
        }

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.field2 = this.field1;
        if (this.looksWithInterest) {
            this.field1 += (1.0F - this.field1) * 0.4F;
        } else {
            this.field1 += (0.0F - this.field1) * 0.4F;
        }

        if (this.looksWithInterest) {
            this.numTicksToChaseTarget = 10;
        }

        if (this.isWet()) {
            this.isWolfShaking = true;
            this.field3 = false;
            this.timeShaking = 0.0F;
            this.prevTimeShaking = 0.0F;
        } else if ((this.isWolfShaking || this.field3) && this.field3) {
            if (this.timeShaking == 0.0F) {
                this.world.playSoundAtEntity(this, "mob.wolf.shake", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }

            this.prevTimeShaking = this.timeShaking;
            this.timeShaking += 0.05F;
            if (this.prevTimeShaking >= 2.0F) {
                this.isWolfShaking = false;
                this.field3 = false;
                this.prevTimeShaking = 0.0F;
                this.timeShaking = 0.0F;
            }

            if (this.timeShaking > 0.4F) {
                float var1 = (float) this.boundingBox.minY;
                int var2 = (int) (MathHelper.sin((this.timeShaking - 0.4F) * MathConstants.PI) * 7.0F);

                for (int var3 = 0; var3 < var2; ++var3) {
                    float var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    this.world.spawnParticle("splash", this.posX + (double) var4, var1 + 0.8F, this.posZ + (double) var5, this.motionX, this.motionY, this.motionZ);
                }
            }
        }

    }

    public boolean getWolfShaking() {
        return this.isWolfShaking;
    }

    public float getShadingWhileShaking(float var1) {
        return 0.75F + (this.prevTimeShaking + (this.timeShaking - this.prevTimeShaking) * var1) / 2.0F * 0.25F;
    }

    public float getShakeAngle(float var1, float var2) {
        float var3 = (this.prevTimeShaking + (this.timeShaking - this.prevTimeShaking) * var1 + var2) / 1.8F;
        if (var3 < 0.0F) {
            var3 = 0.0F;
        } else if (var3 > 1.0F) {
            var3 = 1.0F;
        }

        return MathHelper.sin(var3 * MathConstants.PI) * MathHelper.sin(var3 * MathConstants.PI * 11.0F) * 0.15F * MathConstants.PI;
    }

    public float getInterestedAngle(float var1) {
        return (this.field2 + (this.field1 - this.field2) * var1) * 0.15F * MathConstants.PI;
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.8F;
    }

    @Override
    protected int method1() {
        return this.isSitting() ? 20 : super.method1();
    }

    private void getPathOrWalkableBlock(Entity var1, float var2) {
        PathEntity var3 = this.world.getPathToEntity(this, var1, 16.0F);
        if (var3 == null && var2 > 12.0F) {
            int var4 = MathHelper.floor(var1.posX) - 2;
            int var5 = MathHelper.floor(var1.posZ) - 2;
            int var6 = MathHelper.floor(var1.boundingBox.minY);

            for (int var7 = 0; var7 <= 4; ++var7) {
                for (int var8 = 0; var8 <= 4; ++var8) {
                    if ((var7 < 1 || var8 < 1 || var7 > 3 || var8 > 3) && this.world.isBlockNormalCube(var4 + var7, var6 - 1, var5 + var8) && !this.world.isBlockNormalCube(var4 + var7, var6, var5 + var8) && !this.world.isBlockNormalCube(var4 + var7, var6 + 1, var5 + var8)) {
                        this.setLocationAndAngles((float) (var4 + var7) + 0.5F, var6, (float) (var5 + var8) + 0.5F, this.rotationYaw, this.rotationPitch);
                        return;
                    }
                }
            }
        } else {
            this.setPathToEntity(var3);
        }

    }

    @Override
    protected boolean isMovementCeased() {
        return this.isSitting() || this.field3;
    }

    @Override
    public boolean attackEntityFrom(Entity var1, int var2) {
        this.setSitting(false);
        if (var1 != null && !(var1 instanceof EntityPlayer) && !(var1 instanceof EntityArrow)) {
            var2 = (var2 + 1) / 2;
        }

        if (!super.attackEntityFrom(var1, var2)) {
            return false;
        }

        if (!this.isWolfTamed() && !this.isAngry()) {
            if (var1 instanceof EntityPlayer) {
                this.setAngry(true);
                this.playerToAttack = var1;
            }

            if (var1 instanceof EntityArrow && ((EntityArrow) var1).owner != null) {
                var1 = ((EntityArrow) var1).owner;
            }

            if (var1 instanceof EntityLiving) {
                for (Entity var5 : this.world.getEntitiesWithinAABB(EntityWolf.class, AxisAlignedBB.getBoundingBoxFromPool(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D).expand(16.0D, 4.0D, 16.0D))) {
                    EntityWolf var6 = (EntityWolf) var5;
                    if (!var6.isWolfTamed() && var6.playerToAttack == null) {
                        var6.playerToAttack = var1;
                        if (var1 instanceof EntityPlayer) {
                            var6.setAngry(true);
                        }
                    }
                }
            }
        } else if (var1 != this && var1 != null) {
            if (this.isWolfTamed() && var1 instanceof EntityPlayer && ((EntityPlayer) var1).username.equalsIgnoreCase(this.getOwner())) {
                return true;
            }

            this.playerToAttack = var1;
        }

        return true;
    }

    @Override
    protected Entity findPlayerToAttack() {
        return this.isAngry() ? this.world.getClosestPlayerToEntity(this, 16.0D) : null;
    }

    @Override
    protected void attackEntity(Entity var1, float var2) {
        if (var2 > 2.0F && var2 < 6.0F && this.rand.nextInt(10) == 0) {
            if (this.onGround) {
                double var8 = var1.posX - this.posX;
                double var5 = var1.posZ - this.posZ;
                float var7 = MathHelper.sqrt(var8 * var8 + var5 * var5);
                this.motionX = var8 / (double) var7 * 0.5D * 0.800000011920929D + this.motionX * 0.20000000298023224D;
                this.motionZ = var5 / (double) var7 * 0.5D * 0.800000011920929D + this.motionZ * 0.20000000298023224D;
                this.motionY = 0.4000000059604645D;
            }
        } else if ((double) var2 < 1.5D && var1.boundingBox.maxY > this.boundingBox.minY && var1.boundingBox.minY < this.boundingBox.maxY) {
            this.attackTime = 20;
            byte var3 = 2;
            if (this.isWolfTamed()) {
                var3 = 4;
            }

            var1.attackEntityFrom(this, var3);
        }

    }

    @Override
    public boolean interact(EntityPlayer var1) {
        ItemStack var2 = var1.inventory.getCurrentItem();
        if (!this.isWolfTamed()) {
            if (var2 != null && var2.itemID == Item.BONE.shiftedIndex && !this.isAngry()) {
                --var2.stackSize;
                if (var2.stackSize <= 0) {
                    var1.inventory.setInventorySlotContents(var1.inventory.currentItem, null);
                }

                if (!this.world.localWorld) {
                    if (this.rand.nextInt(3) == 0) {
                        this.setWolfTamed(true);
                        this.setPathToEntity(null);
                        this.setSitting(true);
                        this.health = 20;
                        this.setOwner(var1.username);
                        this.playTameEffect(true);
                        this.world.sendTrackedEntityStatusUpdatePacket(this, (byte) 7);
                    } else {
                        this.playTameEffect(false);
                        this.world.sendTrackedEntityStatusUpdatePacket(this, (byte) 6);
                    }
                }

                return true;
            }
        } else {
            if (var2 != null && Item.ITEMS_LIST[var2.itemID] instanceof ItemFood var3) {
                if (var3.getIsWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectInteger(18) < 20) {
                    --var2.stackSize;
                    if (var2.stackSize <= 0) {
                        var1.inventory.setInventorySlotContents(var1.inventory.currentItem, null);
                    }

                    this.heal(((ItemFood) Item.PORKCHOP_RAW).getHealAmount());
                    return true;
                }
            }

            if (var1.username.equalsIgnoreCase(this.getOwner())) {
                if (!this.world.localWorld) {
                    this.setSitting(!this.isSitting());
                    this.isJumping = false;
                    this.setPathToEntity(null);
                }

                return true;
            }
        }

        return false;
    }

    void playTameEffect(boolean success) {
        String var2 = "heart";
        if (!success) {
            var2 = "smoke";
        }

        for (int var3 = 0; var3 < 7; ++var3) {
            double var4 = this.rand.nextGaussian() * 0.02D;
            double var6 = this.rand.nextGaussian() * 0.02D;
            double var8 = this.rand.nextGaussian() * 0.02D;
            this.world.spawnParticle(var2, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, var4, var6, var8);
        }

    }

    @Override
    @Side(CodeSide.CLIENT)
    public void handleHealthUpdate(byte var1) {
        if (var1 == 7) {
            this.playTameEffect(true);
        } else if (var1 == 6) {
            this.playTameEffect(false);
        } else if (var1 == 8) {
            this.field3 = true;
            this.timeShaking = 0.0F;
            this.prevTimeShaking = 0.0F;
        } else {
            super.handleHealthUpdate(var1);
        }

    }

    @Side(CodeSide.CLIENT)
    public float setTailRotation() {
        if (this.isAngry()) {
            return 1.5393804F;
        } else {
            return this.isWolfTamed() ? (0.55F - (float) (20 - this.dataWatcher.getWatchableObjectInteger(18)) * 0.02F) * MathConstants.PI : 0.62831855F;
        }
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 8;
    }

    public String getOwner() {
        return this.dataWatcher.getWatchableObjectString(17);
    }

    public void setOwner(String var1) {
        this.dataWatcher.updateObject(17, var1);
    }

    public boolean isSitting() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setSitting(boolean var1) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        if (var1) {
            this.dataWatcher.updateObject(16, (byte) (var2 | 1));
        } else {
            this.dataWatcher.updateObject(16, (byte) (var2 & -2));
        }

    }

    public boolean isAngry() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    public void setAngry(boolean var1) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        if (var1) {
            this.dataWatcher.updateObject(16, (byte) (var2 | 2));
        } else {
            this.dataWatcher.updateObject(16, (byte) (var2 & -3));
        }

    }

    public boolean isWolfTamed() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 4) != 0;
    }

    public void setWolfTamed(boolean var1) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        if (var1) {
            this.dataWatcher.updateObject(16, (byte) (var2 | 4));
        } else {
            this.dataWatcher.updateObject(16, (byte) (var2 & -5));
        }

    }
}
