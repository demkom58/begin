package net.potion.entity;

import net.potion.block.Block;
import net.potion.nbt.TagCompound;
import net.hypnosis.util.math.MathHelper;
import net.potion.world.World;

public abstract class EntityAnimal extends EntityCreature implements IAnimals {
    public EntityAnimal(World var1) {
        super(var1);
    }

    @Override
    protected float getBlockPathWeight(int var1, int var2, int var3) {
        return this.worldObj.getBlockId(var1, var2 - 1, var3) == Block.GRASS.blockID ? 10.0F : this.worldObj.getLightBrightness(var1, var2, var3) - 0.5F;
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
        return this.worldObj.getBlockId(var1, var2 - 1, var3) == Block.GRASS.blockID && this.worldObj.getBlockLightValueNoChecks(var1, var2, var3) > 8 && super.getCanSpawnHere();
    }

    @Override
    public int getTalkInterval() {
        return 120;
    }
}
