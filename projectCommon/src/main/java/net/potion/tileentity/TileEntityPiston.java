package net.potion.tileentity;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.block.Block;
import net.potion.block.PistonBlockTextures;
import net.potion.entity.Entity;
import net.potion.nbt.TagCompound;
import net.potion.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class TileEntityPiston extends TileEntity {
    private static final List<Entity> ENTITIES = new ArrayList<>();
    private int storedBlockID;
    private int storedMetadata;
    private int storedOrientation;
    private boolean extending;
    private boolean field1;
    private float progress;
    private float lastProgress;

    public TileEntityPiston() {
    }

    public TileEntityPiston(int var1, int var2, int var3, boolean var4, boolean var5) {
        this.storedBlockID = var1;
        this.storedMetadata = var2;
        this.storedOrientation = var3;
        this.extending = var4;
        this.field1 = var5;
    }

    public int getStoredBlockID() {
        return this.storedBlockID;
    }

    @Override
    public int getBlockMetadata() {
        return this.storedMetadata;
    }

    public boolean isExtending() {
        return this.extending;
    }

    public int getStoredOrientation() {
        return this.storedOrientation;
    }

    @Side(CodeSide.CLIENT)
    public boolean method1() {
        return this.field1;
    }

    public float getProgress(float delta) {
        if (delta > 1.0F) {
            delta = 1.0F;
        }

        return this.lastProgress + (this.progress - this.lastProgress) * delta;
    }

    @Side(CodeSide.CLIENT)
    public float method2(float var1) {
        return this.extending ? (this.getProgress(var1) - 1.0F) * (float) PistonBlockTextures.field2[this.storedOrientation] : (1.0F - this.getProgress(var1)) * (float) PistonBlockTextures.field2[this.storedOrientation];
    }

    @Side(CodeSide.CLIENT)
    public float method3(float var1) {
        return this.extending ? (this.getProgress(var1) - 1.0F) * (float) PistonBlockTextures.field3[this.storedOrientation] : (1.0F - this.getProgress(var1)) * (float) PistonBlockTextures.field3[this.storedOrientation];
    }

    @Side(CodeSide.CLIENT)
    public float method4(float var1) {
        return this.extending ? (this.getProgress(var1) - 1.0F) * (float) PistonBlockTextures.field4[this.storedOrientation] : (1.0F - this.getProgress(var1)) * (float) PistonBlockTextures.field4[this.storedOrientation];
    }

    private void method5(float var1, float var2) {
        if (!this.extending) {
            --var1;
        } else {
            var1 = 1.0F - var1;
        }

        AxisAlignedBB var3 = Block.PISTON_MOVING.method1(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.storedBlockID, var1, this.storedOrientation);
        if (var3 != null) {
            List<Entity> var4 = this.worldObj.getEntitiesWithinAABBExcludingEntity(null, var3);
            if (!var4.isEmpty()) {
                ENTITIES.addAll(var4);

                for (Entity var6 : ENTITIES) {
                    var6.moveEntity(var2 * (float) PistonBlockTextures.field2[this.storedOrientation], var2 * (float) PistonBlockTextures.field3[this.storedOrientation], var2 * (float) PistonBlockTextures.field4[this.storedOrientation]);
                }

                ENTITIES.clear();
            }
        }

    }

    public void clearPistonTileEntity() {
        if (this.lastProgress < 1.0F) {
            this.lastProgress = this.progress = 1.0F;
            this.worldObj.removeBlockTileEntity(this.xCoord, this.yCoord, this.zCoord);
            this.invalidate();
            if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord) == Block.PISTON_MOVING.blockID) {
                this.worldObj.setBlockAndMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.storedBlockID, this.storedMetadata);
            }
        }

    }

    @Override
    public void updateEntity() {
        this.lastProgress = this.progress;
        if (this.lastProgress >= 1.0F) {
            this.method5(1.0F, 0.25F);
            this.worldObj.removeBlockTileEntity(this.xCoord, this.yCoord, this.zCoord);
            this.invalidate();
            if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord) == Block.PISTON_MOVING.blockID) {
                this.worldObj.setBlockAndMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.storedBlockID, this.storedMetadata);
            }

        } else {
            this.progress += 0.5F;
            if (this.progress >= 1.0F) {
                this.progress = 1.0F;
            }

            if (this.extending) {
                this.method5(this.progress, this.progress - this.lastProgress + 0.0625F);
            }

        }
    }

    @Override
    public void readFromNBT(TagCompound tag) {
        super.readFromNBT(tag);
        this.storedBlockID = tag.getInteger("blockId");
        this.storedMetadata = tag.getInteger("blockData");
        this.storedOrientation = tag.getInteger("facing");
        this.lastProgress = this.progress = tag.getFloat("progress");
        this.extending = tag.getBoolean("extending");
    }

    @Override
    public void writeToNBT(TagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("blockId", this.storedBlockID);
        tag.setInteger("blockData", this.storedMetadata);
        tag.setInteger("facing", this.storedOrientation);
        tag.setFloat("progress", this.lastProgress);
        tag.setBoolean("extending", this.extending);
    }
}
