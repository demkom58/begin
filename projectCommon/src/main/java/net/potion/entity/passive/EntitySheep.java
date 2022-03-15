package net.potion.entity.passive;

import net.potion.block.Block;
import net.potion.entity.Entity;
import net.potion.entity.EntityAnimal;
import net.potion.entity.item.EntityItem;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.Item;
import net.potion.item.ItemStack;
import net.potion.nbt.TagCompound;
import net.potion.world.World;

import java.util.Random;

public class EntitySheep extends EntityAnimal {
    public static final float[][] fleeceColorTable = new float[][]{{1.0F, 1.0F, 1.0F}, {0.95F, 0.7F, 0.2F}, {0.9F, 0.5F, 0.85F}, {0.6F, 0.7F, 0.95F}, {0.9F, 0.9F, 0.2F}, {0.5F, 0.8F, 0.1F}, {0.95F, 0.7F, 0.8F}, {0.3F, 0.3F, 0.3F}, {0.6F, 0.6F, 0.6F}, {0.3F, 0.6F, 0.7F}, {0.7F, 0.4F, 0.9F}, {0.2F, 0.4F, 0.8F}, {0.5F, 0.4F, 0.3F}, {0.4F, 0.5F, 0.2F}, {0.8F, 0.3F, 0.3F}, {0.1F, 0.1F, 0.1F}};

    public EntitySheep(World var1) {
        super(var1);
        this.texture = "/mob/sheep.png";
        this.setSize(0.9F, 1.3F);
    }

    public static int getRandomFleeceColor(Random var0) {
        int var1 = var0.nextInt(100);
        if (var1 < 5) {
            return 15;
        } else if (var1 < 10) {
            return 7;
        } else if (var1 < 15) {
            return 8;
        } else if (var1 < 18) {
            return 12;
        } else {
            return var0.nextInt(500) == 0 ? 6 : 0;
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte) 0);
    }

    @Override
    public boolean attackEntityFrom(Entity var1, int var2) {
        return super.attackEntityFrom(var1, var2);
    }

    @Override
    protected void dropFewItems() {
        if (!this.getSheared()) {
            this.entityDropItem(new ItemStack(Block.CLOTH.blockID, 1, this.getFleeceColor()), 0.0F);
        }

    }

    @Override
    protected int getDropItemId() {
        return Block.CLOTH.blockID;
    }

    @Override
    public boolean interact(EntityPlayer var1) {
        ItemStack var2 = var1.inventory.getCurrentItem();
        if (var2 != null && var2.itemID == Item.SHEARS.shiftedIndex && !this.getSheared()) {
            if (!this.worldObj.localWorld) {
                this.setSheared(true);
                int var3 = 2 + this.rand.nextInt(3);

                for (int var4 = 0; var4 < var3; ++var4) {
                    EntityItem var5 = this.entityDropItem(new ItemStack(Block.CLOTH.blockID, 1, this.getFleeceColor()), 1.0F);
                    var5.motionY += this.rand.nextFloat() * 0.05F;
                    var5.motionX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
                    var5.motionZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
                }
            }

            var2.damageItem(1, var1);
        }

        return false;
    }

    @Override
    public void writeEntityToNBT(TagCompound var1) {
        super.writeEntityToNBT(var1);
        var1.setBoolean("Sheared", this.getSheared());
        var1.setByte("Color", (byte) this.getFleeceColor());
    }

    @Override
    public void readEntityFromNBT(TagCompound var1) {
        super.readEntityFromNBT(var1);
        this.setSheared(var1.getBoolean("Sheared"));
        this.setFleeceColor(var1.getByte("Color"));
    }

    @Override
    protected String getLivingSound() {
        return "mob.sheep";
    }

    @Override
    protected String getHurtSound() {
        return "mob.sheep";
    }

    @Override
    protected String getDeathSound() {
        return "mob.sheep";
    }

    public int getFleeceColor() {
        return this.dataWatcher.getWatchableObjectByte(16) & 15;
    }

    public void setFleeceColor(int var1) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        this.dataWatcher.updateObject(16, (byte) (var2 & 240 | var1 & 15));
    }

    public boolean getSheared() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 16) != 0;
    }

    public void setSheared(boolean var1) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        if (var1) {
            this.dataWatcher.updateObject(16, (byte) (var2 | 16));
        } else {
            this.dataWatcher.updateObject(16, (byte) (var2 & -17));
        }

    }
}
