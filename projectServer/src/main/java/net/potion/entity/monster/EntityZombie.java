package net.potion.entity.monster;

import net.potion.entity.EntityMob;
import net.potion.item.Item;
import net.potion.world.World;
import net.potion.util.MathHelper;

public class EntityZombie extends EntityMob {
    public EntityZombie(World var1) {
        super(var1);
        this.texture = "/mob/zombie.png";
        this.moveSpeed = 0.5F;
        this.attackStrength = 5;
    }

    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isDaytime()) {
            float var1 = this.getEntityBrightness(1.0F);
            if (var1 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)) && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F) {
                this.fire = 300;
            }
        }

        super.onLivingUpdate();
    }

    @Override
    protected String getLivingSound() {
        return "mob.zombie";
    }

    @Override
    protected String getHurtSound() {
        return "mob.zombiehurt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.zombiedeath";
    }

    @Override
    protected int getDropItemId() {
        return Item.FEATHER.shiftedIndex;
    }
}
