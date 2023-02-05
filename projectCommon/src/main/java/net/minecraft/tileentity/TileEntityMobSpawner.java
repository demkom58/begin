package net.minecraft.tileentity;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.TagCompound;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityMobSpawner extends TileEntity {
    public int delay = -1;
    public double yaw;
    public double yaw2 = 0.0D;
    private String mobID = "Pig";

    public TileEntityMobSpawner() {
        this.delay = 20;
    }

    public String getMobID() {
        return this.mobID;
    }

    public void setMobID(String var1) {
        this.mobID = var1;
    }

    public boolean anyPlayerInRange() {
        return this.world.getClosestPlayer((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D, 16.0D) != null;
    }

    @Override
    public void updateEntity() {
        this.yaw2 = this.yaw;
        if (this.anyPlayerInRange()) {
            double var1 = (float) this.xCoord + this.world.rand.nextFloat();
            double var3 = (float) this.yCoord + this.world.rand.nextFloat();
            double var5 = (float) this.zCoord + this.world.rand.nextFloat();
            this.world.spawnParticle("smoke", var1, var3, var5, 0.0D, 0.0D, 0.0D);
            this.world.spawnParticle("flame", var1, var3, var5, 0.0D, 0.0D, 0.0D);

            for (this.yaw += 1000.0F / ((float) this.delay + 200.0F); this.yaw > 360.0D; this.yaw2 -= 360.0D) {
                this.yaw -= 360.0D;
            }

            if (!this.world.localWorld) {
                if (this.delay == -1) {
                    this.updateDelay();
                }

                if (this.delay > 0) {
                    --this.delay;
                    return;
                }

                byte var7 = 4;

                for (int var8 = 0; var8 < var7; ++var8) {
                    EntityLiving var9 = (EntityLiving) EntityList.createEntityInWorld(this.mobID, this.world);
                    if (var9 == null) {
                        return;
                    }

                    int var10 = this.world.getEntitiesWithinAABB(var9.getClass(), AxisAlignedBB.getBoundingBoxFromPool(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(8.0D, 4.0D, 8.0D)).size();
                    if (var10 >= 6) {
                        this.updateDelay();
                        return;
                    }

                    if (var9 != null) {
                        double var11 = (double) this.xCoord + (this.world.rand.nextDouble() - this.world.rand.nextDouble()) * 4.0D;
                        double var13 = this.yCoord + this.world.rand.nextInt(3) - 1;
                        double var15 = (double) this.zCoord + (this.world.rand.nextDouble() - this.world.rand.nextDouble()) * 4.0D;
                        var9.setLocationAndAngles(var11, var13, var15, this.world.rand.nextFloat() * 360.0F, 0.0F);
                        if (var9.getCanSpawnHere()) {
                            this.world.entityJoinedWorld(var9);

                            for (int var17 = 0; var17 < 20; ++var17) {
                                var1 = (double) this.xCoord + 0.5D + ((double) this.world.rand.nextFloat() - 0.5D) * 2.0D;
                                var3 = (double) this.yCoord + 0.5D + ((double) this.world.rand.nextFloat() - 0.5D) * 2.0D;
                                var5 = (double) this.zCoord + 0.5D + ((double) this.world.rand.nextFloat() - 0.5D) * 2.0D;
                                this.world.spawnParticle("smoke", var1, var3, var5, 0.0D, 0.0D, 0.0D);
                                this.world.spawnParticle("flame", var1, var3, var5, 0.0D, 0.0D, 0.0D);
                            }

                            var9.spawnExplosionParticle();
                            this.updateDelay();
                        }
                    }
                }
            }

            super.updateEntity();
        }
    }

    private void updateDelay() {
        this.delay = 200 + this.world.rand.nextInt(600);
    }

    @Override
    public void readFromNBT(TagCompound tag) {
        super.readFromNBT(tag);
        this.mobID = tag.getString("EntityId");
        this.delay = tag.getShort("Delay");
    }

    @Override
    public void writeToNBT(TagCompound tag) {
        super.writeToNBT(tag);
        tag.setString("EntityId", this.mobID);
        tag.setShort("Delay", (short) this.delay);
    }
}
