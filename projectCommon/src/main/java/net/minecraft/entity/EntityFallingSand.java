package net.minecraft.entity;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.block.BlockSand;
import net.minecraft.nbt.TagCompound;
import net.hypnosis.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityFallingSand extends Entity {
    public int blockID;
    public int fallTime = 0;

    public EntityFallingSand(World var1) {
        super(var1);
    }

    public EntityFallingSand(World var1, double var2, double var4, double var6, int var8) {
        super(var1);
        this.blockID = var8;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(var2, var4, var6);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = var2;
        this.prevPosY = var4;
        this.prevPosZ = var6;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public void onUpdate() {
        if (this.blockID == 0) {
            this.setEntityDead();
        } else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            ++this.fallTime;
            this.motionY -= 0.03999999910593033D;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9800000190734863D;
            int var1 = MathHelper.floor(this.posX);
            int var2 = MathHelper.floor(this.posY);
            int var3 = MathHelper.floor(this.posZ);
            if (this.world.getBlockId(var1, var2, var3) == this.blockID) {
                this.world.setBlockWithNotify(var1, var2, var3, 0);
            }

            if (this.onGround) {
                this.motionX *= 0.699999988079071D;
                this.motionZ *= 0.699999988079071D;
                this.motionY *= -0.5D;
                this.setEntityDead();
                if ((!this.world.canBlockBePlacedAt(this.blockID, var1, var2, var3, true, 1) || BlockSand.canFallBelow(this.world, var1, var2 - 1, var3) || !this.world.setBlockWithNotify(var1, var2, var3, this.blockID)) && !this.world.localWorld) {
                    this.dropItem(this.blockID, 1);
                }
            } else if (this.fallTime > 100 && !this.world.localWorld) {
                this.dropItem(this.blockID, 1);
                this.setEntityDead();
            }

        }
    }

    @Override
    protected void writeEntityToNBT(TagCompound var1) {
        var1.setByte("Tile", (byte) this.blockID);
    }

    @Override
    protected void readEntityFromNBT(TagCompound var1) {
        this.blockID = var1.getByte("Tile") & 255;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

}
