package net.potion.entity.passive;

import net.potion.entity.EntityAnimal;
import net.potion.item.Item;
import net.potion.nbt.TagCompound;
import net.potion.world.World;

public class EntityChicken extends EntityAnimal {
    public boolean field1 = false;
    public float field2 = 0.0F;
    public float destPos = 0.0F;
    public float field3;
    public float field4;
    public float field5 = 1.0F;
    public int timeUntilNextEgg;

    public EntityChicken(World var1) {
        super(var1);
        this.texture = "/mob/chicken.png";
        this.setSize(0.3F, 0.4F);
        this.health = 4;
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.field4 = this.field2;
        this.field3 = this.destPos;
        this.destPos = (float) ((double) this.destPos + (double) (this.onGround ? -1 : 4) * 0.3D);
        if (this.destPos < 0.0F) {
            this.destPos = 0.0F;
        }

        if (this.destPos > 1.0F) {
            this.destPos = 1.0F;
        }

        if (!this.onGround && this.field5 < 1.0F) {
            this.field5 = 1.0F;
        }

        this.field5 = (float) ((double) this.field5 * 0.9D);
        if (!this.onGround && this.motionY < 0.0D) {
            this.motionY *= 0.6D;
        }

        this.field2 += this.field5 * 2.0F;
        if (!this.world.localWorld && --this.timeUntilNextEgg <= 0) {
            this.world.playSoundAtEntity(this, "mob.chickenplop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(Item.EGG.shiftedIndex, 1);
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }

    }

    @Override
    protected void fall(float var1) {
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
        return "mob.chicken";
    }

    @Override
    protected String getHurtSound() {
        return "mob.chickenhurt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.chickenhurt";
    }

    @Override
    protected int getDropItemId() {
        return Item.FEATHER.shiftedIndex;
    }
}
