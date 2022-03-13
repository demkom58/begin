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
    public float field_21063_a = 0.0F;
    public float field_21062_b = 0.0F;
    public float field_21061_c = 0.0F;
    public float field_21059_f = 0.0F;
    public float field_21060_ak = 0.0F;
    public float field_21058_al = 0.0F;
    public float field_21057_am = 0.0F;
    public float field_21056_an = 0.0F;
    private float field_21055_ao = 0.0F;
    private float field_21054_ap = 0.0F;
    private float field_21053_aq = 0.0F;
    private float field_21052_ar = 0.0F;
    private float field_21051_as = 0.0F;
    private float field_21050_at = 0.0F;

    public EntitySquid(World var1) {
        super(var1);
        this.texture = "/mob/squid.png";
        this.setSize(0.95F, 0.95F);
        this.field_21054_ap = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
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
        return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.6000000238418579D, 0.0D), Material.WATER, this);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.field_21062_b = this.field_21063_a;
        this.field_21059_f = this.field_21061_c;
        this.field_21058_al = this.field_21060_ak;
        this.field_21056_an = this.field_21057_am;
        this.field_21060_ak += this.field_21054_ap;
        if (this.field_21060_ak > 6.2831855F) {
            this.field_21060_ak -= 6.2831855F;
            if (this.rand.nextInt(10) == 0) {
                this.field_21054_ap = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
            }
        }

        if (this.isInWater()) {
            if (this.field_21060_ak < MathConstants.PI) {
                float var1 = this.field_21060_ak / MathConstants.PI;
                this.field_21057_am = MathHelper.sin(var1 * var1 * MathConstants.PI) * MathConstants.PI * 0.25F;
                if ((double) var1 > 0.75D) {
                    this.field_21055_ao = 1.0F;
                    this.field_21053_aq = 1.0F;
                } else {
                    this.field_21053_aq *= 0.8F;
                }
            } else {
                this.field_21057_am = 0.0F;
                this.field_21055_ao *= 0.9F;
                this.field_21053_aq *= 0.99F;
            }

            if (!this.isMultiplayerEntity) {
                this.motionX = this.field_21052_ar * this.field_21055_ao;
                this.motionY = this.field_21051_as * this.field_21055_ao;
                this.motionZ = this.field_21050_at * this.field_21055_ao;
            }

            float var2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.renderYawOffset += (-((float) Math.atan2(this.motionX, this.motionZ)) * 180.0F / MathConstants.PI - this.renderYawOffset) * 0.1F;
            this.rotationYaw = this.renderYawOffset;
            this.field_21061_c += MathConstants.PI * this.field_21053_aq * 1.5F;
            this.field_21063_a += (-((float) Math.atan2(var2, this.motionY)) * 180.0F / MathConstants.PI - this.field_21063_a) * 0.1F;
        } else {
            this.field_21057_am = MathHelper.abs(MathHelper.sin(this.field_21060_ak)) * MathConstants.PI * 0.25F;
            if (!this.isMultiplayerEntity) {
                this.motionX = 0.0D;
                this.motionY -= 0.08D;
                this.motionY *= 0.9800000190734863D;
                this.motionZ = 0.0D;
            }

            this.field_21063_a = (float) ((double) this.field_21063_a + (double) (-90.0F - this.field_21063_a) * 0.02D);
        }

    }

    @Override
    public void moveEntityWithHeading(float var1, float var2) {
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    @Override
    protected void updatePlayerActionState() {
        if (this.rand.nextInt(50) == 0 || !this.inWater || this.field_21052_ar == 0.0F && this.field_21051_as == 0.0F && this.field_21050_at == 0.0F) {
            float var1 = this.rand.nextFloat() * MathConstants.PI * 2.0F;
            this.field_21052_ar = MathHelper.cos(var1) * 0.2F;
            this.field_21051_as = -0.1F + this.rand.nextFloat() * 0.2F;
            this.field_21050_at = MathHelper.sin(var1) * 0.2F;
        }

        this.func_27013_Q();
    }
}
