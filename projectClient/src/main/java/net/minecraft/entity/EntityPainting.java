package net.minecraft.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.material.Material;
import net.minecraft.nbt.TagCompound;
import net.minecraft.world.World;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class EntityPainting extends Entity {
    public int direction;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public EnumArt art;
    private int field_695_c;

    public EntityPainting(World var1) {
        super(var1);
        this.field_695_c = 0;
        this.direction = 0;
        this.yOffset = 0.0F;
        this.setSize(0.5F, 0.5F);
    }

    public EntityPainting(World var1, int var2, int var3, int var4, int var5) {
        this(var1);
        this.xPosition = var2;
        this.yPosition = var3;
        this.zPosition = var4;
        ArrayList var6 = new ArrayList();

        for (EnumArt var10 : EnumArt.values()) {
            this.art = var10;
            this.func_412_b(var5);
            if (this.func_410_i()) {
                var6.add(var10);
            }
        }

        if (var6.size() > 0) {
            this.art = (EnumArt) var6.get(this.rand.nextInt(var6.size()));
        }

        this.func_412_b(var5);
    }

    public EntityPainting(World var1, int var2, int var3, int var4, int var5, String var6) {
        this(var1);
        this.xPosition = var2;
        this.yPosition = var3;
        this.zPosition = var4;

        for (EnumArt var10 : EnumArt.values()) {
            if (var10.title.equals(var6)) {
                this.art = var10;
                break;
            }
        }

        this.func_412_b(var5);
    }

    @Override
    protected void entityInit() {
    }

    public void func_412_b(int var1) {
        this.direction = var1;
        this.prevRotationYaw = this.rotationYaw = (float) (var1 * 90);
        float var2 = (float) this.art.sizeX;
        float var3 = (float) this.art.sizeY;
        float var4 = (float) this.art.sizeX;
        if (var1 != 0 && var1 != 2) {
            var2 = 0.5F;
        } else {
            var4 = 0.5F;
        }

        var2 = var2 / 32.0F;
        var3 = var3 / 32.0F;
        var4 = var4 / 32.0F;
        float var5 = (float) this.xPosition + 0.5F;
        float var6 = (float) this.yPosition + 0.5F;
        float var7 = (float) this.zPosition + 0.5F;
        float var8 = 0.5625F;
        if (var1 == 0) {
            var7 -= var8;
        }

        if (var1 == 1) {
            var5 -= var8;
        }

        if (var1 == 2) {
            var7 += var8;
        }

        if (var1 == 3) {
            var5 += var8;
        }

        if (var1 == 0) {
            var5 -= this.func_411_c(this.art.sizeX);
        }

        if (var1 == 1) {
            var7 += this.func_411_c(this.art.sizeX);
        }

        if (var1 == 2) {
            var5 += this.func_411_c(this.art.sizeX);
        }

        if (var1 == 3) {
            var7 -= this.func_411_c(this.art.sizeX);
        }

        var6 = var6 + this.func_411_c(this.art.sizeY);
        this.setPosition(var5, var6, var7);
        float var9 = -0.00625F;
        this.boundingBox.setBounds(var5 - var2 - var9, var6 - var3 - var9, var7 - var4 - var9, var5 + var2 + var9, var6 + var3 + var9, var7 + var4 + var9);
    }

    private float func_411_c(int var1) {
        if (var1 == 32) {
            return 0.5F;
        } else {
            return var1 == 64 ? 0.5F : 0.0F;
        }
    }

    @Override
    public void onUpdate() {
        if (this.field_695_c++ == 100 && !this.worldObj.multiplayerWorld) {
            this.field_695_c = 0;
            if (!this.func_410_i()) {
                this.setEntityDead();
                this.worldObj.entityJoinedWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.PAINTING)));
            }
        }

    }

    public boolean func_410_i() {
        if (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() > 0) {
            return false;
        } else {
            int var1 = this.art.sizeX / 16;
            int var2 = this.art.sizeY / 16;
            int var3 = this.xPosition;
            int var4 = this.yPosition;
            int var5 = this.zPosition;
            if (this.direction == 0) {
                var3 = MathHelper.floor(this.posX - (double) ((float) this.art.sizeX / 32.0F));
            }

            if (this.direction == 1) {
                var5 = MathHelper.floor(this.posZ - (double) ((float) this.art.sizeX / 32.0F));
            }

            if (this.direction == 2) {
                var3 = MathHelper.floor(this.posX - (double) ((float) this.art.sizeX / 32.0F));
            }

            if (this.direction == 3) {
                var5 = MathHelper.floor(this.posZ - (double) ((float) this.art.sizeX / 32.0F));
            }

            var4 = MathHelper.floor(this.posY - (double) ((float) this.art.sizeY / 32.0F));

            for (int var6 = 0; var6 < var1; ++var6) {
                for (int var7 = 0; var7 < var2; ++var7) {
                    Material var8;
                    if (this.direction != 0 && this.direction != 2) {
                        var8 = this.worldObj.getBlockMaterial(this.xPosition, var4 + var7, var5 + var6);
                    } else {
                        var8 = this.worldObj.getBlockMaterial(var3 + var6, var4 + var7, this.zPosition);
                    }

                    if (!var8.isSolid()) {
                        return false;
                    }
                }
            }

            List var10 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox);

            for (int var11 = 0; var11 < var10.size(); ++var11) {
                if (var10.get(var11) instanceof EntityPainting) {
                    return false;
                }
            }

            return true;
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(Entity var1, int var2) {
        if (!this.isDead && !this.worldObj.multiplayerWorld) {
            this.setEntityDead();
            this.setBeenAttacked();
            this.worldObj.entityJoinedWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.PAINTING)));
        }

        return true;
    }

    @Override
    public void writeEntityToNBT(TagCompound var1) {
        var1.setByte("Dir", (byte) this.direction);
        var1.setString("Motive", this.art.title);
        var1.setInteger("TileX", this.xPosition);
        var1.setInteger("TileY", this.yPosition);
        var1.setInteger("TileZ", this.zPosition);
    }

    @Override
    public void readEntityFromNBT(TagCompound var1) {
        this.direction = var1.getByte("Dir");
        this.xPosition = var1.getInteger("TileX");
        this.yPosition = var1.getInteger("TileY");
        this.zPosition = var1.getInteger("TileZ");
        String var2 = var1.getString("Motive");

        for (EnumArt var6 : EnumArt.values()) {
            if (var6.title.equals(var2)) {
                this.art = var6;
            }
        }

        if (this.art == null) {
            this.art = EnumArt.KEBAB;
        }

        this.func_412_b(this.direction);
    }

    @Override
    public void moveEntity(double var1, double var3, double var5) {
        if (!this.worldObj.multiplayerWorld && var1 * var1 + var3 * var3 + var5 * var5 > 0.0D) {
            this.setEntityDead();
            this.worldObj.entityJoinedWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.PAINTING)));
        }

    }

    @Override
    public void addVelocity(double var1, double var3, double var5) {
        if (!this.worldObj.multiplayerWorld && var1 * var1 + var3 * var3 + var5 * var5 > 0.0D) {
            this.setEntityDead();
            this.worldObj.entityJoinedWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.PAINTING)));
        }

    }
}
