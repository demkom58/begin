package net.potion.entity.passive;

import net.hypnosis.util.math.MathConstants;
import net.potion.entity.EntityWaterMob;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.Item;
import net.potion.item.ItemStack;
import net.potion.material.Material;
import net.potion.nbt.TagCompound;
import net.hypnosis.util.math.MathHelper;
import net.potion.world.World;

public class EntitySquid extends EntityWaterMob {
    public float field1 = 0.0F;
    public float field2 = 0.0F;
    public float field3 = 0.0F;
    public float field4 = 0.0F;
    public float field5 = 0.0F;
    public float field6 = 0.0F;
    public float field7 = 0.0F;
    public float field8 = 0.0F;
    private float randomMotionSpeed = 0.0F;
    private float field9 = 0.0F;
    private float field10 = 0.0F;
    private float randomMotionVecX = 0.0F;
    private float randomMotionVecY = 0.0F;
    private float randomMotionVecZ = 0.0F;

    public EntitySquid(World var1) {
        super(var1);
        this.texture = "/mob/squid.png";
        this.setSize(0.95F, 0.95F);
        this.field9 = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
    }

    @Override
    public void writeEntityToNBT(TagCompound var1) {
        super.writeEntityToNBT(var1);
    }

    @Override
    public void readEntityFromNBT(TagCompound var1) {
        super.readEntityFromNBT(var1);
    }

    @Override
    protected String getLivingSound() {
        return null;
    }

    @Override
    protected String getHurtSound() {
        return null;
    }

    @Override
    protected String getDeathSound() {
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected int getDropItemId() {
        return 0;
    }

    @Override
    protected void dropFewItems() {
        int var1 = this.rand.nextInt(3) + 1;

        for (int var2 = 0; var2 < var1; ++var2) {
            this.entityDropItem(new ItemStack(Item.DYE_POWDER, 1, 0), 0.0F);
        }

    }

    @Override
    public boolean interact(EntityPlayer var1) {
        return false;
    }

    @Override
    public boolean isInWater() {
        return this.world.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.6000000238418579D, 0.0D), Material.WATER, this);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.field2 = this.field1;
        this.field4 = this.field3;
        this.field6 = this.field5;
        this.field8 = this.field7;
        this.field5 += this.field9;
        if (this.field5 > 6.2831855F) {
            this.field5 -= 6.2831855F;
            if (this.rand.nextInt(10) == 0) {
                this.field9 = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
            }
        }

        if (this.isInWater()) {
            if (this.field5 < MathConstants.PI) {
                float var1 = this.field5 / MathConstants.PI;
                this.field7 = MathHelper.sin(var1 * var1 * MathConstants.PI) * MathConstants.PI * 0.25F;
                if ((double) var1 > 0.75D) {
                    this.randomMotionSpeed = 1.0F;
                    this.field10 = 1.0F;
                } else {
                    this.field10 *= 0.8F;
                }
            } else {
                this.field7 = 0.0F;
                this.randomMotionSpeed *= 0.9F;
                this.field10 *= 0.99F;
            }

            if (!this.isMultiplayerEntity) {
                this.motionX = this.randomMotionVecX * this.randomMotionSpeed;
                this.motionY = this.randomMotionVecY * this.randomMotionSpeed;
                this.motionZ = this.randomMotionVecZ * this.randomMotionSpeed;
            }

            float var2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.renderYawOffset += (-((float) Math.atan2(this.motionX, this.motionZ)) * 180.0F / MathConstants.PI - this.renderYawOffset) * 0.1F;
            this.rotationYaw = this.renderYawOffset;
            this.field3 += MathConstants.PI * this.field10 * 1.5F;
            this.field1 += (-((float) Math.atan2(var2, this.motionY)) * 180.0F / MathConstants.PI - this.field1) * 0.1F;
        } else {
            this.field7 = MathHelper.abs(MathHelper.sin(this.field5)) * MathConstants.PI * 0.25F;
            if (!this.isMultiplayerEntity) {
                this.motionX = 0.0D;
                this.motionY -= 0.08D;
                this.motionY *= 0.9800000190734863D;
                this.motionZ = 0.0D;
            }

            this.field1 = (float) ((double) this.field1 + (double) (-90.0F - this.field1) * 0.02D);
        }

    }

    @Override
    public void moveEntityWithHeading(float var1, float var2) {
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    @Override
    protected void updatePlayerActionState() {
        if (this.rand.nextInt(50) == 0 || !this.inWater || this.randomMotionVecX == 0.0F && this.randomMotionVecY == 0.0F && this.randomMotionVecZ == 0.0F) {
            float var1 = this.rand.nextFloat() * MathConstants.PI * 2.0F;
            this.randomMotionVecX = MathHelper.cos(var1) * 0.2F;
            this.randomMotionVecY = -0.1F + this.rand.nextFloat() * 0.2F;
            this.randomMotionVecZ = MathHelper.sin(var1) * 0.2F;
        }

        this.tryDespawn();
    }
}
