package net.potion.entity.monster;

import net.potion.entity.Entity;
import net.potion.entity.EntityMob;
import net.potion.item.Item;
import net.potion.nbt.TagCompound;
import net.potion.world.World;
import net.potion.util.MathHelper;

public class EntitySpider extends EntityMob {
    public EntitySpider(World var1) {
        super(var1);
        this.texture = "/mob/spider.png";
        this.setSize(1.4F, 0.9F);
        this.moveSpeed = 0.8F;
    }

    @Override
    public double getMountedYOffset() {
        return (double) this.height * 0.75D - 0.5D;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected Entity findPlayerToAttack() {
        float var1 = this.getEntityBrightness(1.0F);
        if (var1 < 0.5F) {
            double var2 = 16.0D;
            return this.worldObj.getClosestPlayerToEntity(this, var2);
        } else {
            return null;
        }
    }

    @Override
    protected String getLivingSound() {
        return "mob.spider";
    }

    @Override
    protected String getHurtSound() {
        return "mob.spider";
    }

    @Override
    protected String getDeathSound() {
        return "mob.spiderdeath";
    }

    @Override
    protected void attackEntity(Entity var1, float var2) {
        float var3 = this.getEntityBrightness(1.0F);
        if (var3 > 0.5F && this.rand.nextInt(100) == 0) {
            this.playerToAttack = null;
        } else {
            if (var2 > 2.0F && var2 < 6.0F && this.rand.nextInt(10) == 0) {
                if (this.onGround) {
                    double var4 = var1.posX - this.posX;
                    double var6 = var1.posZ - this.posZ;
                    float var8 = MathHelper.sqrt(var4 * var4 + var6 * var6);
                    this.motionX = var4 / (double) var8 * 0.5D * 0.800000011920929D + this.motionX * 0.20000000298023224D;
                    this.motionZ = var6 / (double) var8 * 0.5D * 0.800000011920929D + this.motionZ * 0.20000000298023224D;
                    this.motionY = 0.4000000059604645D;
                }
            } else {
                super.attackEntity(var1, var2);
            }

        }
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
    protected int getDropItemId() {
        return Item.SILK.shiftedIndex;
    }

    @Override
    public boolean isOnLadder() {
        return this.isCollidedHorizontally;
    }
}
