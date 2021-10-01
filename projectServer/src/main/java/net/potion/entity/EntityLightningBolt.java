package net.potion.entity;

import net.potion.block.Block;
import net.potion.nbt.TagCompound;
import net.potion.util.AxisAlignedBB;
import net.potion.util.MathHelper;
import net.potion.world.World;

import java.util.List;

public class EntityLightningBolt extends EntityWeatherEffect {
    public long field_27019_a = 0L;
    private int field_27018_b;
    private int field_27020_c;

    public EntityLightningBolt(World var1, double var2, double var4, double var6) {
        super(var1);
        this.setLocationAndAngles(var2, var4, var6, 0.0F, 0.0F);
        this.field_27018_b = 2;
        this.field_27019_a = this.rand.nextLong();
        this.field_27020_c = this.rand.nextInt(3) + 1;
        if (var1.difficultySetting >= 2 && var1.doChunksNearChunkExist(MathHelper.floor(var2), MathHelper.floor(var4), MathHelper.floor(var6), 10)) {
            int var8 = MathHelper.floor(var2);
            int var9 = MathHelper.floor(var4);
            int var10 = MathHelper.floor(var6);
            if (var1.getBlockId(var8, var9, var10) == 0 && Block.FIRE.canPlaceBlockAt(var1, var8, var9, var10)) {
                var1.setBlockWithNotify(var8, var9, var10, Block.FIRE.blockID);
            }

            for (int var12 = 0; var12 < 4; ++var12) {
                var9 = MathHelper.floor(var2) + this.rand.nextInt(3) - 1;
                var10 = MathHelper.floor(var4) + this.rand.nextInt(3) - 1;
                int var11 = MathHelper.floor(var6) + this.rand.nextInt(3) - 1;
                if (var1.getBlockId(var9, var10, var11) == 0 && Block.FIRE.canPlaceBlockAt(var1, var9, var10, var11)) {
                    var1.setBlockWithNotify(var9, var10, var11, Block.FIRE.blockID);
                }
            }
        }

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.field_27018_b == 2) {
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
        }

        --this.field_27018_b;
        if (this.field_27018_b < 0) {
            if (this.field_27020_c == 0) {
                this.setEntityDead();
            } else if (this.field_27018_b < -this.rand.nextInt(10)) {
                --this.field_27020_c;
                this.field_27018_b = 1;
                this.field_27019_a = this.rand.nextLong();
                if (this.worldObj.doChunksNearChunkExist(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ), 10)) {
                    int var1 = MathHelper.floor(this.posX);
                    int var2 = MathHelper.floor(this.posY);
                    int var3 = MathHelper.floor(this.posZ);
                    if (this.worldObj.getBlockId(var1, var2, var3) == 0 && Block.FIRE.canPlaceBlockAt(this.worldObj, var1, var2, var3)) {
                        this.worldObj.setBlockWithNotify(var1, var2, var3, Block.FIRE.blockID);
                    }
                }
            }
        }

        if (this.field_27018_b >= 0) {
            double var6 = 3.0D;
            List<Entity> var7 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBoxFromPool(this.posX - var6, this.posY - var6, this.posZ - var6, this.posX + var6, this.posY + 6.0D + var6, this.posZ + var6));

            for (int var4 = 0; var4 < var7.size(); ++var4) {
                Entity var5 = var7.get(var4);
                var5.onStruckByLightning(this);
            }

            this.worldObj.field_27080_i = 2;
        }

    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(TagCompound var1) {
    }

    @Override
    protected void writeEntityToNBT(TagCompound var1) {
    }
}
