package net.minecraft;

import net.minecraft.nbt.NBTTagCompound;
import util.MathHelper;

public abstract class EntityAnimal extends EntityCreature implements IAnimals {
    public EntityAnimal(World var1) {
        super(var1);
    }

    protected float getBlockPathWeight(int var1, int var2, int var3) {
        return this.worldObj.getBlockId(var1, var2 - 1, var3) == Block.GRASS.blockID ? 10.0F : this.worldObj.getLightBrightness(var1, var2, var3) - 0.5F;
    }

    public void writeEntityToNBT(NBTTagCompound var1) {
        super.writeEntityToNBT(var1);
    }

    public void readEntityFromNBT(NBTTagCompound var1) {
        super.readEntityFromNBT(var1);
    }

    public boolean getCanSpawnHere() {
        int var1 = MathHelper.floor(this.posX);
        int var2 = MathHelper.floor(this.boundingBox.minY);
        int var3 = MathHelper.floor(this.posZ);
        return this.worldObj.getBlockId(var1, var2 - 1, var3) == Block.GRASS.blockID && this.worldObj.getBlockLightValueNoChecks(var1, var2, var3) > 8 && super.getCanSpawnHere();
    }

    public int getTalkInterval() {
        return 120;
    }
}
