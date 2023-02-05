package net.minecraft.entity.passive;

import net.minecraft.achievement.AchievementList;
import net.minecraft.entity.EntityAnimal;
import net.minecraft.entity.EntityLightningBolt;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.TagCompound;
import net.minecraft.world.World;

public class EntityPig extends EntityAnimal {
    public EntityPig(World var1) {
        super(var1);
        this.texture = "/mob/pig.png";
        this.setSize(0.9F, 0.9F);
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(16, (byte) 0);
    }

    @Override
    public void writeEntityToNBT(TagCompound var1) {
        super.writeEntityToNBT(var1);
        var1.setBoolean("Saddle", this.getSaddled());
    }

    @Override
    public void readEntityFromNBT(TagCompound var1) {
        super.readEntityFromNBT(var1);
        this.setSaddled(var1.getBoolean("Saddle"));
    }

    @Override
    protected String getLivingSound() {
        return "mob.pig";
    }

    @Override
    protected String getHurtSound() {
        return "mob.pig";
    }

    @Override
    protected String getDeathSound() {
        return "mob.pigdeath";
    }

    @Override
    public boolean interact(EntityPlayer var1) {
        if (!this.getSaddled() || this.world.localWorld || this.riddenByEntity != null && this.riddenByEntity != var1) {
            return false;
        } else {
            var1.mountEntity(this);
            return true;
        }
    }

    @Override
    protected int getDropItemId() {
        return this.fire > 0 ? Item.PORKCHOP_COOKED.shiftedIndex : Item.PORKCHOP_RAW.shiftedIndex;
    }

    public boolean getSaddled() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setSaddled(boolean var1) {
        if (var1) {
            this.dataWatcher.updateObject(16, (byte) 1);
        } else {
            this.dataWatcher.updateObject(16, (byte) 0);
        }

    }

    @Override
    public void onStruckByLightning(EntityLightningBolt var1) {
        if (!this.world.localWorld) {
            EntityPigZombie var2 = new EntityPigZombie(this.world);
            var2.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.world.entityJoinedWorld(var2);
            this.setEntityDead();
        }
    }

    @Override
    protected void fall(float var1) {
        super.fall(var1);
        if (var1 > 5.0F && this.riddenByEntity instanceof EntityPlayer) {
            ((EntityPlayer) this.riddenByEntity).triggerAchievement(AchievementList.flyPig);
        }

    }
}
