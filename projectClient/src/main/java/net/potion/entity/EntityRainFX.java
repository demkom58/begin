package net.potion.entity;

import net.hypnosis.render.Tessellator;
import net.potion.block.BlockFluid;
import net.potion.material.Material;
import net.hypnosis.util.math.MathHelper;
import net.potion.world.World;

public class EntityRainFX extends EntityFX {
    public EntityRainFX(World var1, double var2, double var4, double var6) {
        super(var1, var2, var4, var6, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.30000001192092896D;
        this.motionY = (float) Math.random() * 0.2F + 0.1F;
        this.motionZ *= 0.30000001192092896D;
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.particleTextureIndex = 19 + this.rand.nextInt(4);
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
    }

    @Override
    public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
        super.renderParticle(var1, var2, var3, var4, var5, var6, var7);
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= this.particleGravity;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;
        if (this.particleMaxAge-- <= 0) {
            this.setEntityDead();
        }

        if (this.onGround) {
            if (Math.random() < 0.5D) {
                this.setEntityDead();
            }

            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }

        Material var1 = this.worldObj.getBlockMaterial(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));
        if (var1.isLiquid() || var1.isSolid()) {
            double var2 = (float) (MathHelper.floor(this.posY) + 1) - BlockFluid.setFluidHeight(this.worldObj.getBlockMetadata(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)));
            if (this.posY < var2) {
                this.setEntityDead();
            }
        }

    }
}
