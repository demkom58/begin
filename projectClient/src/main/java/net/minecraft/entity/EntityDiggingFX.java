package net.minecraft.entity;

import net.hypnosis.render.Tessellator;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class EntityDiggingFX extends EntityFX {
    private Block field_4082_a;
    private int field_32001_o = 0;

    public EntityDiggingFX(World world, double x, double y, double z, double motX, double motY, double motZ, Block block, int var15, int var16) {
        super(world, x, y, z, motX, motY, motZ);
        this.field_4082_a = block;
        this.particleTextureIndex = block.getBlockTextureFromSideAndMetadata(0, var16);
        this.particleGravity = block.blockParticleGravity;
        this.particleRed = this.particleGreen = this.particleBlue = 0.6F;
        this.particleScale /= 2.0F;
        this.field_32001_o = var15;
    }

    public EntityDiggingFX position(int x, int y, int z) {
        if (this.field_4082_a == Block.GRASS)
            return this;

        int multiplier = this.field_4082_a.colorMultiplier(this.worldObj, x, y, z);
        this.particleRed *= (float) (multiplier >> 16 & 255) / 255.0F;
        this.particleGreen *= (float) (multiplier >> 8 & 255) / 255.0F;
        this.particleBlue *= (float) (multiplier & 255) / 255.0F;
        return this;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public void renderParticle(Tessellator tess, float var2, float var3, float var4, float var5, float var6, float var7) {
        float var8 = ((float) (this.particleTextureIndex % 16) + this.particleTextureJitterX / 4.0F) / 16.0F;
        float var9 = var8 + 0.015609375F;
        float var10 = ((float) (this.particleTextureIndex / 16) + this.particleTextureJitterY / 4.0F) / 16.0F;
        float var11 = var10 + 0.015609375F;
        float var12 = 0.1F * this.particleScale;
        float var13 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) var2 - interpPosX);
        float var14 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) var2 - interpPosY);
        float var15 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) var2 - interpPosZ);
        float var16 = this.getEntityBrightness(var2);
        tess.setColorOpaque_F(var16 * this.particleRed, var16 * this.particleGreen, var16 * this.particleBlue);
        tess.addVertexWithUV(var13 - var3 * var12 - var6 * var12, var14 - var4 * var12, var15 - var5 * var12 - var7 * var12, var8, var11);
        tess.addVertexWithUV(var13 - var3 * var12 + var6 * var12, var14 + var4 * var12, var15 - var5 * var12 + var7 * var12, var8, var10);
        tess.addVertexWithUV(var13 + var3 * var12 + var6 * var12, var14 + var4 * var12, var15 + var5 * var12 + var7 * var12, var9, var10);
        tess.addVertexWithUV(var13 + var3 * var12 - var6 * var12, var14 - var4 * var12, var15 + var5 * var12 - var7 * var12, var9, var11);
    }
}
