package net.potion.entity;

import net.hypnosis.util.math.MathHelper;
import net.potion.block.EnumSkyBlock;
import net.potion.entity.player.EntityPlayer;
import net.potion.nbt.TagCompound;
import net.potion.world.World;

public class EntityMob extends EntityCreature implements IMob {
    protected int attackStrength = 2;

    public EntityMob(World var1) {
        super(var1);
        this.health = 20;
    }

    @Override
    public void onLivingUpdate() {
        float var1 = this.getEntityBrightness(1.0F);
        if (var1 > 0.5F) {
            this.entityAge += 2;
        }

        super.onLivingUpdate();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.localWorld && this.world.difficultySetting == 0) {
            this.setEntityDead();
        }

    }

    @Override
    protected Entity findPlayerToAttack() {
        EntityPlayer var1 = this.world.getClosestPlayerToEntity(this, 16.0D);
        return var1 != null && this.canEntityBeSeen(var1) ? var1 : null;
    }

    @Override
    public boolean attackEntityFrom(Entity var1, int var2) {
        if (!super.attackEntityFrom(var1, var2)) {
            return false;
        }

        if (this.riddenByEntity != var1 && this.ridingEntity != var1) {
            if (var1 != this) {
                this.playerToAttack = var1;
            }

        }

        return true;
    }

    @Override
    protected void attackEntity(Entity var1, float var2) {
        if (this.attackTime <= 0 && var2 < 2.0F && var1.boundingBox.maxY > this.boundingBox.minY && var1.boundingBox.minY < this.boundingBox.maxY) {
            this.attackTime = 20;
            var1.attackEntityFrom(this, this.attackStrength);
        }

    }

    @Override
    protected float getBlockPathWeight(int var1, int var2, int var3) {
        return 0.5F - this.world.getLightBrightness(var1, var2, var3);
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
    public boolean getCanSpawnHere() {
        int var1 = MathHelper.floor(this.posX);
        int var2 = MathHelper.floor(this.boundingBox.minY);
        int var3 = MathHelper.floor(this.posZ);
        if (this.world.getSavedLightValue(EnumSkyBlock.SKY, var1, var2, var3) > this.rand.nextInt(32)) {
            return false;
        } else {
            int var4 = this.world.getBlockLightValue(var1, var2, var3);
            if (this.world.isBigThunder()) {
                int var5 = this.world.skylightSubtracted;
                this.world.skylightSubtracted = 10;
                var4 = this.world.getBlockLightValue(var1, var2, var3);
                this.world.skylightSubtracted = var5;
            }

            return var4 <= this.rand.nextInt(8) && super.getCanSpawnHere();
        }
    }
}
