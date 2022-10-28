package net.potion.entity.monster;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.entity.Entity;
import net.potion.entity.EntityLightningBolt;
import net.potion.entity.EntityMob;
import net.potion.item.Item;
import net.potion.nbt.TagCompound;
import net.potion.world.World;

public class EntityCreeper extends EntityMob {
    int timeSinceIgnited;
    int lastActiveTime;

    public EntityCreeper(World var1) {
        super(var1);
        this.texture = "/mob/creeper.png";
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte) -1);
        this.dataWatcher.addObject(17, (byte) 0);
    }

    @Override
    public void writeEntityToNBT(TagCompound var1) {
        super.writeEntityToNBT(var1);
        if (this.dataWatcher.getWatchableObjectByte(17) == 1) {
            var1.setBoolean("powered", true);
        }

    }

    @Override
    public void readEntityFromNBT(TagCompound var1) {
        super.readEntityFromNBT(var1);
        this.dataWatcher.updateObject(17, (byte) (var1.getBoolean("powered") ? 1 : 0));
    }

    @Override
    protected void attackBlockedEntity(Entity var1, float var2) {
        if (!this.world.localWorld) {
            if (this.timeSinceIgnited > 0) {
                this.setCreeperState(-1);
                --this.timeSinceIgnited;
                if (this.timeSinceIgnited < 0) {
                    this.timeSinceIgnited = 0;
                }
            }

        }
    }

    @Override
    public void onUpdate() {
        this.lastActiveTime = this.timeSinceIgnited;
        if (this.world.localWorld) {
            int var1 = this.getCreeperState();
            if (var1 > 0 && this.timeSinceIgnited == 0) {
                this.world.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
            }

            this.timeSinceIgnited += var1;
            if (this.timeSinceIgnited < 0) {
                this.timeSinceIgnited = 0;
            }

            if (this.timeSinceIgnited >= 30) {
                this.timeSinceIgnited = 30;
            }
        }

        super.onUpdate();
        if (this.playerToAttack == null && this.timeSinceIgnited > 0) {
            this.setCreeperState(-1);
            --this.timeSinceIgnited;
            if (this.timeSinceIgnited < 0) {
                this.timeSinceIgnited = 0;
            }
        }

    }

    @Override
    protected String getHurtSound() {
        return "mob.creeper";
    }

    @Override
    protected String getDeathSound() {
        return "mob.creeperdeath";
    }

    @Override
    public void onDeath(Entity var1) {
        super.onDeath(var1);
        if (var1 instanceof EntitySkeleton) {
            this.dropItem(Item.RECORD_13.shiftedIndex + this.rand.nextInt(2), 1);
        }

    }

    @Override
    protected void attackEntity(Entity var1, float var2) {
        if (!this.world.localWorld) {
            int var3 = this.getCreeperState();
            if (var3 <= 0 && var2 < 3.0F || var3 > 0 && var2 < 7.0F) {
                if (this.timeSinceIgnited == 0) {
                    this.world.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
                }

                this.setCreeperState(1);
                ++this.timeSinceIgnited;
                if (this.timeSinceIgnited >= 30) {
                    if (this.getPowered()) {
                        this.world.createExplosion(this, this.posX, this.posY, this.posZ, 6.0F);
                    } else {
                        this.world.createExplosion(this, this.posX, this.posY, this.posZ, 3.0F);
                    }

                    this.setEntityDead();
                }

                this.hasAttacked = true;
            } else {
                this.setCreeperState(-1);
                --this.timeSinceIgnited;
                if (this.timeSinceIgnited < 0) {
                    this.timeSinceIgnited = 0;
                }
            }

        }
    }

    public boolean getPowered() {
        return this.dataWatcher.getWatchableObjectByte(17) == 1;
    }

    @Side(CodeSide.CLIENT)
    public float setCreeperFlashTime(float var1) {
        return ((float) this.lastActiveTime + (float) (this.timeSinceIgnited - this.lastActiveTime) * var1) / 28.0F;
    }

    @Override
    protected int getDropItemId() {
        return Item.GUNPOWDER.shiftedIndex;
    }

    private int getCreeperState() {
        return this.dataWatcher.getWatchableObjectByte(16);
    }

    private void setCreeperState(int var1) {
        this.dataWatcher.updateObject(16, (byte) var1);
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt var1) {
        super.onStruckByLightning(var1);
        this.dataWatcher.updateObject(17, (byte) 1);
    }
}
