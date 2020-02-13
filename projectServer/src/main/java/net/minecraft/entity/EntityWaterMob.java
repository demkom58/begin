package net.minecraft.entity;

import net.minecraft.nbt.TagCompound;
import net.minecraft.world.World;

public class EntityWaterMob extends EntityCreature implements IAnimals {
    public EntityWaterMob(World var1) {
        super(var1);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
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
        return this.worldObj.checkIfAABBIsClear(this.boundingBox);
    }

    @Override
    public int getTalkInterval() {
        return 120;
    }
}
