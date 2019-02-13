package net.minecraft;

import util.MathHelper;
import util.Vec3D;

import java.util.*;

public class Explosion {
    public boolean isFlaming = false;
    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public Entity exploder;
    public float explosionSize;
    public Set destroyedBlockPositions = new HashSet();
    private Random ExplosionRNG = new Random();
    private World worldObj;

    public Explosion(World var1, Entity var2, double var3, double var5, double var7, float var9) {
        this.worldObj = var1;
        this.exploder = var2;
        this.explosionSize = var9;
        this.explosionX = var3;
        this.explosionY = var5;
        this.explosionZ = var7;
    }

    public void doExplosionA() {
        float var1 = this.explosionSize;
        byte var2 = 16;

        for (int var3 = 0; var3 < var2; ++var3) {
            for (int var4 = 0; var4 < var2; ++var4) {
                for (int var5 = 0; var5 < var2; ++var5) {
                    if (var3 == 0 || var3 == var2 - 1 || var4 == 0 || var4 == var2 - 1 || var5 == 0 || var5 == var2 - 1) {
                        double var6 = (double) ((float) var3 / ((float) var2 - 1.0F) * 2.0F - 1.0F);
                        double var8 = (double) ((float) var4 / ((float) var2 - 1.0F) * 2.0F - 1.0F);
                        double var10 = (double) ((float) var5 / ((float) var2 - 1.0F) * 2.0F - 1.0F);
                        double var12 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
                        var6 = var6 / var12;
                        var8 = var8 / var12;
                        var10 = var10 / var12;
                        float var14 = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
                        double var15 = this.explosionX;
                        double var17 = this.explosionY;
                        double var19 = this.explosionZ;

                        for (float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * 0.75F) {
                            int var22 = MathHelper.floor_double(var15);
                            int var23 = MathHelper.floor_double(var17);
                            int var24 = MathHelper.floor_double(var19);
                            int var25 = this.worldObj.getBlockId(var22, var23, var24);
                            if (var25 > 0) {
                                var14 -= (Block.BLOCKS_LIST[var25].getExplosionResistance(this.exploder) + 0.3F) * var21;
                            }

                            if (var14 > 0.0F) {
                                this.destroyedBlockPositions.add(new ChunkPosition(var22, var23, var24));
                            }

                            var15 += var6 * (double) var21;
                            var17 += var8 * (double) var21;
                            var19 += var10 * (double) var21;
                        }
                    }
                }
            }
        }

        this.explosionSize *= 2.0F;
        int var29 = MathHelper.floor_double(this.explosionX - (double) this.explosionSize - 1.0D);
        int var30 = MathHelper.floor_double(this.explosionX + (double) this.explosionSize + 1.0D);
        int var31 = MathHelper.floor_double(this.explosionY - (double) this.explosionSize - 1.0D);
        int var33 = MathHelper.floor_double(this.explosionY + (double) this.explosionSize + 1.0D);
        int var7 = MathHelper.floor_double(this.explosionZ - (double) this.explosionSize - 1.0D);
        int var35 = MathHelper.floor_double(this.explosionZ + (double) this.explosionSize + 1.0D);
        List var9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.getBoundingBoxFromPool((double) var29, (double) var31, (double) var7, (double) var30, (double) var33, (double) var35));
        Vec3D var37 = Vec3D.createVector(this.explosionX, this.explosionY, this.explosionZ);

        for (int var11 = 0; var11 < var9.size(); ++var11) {
            Entity var39 = (Entity) var9.get(var11);
            double var13 = var39.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double) this.explosionSize;
            if (var13 <= 1.0D) {
                double var43 = var39.posX - this.explosionX;
                double var46 = var39.posY - this.explosionY;
                double var49 = var39.posZ - this.explosionZ;
                double var51 = (double) MathHelper.sqrt_double(var43 * var43 + var46 * var46 + var49 * var49);
                var43 = var43 / var51;
                var46 = var46 / var51;
                var49 = var49 / var51;
                double var52 = (double) this.worldObj.func_675_a(var37, var39.boundingBox);
                double var53 = (1.0D - var13) * var52;
                var39.attackEntityFrom(this.exploder, (int) ((var53 * var53 + var53) / 2.0D * 8.0D * (double) this.explosionSize + 1.0D));
                var39.motionX += var43 * var53;
                var39.motionY += var46 * var53;
                var39.motionZ += var49 * var53;
            }
        }

        this.explosionSize = var1;
        ArrayList var38 = new ArrayList();
        var38.addAll(this.destroyedBlockPositions);
        if (this.isFlaming) {
            for (int var40 = var38.size() - 1; var40 >= 0; --var40) {
                ChunkPosition var41 = (ChunkPosition) var38.get(var40);
                int var42 = var41.x;
                int var45 = var41.y;
                int var16 = var41.z;
                int var48 = this.worldObj.getBlockId(var42, var45, var16);
                int var18 = this.worldObj.getBlockId(var42, var45 - 1, var16);
                if (var48 == 0 && Block.OPAQUE_CUBE_LOOKUP[var18] && this.ExplosionRNG.nextInt(3) == 0) {
                    this.worldObj.setBlockWithNotify(var42, var45, var16, Block.fire.blockID);
                }
            }
        }

    }

    public void doExplosionB(boolean var1) {
        this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
        ArrayList var2 = new ArrayList();
        var2.addAll(this.destroyedBlockPositions);

        for (int var3 = var2.size() - 1; var3 >= 0; --var3) {
            ChunkPosition var4 = (ChunkPosition) var2.get(var3);
            int var5 = var4.x;
            int var6 = var4.y;
            int var7 = var4.z;
            int var8 = this.worldObj.getBlockId(var5, var6, var7);
            if (var1) {
                double var9 = (double) ((float) var5 + this.worldObj.rand.nextFloat());
                double var11 = (double) ((float) var6 + this.worldObj.rand.nextFloat());
                double var13 = (double) ((float) var7 + this.worldObj.rand.nextFloat());
                double var15 = var9 - this.explosionX;
                double var17 = var11 - this.explosionY;
                double var19 = var13 - this.explosionZ;
                double var21 = (double) MathHelper.sqrt_double(var15 * var15 + var17 * var17 + var19 * var19);
                var15 = var15 / var21;
                var17 = var17 / var21;
                var19 = var19 / var21;
                double var23 = 0.5D / (var21 / (double) this.explosionSize + 0.1D);
                var23 = var23 * (double) (this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
                var15 = var15 * var23;
                var17 = var17 * var23;
                var19 = var19 * var23;
                this.worldObj.spawnParticle("explode", (var9 + this.explosionX * 1.0D) / 2.0D, (var11 + this.explosionY * 1.0D) / 2.0D, (var13 + this.explosionZ * 1.0D) / 2.0D, var15, var17, var19);
                this.worldObj.spawnParticle("smoke", var9, var11, var13, var15, var17, var19);
            }

            if (var8 > 0) {
                Block.BLOCKS_LIST[var8].dropBlockAsItemWithChance(this.worldObj, var5, var6, var7, this.worldObj.getBlockMetadata(var5, var6, var7), 0.3F);
                this.worldObj.setBlockWithNotify(var5, var6, var7, 0);
                Block.BLOCKS_LIST[var8].onBlockDestroyedByExplosion(this.worldObj, var5, var6, var7);
            }
        }

    }
}
