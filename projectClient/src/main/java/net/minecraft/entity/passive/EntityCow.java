package net.minecraft.entity.passive;

import net.minecraft.entity.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.TagCompound;
import net.minecraft.world.World;

public class EntityCow extends EntityAnimal {
    public EntityCow(World var1) {
        super(var1);
        this.texture = "/mob/cow.png";
        this.setSize(0.9F, 1.3F);
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
        return "mob.cow";
    }

    @Override
    protected String getHurtSound() {
        return "mob.cowhurt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.cowhurt";
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected int getDropItemId() {
        return Item.LEATHER.shiftedIndex;
    }

    @Override
    public boolean interact(EntityPlayer var1) {
        ItemStack var2 = var1.inventory.getCurrentItem();
        if (var2 != null && var2.itemID == Item.BUCKET_EMPTY.shiftedIndex) {
            var1.inventory.setInventorySlotContents(var1.inventory.currentItem, new ItemStack(Item.BUCKET_MILK));
            return true;
        } else {
            return false;
        }
    }
}
