package net.minecraft.entity;

import net.minecraft.nbt.TagCompound;
import net.minecraft.world.World;

public class EntityWaterMob extends EntityCreature {
    public EntityWaterMob(World var1) {
        super(var1);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public void writeEntityToNBT(TagCompound var1) {
        super.writeEntityToNBT(var1);
    }

    public void readEntityFromNBT(TagCompound var1) {
        super.readEntityFromNBT(var1);
    }

    public boolean getCanSpawnHere() {
        return this.worldObj.checkIfAABBIsClear(this.boundingBox);
    }

    public int getTalkInterval() {
        return 120;
    }
}
