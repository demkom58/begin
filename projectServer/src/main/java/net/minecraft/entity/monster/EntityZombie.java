package net.minecraft.entity.monster;

import net.minecraft.entity.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import util.MathHelper;

public class EntityZombie extends EntityMob {
    public EntityZombie(World var1) {
        super(var1);
        this.texture = "/mob/zombie.png";
        this.moveSpeed = 0.5F;
        this.attackStrength = 5;
    }

    public void onLivingUpdate() {
        if (this.worldObj.isDaytime()) {
            float var1 = this.getEntityBrightness(1.0F);
            if (var1 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)) && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F) {
                this.fire = 300;
            }
        }

        super.onLivingUpdate();
    }

    protected String getLivingSound() {
        return "mob.zombie";
    }

    protected String getHurtSound() {
        return "mob.zombiehurt";
    }

    protected String getDeathSound() {
        return "mob.zombiedeath";
    }

    protected int getDropItemId() {
        return Item.FEATHER.shiftedIndex;
    }
}
