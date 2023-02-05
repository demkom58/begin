package net.minecraft.entity;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRail;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.TagCompound;
import net.minecraft.nbt.TagList;
import net.minecraft.util.AxisAlignedBB;
import net.hypnosis.util.math.MathHelper;
import net.minecraft.world.World;
import net.hypnosis.util.math.Vec3d;

import java.util.List;

public class EntityMinecart extends Entity implements IInventory {
    private static final int[][][] FIELD1 = new int[][][]{{{0, 0, -1}, {0, 0, 1}}, {{-1, 0, 0}, {1, 0, 0}}, {{-1, -1, 0}, {1, 0, 0}}, {{-1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, {-1, 0, 0}}, {{0, 0, -1}, {-1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
    public int currentDamage;
    public int timeSinceHit;
    public int rockDirection;
    public int minecartType;
    public int fuel;
    public double pushX;
    public double pushZ;
    private ItemStack[] cargoItems;
    private boolean field2;
    private int field3;
    private double prevX;
    private double prevY;
    private double prevZ;
    private double field4;
    private double field5;

    private double field6;
    private double field7;
    private double field8;

    public EntityMinecart(World var1) {
        super(var1);
        this.cargoItems = new ItemStack[36];
        this.currentDamage = 0;
        this.timeSinceHit = 0;
        this.rockDirection = 1;
        this.field2 = false;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.7F);
        this.yOffset = this.height / 2.0F;
    }

    public EntityMinecart(World var1, double var2, double var4, double var6, int var8) {
        this(var1);
        this.setPosition(var2, var4 + (double) this.yOffset, var6);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = var2;
        this.prevPosY = var4;
        this.prevPosZ = var6;
        this.minecartType = var8;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity var1) {
        return var1.boundingBox;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    public double getMountedYOffset() {
        return (double) this.height * 0.0D - 0.30000001192092896D;
    }

    @Override
    public boolean attackEntityFrom(Entity var1, int var2) {
        if (this.world.localWorld || this.isDead) {
            return true;
        }

        this.rockDirection = -this.rockDirection;
        this.timeSinceHit = 10;
        this.setBeenAttacked();
        this.currentDamage += var2 * 10;
        if (this.currentDamage > 40) {
            if (this.riddenByEntity != null) {
                this.riddenByEntity.mountEntity(this);
            }

            this.setEntityDead();
            this.dropItemWithOffset(Item.MINECART.shiftedIndex, 1, 0.0F);
            if (this.minecartType == 1) {
                EntityMinecart var3 = this;

                for (int var4 = 0; var4 < var3.getSizeInventory(); ++var4) {
                    ItemStack var5 = var3.getStackInSlot(var4);
                    if (var5 != null) {
                        float var6 = this.rand.nextFloat() * 0.8F + 0.1F;
                        float var7 = this.rand.nextFloat() * 0.8F + 0.1F;
                        float var8 = this.rand.nextFloat() * 0.8F + 0.1F;

                        while (var5.stackSize > 0) {
                            int var9 = this.rand.nextInt(21) + 10;
                            if (var9 > var5.stackSize) {
                                var9 = var5.stackSize;
                            }

                            var5.stackSize -= var9;
                            EntityItem var10 = new EntityItem(this.world, this.posX + (double) var6, this.posY + (double) var7, this.posZ + (double) var8, new ItemStack(var5.itemID, var9, var5.getItemDamage()));
                            float var11 = 0.05F;
                            var10.motionX = (float) this.rand.nextGaussian() * var11;
                            var10.motionY = (float) this.rand.nextGaussian() * var11 + 0.2F;
                            var10.motionZ = (float) this.rand.nextGaussian() * var11;
                            this.world.entityJoinedWorld(var10);
                        }
                    }
                }

                this.dropItemWithOffset(Block.CHEST.blockID, 1, 0.0F);
            } else if (this.minecartType == 2) {
                this.dropItemWithOffset(Block.FURNACE.blockID, 1, 0.0F);
            }
        }

        return true;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public void performHurtAnimation() {
        System.out.println("Animating hurt");
        this.rockDirection = -this.rockDirection;
        this.timeSinceHit = 10;
        this.currentDamage += this.currentDamage * 10;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public void setEntityDead() {
        for (int var1 = 0; var1 < this.getSizeInventory(); ++var1) {
            ItemStack var2 = this.getStackInSlot(var1);
            if (var2 == null) {
                continue;
            }

            float var3 = this.rand.nextFloat() * 0.8F + 0.1F;
            float var4 = this.rand.nextFloat() * 0.8F + 0.1F;
            float var5 = this.rand.nextFloat() * 0.8F + 0.1F;

            while (var2.stackSize > 0) {
                int var6 = this.rand.nextInt(21) + 10;
                if (var6 > var2.stackSize) {
                    var6 = var2.stackSize;
                }

                var2.stackSize -= var6;
                EntityItem var7 = new EntityItem(this.world, this.posX + (double) var3, this.posY + (double) var4, this.posZ + (double) var5, new ItemStack(var2.itemID, var6, var2.getItemDamage()));
                float var8 = 0.05F;
                var7.motionX = (float) this.rand.nextGaussian() * var8;
                var7.motionY = (float) this.rand.nextGaussian() * var8 + 0.2F;
                var7.motionZ = (float) this.rand.nextGaussian() * var8;
                this.world.entityJoinedWorld(var7);
            }
        }

        super.setEntityDead();
    }

    @Override
    public void onUpdate() {
        if (this.timeSinceHit > 0) {
            --this.timeSinceHit;
        }

        if (this.currentDamage > 0) {
            --this.currentDamage;
        }

        if (this.world.localWorld && this.field3 > 0) {
            if (this.field3 > 0) {
                double var46 = this.posX + (this.prevX - this.posX) / (double) this.field3;
                double var47 = this.posY + (this.prevY - this.posY) / (double) this.field3;
                double var5 = this.posZ + (this.prevZ - this.posZ) / (double) this.field3;

                double var48 = this.field4 - (double) this.rotationYaw;
                while (var48 < -180.0D) {
                    var48 += 360.0D;
                }

                while (var48 >= 180.0D) {
                    var48 -= 360.0D;
                }

                this.rotationYaw = (float) ((double) this.rotationYaw + var48 / (double) this.field3);
                this.rotationPitch = (float) ((double) this.rotationPitch + (this.field5 - (double) this.rotationPitch) / (double) this.field3);
                --this.field3;
                this.setPosition(var46, var47, var5);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            } else {
                this.setPosition(this.posX, this.posY, this.posZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }

        } else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= 0.03999999910593033D;
            int var1 = MathHelper.floor(this.posX);
            int var2 = MathHelper.floor(this.posY);
            int var3 = MathHelper.floor(this.posZ);
            if (BlockRail.isRailBlockAt(this.world, var1, var2 - 1, var3)) {
                --var2;
            }

            double var4 = 0.4D;
            boolean var6 = false;
            double var7 = 0.0078125D;
            int var9 = this.world.getBlockId(var1, var2, var3);
            if (BlockRail.isRailBlock(var9)) {
                Vec3d var10 = this.method2(this.posX, this.posY, this.posZ);
                int var11 = this.world.getBlockMetadata(var1, var2, var3);
                this.posY = var2;
                boolean var12 = false;
                boolean var13 = false;
                if (var9 == Block.RAIL_POWERED.blockID) {
                    var12 = (var11 & 8) != 0;
                    var13 = !var12;
                }

                if (((BlockRail) Block.BLOCKS_LIST[var9]).getIsPowered()) {
                    var11 &= 7;
                }

                if (var11 >= 2 && var11 <= 5) {
                    this.posY = var2 + 1;
                }

                if (var11 == 2) {
                    this.motionX -= var7;
                }

                if (var11 == 3) {
                    this.motionX += var7;
                }

                if (var11 == 4) {
                    this.motionZ += var7;
                }

                if (var11 == 5) {
                    this.motionZ -= var7;
                }

                int[][] var14 = FIELD1[var11];
                double var15 = var14[1][0] - var14[0][0];
                double var17 = var14[1][2] - var14[0][2];
                double var19 = Math.sqrt(var15 * var15 + var17 * var17);
                double var21 = this.motionX * var15 + this.motionZ * var17;
                if (var21 < 0.0D) {
                    var15 = -var15;
                    var17 = -var17;
                }

                double var23 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.motionX = var23 * var15 / var19;
                this.motionZ = var23 * var17 / var19;
                if (var13) {
                    double var25 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    if (var25 < 0.03D) {
                        this.motionX *= 0.0D;
                        this.motionY *= 0.0D;
                        this.motionZ *= 0.0D;
                    } else {
                        this.motionX *= 0.5D;
                        this.motionY *= 0.0D;
                        this.motionZ *= 0.5D;
                    }
                }

                double var57 = 0.0D;
                double var27 = (double) var1 + 0.5D + (double) var14[0][0] * 0.5D;
                double var29 = (double) var3 + 0.5D + (double) var14[0][2] * 0.5D;
                double var31 = (double) var1 + 0.5D + (double) var14[1][0] * 0.5D;
                double var33 = (double) var3 + 0.5D + (double) var14[1][2] * 0.5D;
                var15 = var31 - var27;
                var17 = var33 - var29;
                if (var15 == 0.0D) {
                    this.posX = (double) var1 + 0.5D;
                    var57 = this.posZ - (double) var3;
                } else if (var17 == 0.0D) {
                    this.posZ = (double) var3 + 0.5D;
                    var57 = this.posX - (double) var1;
                } else {
                    double var35 = this.posX - var27;
                    double var37 = this.posZ - var29;
                    var57 = (var35 * var15 + var37 * var17) * 2.0D;
                }

                this.posX = var27 + var15 * var57;
                this.posZ = var29 + var17 * var57;
                this.setPosition(this.posX, this.posY + (double) this.yOffset, this.posZ);
                double var59 = this.motionX;
                double var60 = this.motionZ;
                if (this.riddenByEntity != null) {
                    var59 *= 0.75D;
                    var60 *= 0.75D;
                }

                if (var59 < -var4) {
                    var59 = -var4;
                }

                if (var59 > var4) {
                    var59 = var4;
                }

                if (var60 < -var4) {
                    var60 = -var4;
                }

                if (var60 > var4) {
                    var60 = var4;
                }

                this.moveEntity(var59, 0.0D, var60);
                if (var14[0][1] != 0 && MathHelper.floor(this.posX) - var1 == var14[0][0] && MathHelper.floor(this.posZ) - var3 == var14[0][2]) {
                    this.setPosition(this.posX, this.posY + (double) var14[0][1], this.posZ);
                } else if (var14[1][1] != 0 && MathHelper.floor(this.posX) - var1 == var14[1][0] && MathHelper.floor(this.posZ) - var3 == var14[1][2]) {
                    this.setPosition(this.posX, this.posY + (double) var14[1][1], this.posZ);
                }

                if (this.riddenByEntity != null) {
                    this.motionX *= 0.996999979019165D;
                    this.motionY *= 0.0D;
                    this.motionZ *= 0.996999979019165D;
                } else {
                    if (this.minecartType == 2) {
                        double var61 = MathHelper.sqrt(this.pushX * this.pushX + this.pushZ * this.pushZ);
                        if (var61 > 0.01D) {
                            var6 = true;
                            this.pushX /= var61;
                            this.pushZ /= var61;
                            double var41 = 0.04D;
                            this.motionX *= 0.800000011920929D;
                            this.motionY *= 0.0D;
                            this.motionZ *= 0.800000011920929D;
                            this.motionX += this.pushX * var41;
                            this.motionZ += this.pushZ * var41;
                        } else {
                            this.motionX *= 0.8999999761581421D;
                            this.motionY *= 0.0D;
                            this.motionZ *= 0.8999999761581421D;
                        }
                    }

                    this.motionX *= 0.9599999785423279D;
                    this.motionY *= 0.0D;
                    this.motionZ *= 0.9599999785423279D;
                }

                Vec3d var62 = this.method2(this.posX, this.posY, this.posZ);
                if (var62 != null && var10 != null) {
                    double var40 = (var10.y - var62.y) * 0.05D;
                    var23 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    if (var23 > 0.0D) {
                        this.motionX = this.motionX / var23 * (var23 + var40);
                        this.motionZ = this.motionZ / var23 * (var23 + var40);
                    }

                    this.setPosition(this.posX, var62.y, this.posZ);
                }

                int var63 = MathHelper.floor(this.posX);
                int var64 = MathHelper.floor(this.posZ);
                if (var63 != var1 || var64 != var3) {
                    var23 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    this.motionX = var23 * (double) (var63 - var1);
                    this.motionZ = var23 * (double) (var64 - var3);
                }

                if (this.minecartType == 2) {
                    double var42 = MathHelper.sqrt(this.pushX * this.pushX + this.pushZ * this.pushZ);
                    if (var42 > 0.01D && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001D) {
                        this.pushX /= var42;
                        this.pushZ /= var42;
                        if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0D) {
                            this.pushX = 0.0D;
                            this.pushZ = 0.0D;
                        } else {
                            this.pushX = this.motionX;
                            this.pushZ = this.motionZ;
                        }
                    }
                }

                if (var12) {
                    double var65 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    if (var65 > 0.01D) {
                        double var44 = 0.06D;
                        this.motionX += this.motionX / var65 * var44;
                        this.motionZ += this.motionZ / var65 * var44;
                    } else if (var11 == 1) {
                        if (this.world.isBlockNormalCube(var1 - 1, var2, var3)) {
                            this.motionX = 0.02D;
                        } else if (this.world.isBlockNormalCube(var1 + 1, var2, var3)) {
                            this.motionX = -0.02D;
                        }
                    } else if (var11 == 0) {
                        if (this.world.isBlockNormalCube(var1, var2, var3 - 1)) {
                            this.motionZ = 0.02D;
                        } else if (this.world.isBlockNormalCube(var1, var2, var3 + 1)) {
                            this.motionZ = -0.02D;
                        }
                    }
                }
            } else {
                if (this.motionX < -var4) {
                    this.motionX = -var4;
                }

                if (this.motionX > var4) {
                    this.motionX = var4;
                }

                if (this.motionZ < -var4) {
                    this.motionZ = -var4;
                }

                if (this.motionZ > var4) {
                    this.motionZ = var4;
                }

                if (this.onGround) {
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;
                }

                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                if (!this.onGround) {
                    this.motionX *= 0.949999988079071D;
                    this.motionY *= 0.949999988079071D;
                    this.motionZ *= 0.949999988079071D;
                }
            }

            this.rotationPitch = 0.0F;
            double var49 = this.prevPosX - this.posX;
            double var50 = this.prevPosZ - this.posZ;
            if (var49 * var49 + var50 * var50 > 0.001D) {
                this.rotationYaw = (float) (Math.atan2(var50, var49) * 180.0D / Math.PI);
                if (this.field2) {
                    this.rotationYaw += 180.0F;
                }
            }

            double var51;
            var51 = this.rotationYaw - this.prevRotationYaw;
            while (var51 >= 180.0D) {
                var51 -= 360.0D;
            }

            while (var51 < -180.0D) {
                var51 += 360.0D;
            }

            if (var51 < -170.0D || var51 >= 170.0D) {
                this.rotationYaw += 180.0F;
                this.field2 = !this.field2;
            }

            this.setRotation(this.rotationYaw, this.rotationPitch);
            List<Entity> var16 = this.world.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
            if (var16 != null && var16.size() > 0) {
                for (int var54 = 0; var54 < var16.size(); ++var54) {
                    Entity var18 = (Entity) var16.get(var54);
                    if (var18 != this.riddenByEntity && var18.canBePushed() && var18 instanceof EntityMinecart) {
                        var18.applyEntityCollision(this);
                    }
                }
            }

            if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
                this.riddenByEntity = null;
            }

            if (var6 && this.rand.nextInt(4) == 0) {
                --this.fuel;
                if (this.fuel < 0) {
                    this.pushX = this.pushZ = 0.0D;
                }

                this.world.spawnParticle("largesmoke", this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D);
            }

        }
    }

    @Side(CodeSide.CLIENT)
    public Vec3d method1(double var1, double var3, double var5, double var7) {
        int var9 = MathHelper.floor(var1);
        int var10 = MathHelper.floor(var3);
        int var11 = MathHelper.floor(var5);
        if (BlockRail.isRailBlockAt(this.world, var9, var10 - 1, var11)) {
            --var10;
        }

        int var12 = this.world.getBlockId(var9, var10, var11);
        if (!BlockRail.isRailBlock(var12)) {
            return null;
        } else {
            int var13 = this.world.getBlockMetadata(var9, var10, var11);
            if (((BlockRail) Block.BLOCKS_LIST[var12]).getIsPowered()) {
                var13 &= 7;
            }

            var3 = var10;
            if (var13 >= 2 && var13 <= 5) {
                var3 = var10 + 1;
            }

            int[][] var14 = FIELD1[var13];
            double var15 = var14[1][0] - var14[0][0];
            double var17 = var14[1][2] - var14[0][2];
            double var19 = Math.sqrt(var15 * var15 + var17 * var17);
            var15 = var15 / var19;
            var17 = var17 / var19;
            var1 = var1 + var15 * var7;
            var5 = var5 + var17 * var7;
            if (var14[0][1] != 0 && MathHelper.floor(var1) - var9 == var14[0][0] && MathHelper.floor(var5) - var11 == var14[0][2]) {
                var3 += var14[0][1];
            } else if (var14[1][1] != 0 && MathHelper.floor(var1) - var9 == var14[1][0] && MathHelper.floor(var5) - var11 == var14[1][2]) {
                var3 += var14[1][1];
            }

            return this.method2(var1, var3, var5);
        }
    }

    public Vec3d method2(double var1, double var3, double var5) {
        int var7 = MathHelper.floor(var1);
        int var8 = MathHelper.floor(var3);
        int var9 = MathHelper.floor(var5);
        if (BlockRail.isRailBlockAt(this.world, var7, var8 - 1, var9)) {
            --var8;
        }

        int var10 = this.world.getBlockId(var7, var8, var9);
        if (BlockRail.isRailBlock(var10)) {
            int var11 = this.world.getBlockMetadata(var7, var8, var9);
            var3 = var8;
            if (((BlockRail) Block.BLOCKS_LIST[var10]).getIsPowered()) {
                var11 &= 7;
            }

            if (var11 >= 2 && var11 <= 5) {
                var3 = var8 + 1;
            }

            int[][] var12 = FIELD1[var11];
            double var13 = 0.0D;
            double var15 = (double) var7 + 0.5D + (double) var12[0][0] * 0.5D;
            double var17 = (double) var8 + 0.5D + (double) var12[0][1] * 0.5D;
            double var19 = (double) var9 + 0.5D + (double) var12[0][2] * 0.5D;
            double var21 = (double) var7 + 0.5D + (double) var12[1][0] * 0.5D;
            double var23 = (double) var8 + 0.5D + (double) var12[1][1] * 0.5D;
            double var25 = (double) var9 + 0.5D + (double) var12[1][2] * 0.5D;
            double var27 = var21 - var15;
            double var29 = (var23 - var17) * 2.0D;
            double var31 = var25 - var19;
            if (var27 == 0.0D) {
                var1 = (double) var7 + 0.5D;
                var13 = var5 - (double) var9;
            } else if (var31 == 0.0D) {
                var5 = (double) var9 + 0.5D;
                var13 = var1 - (double) var7;
            } else {
                double var33 = var1 - var15;
                double var35 = var5 - var19;
                var13 = (var33 * var27 + var35 * var31) * 2.0D;
            }

            var1 = var15 + var27 * var13;
            var3 = var17 + var29 * var13;
            var5 = var19 + var31 * var13;
            if (var29 < 0.0D) {
                ++var3;
            }

            if (var29 > 0.0D) {
                var3 += 0.5D;
            }

            return new Vec3d(var1, var3, var5);
        } else {
            return null;
        }
    }

    @Override
    protected void writeEntityToNBT(TagCompound var1) {
        var1.setInteger("Type", this.minecartType);
        if (this.minecartType == 2) {
            var1.setDouble("PushX", this.pushX);
            var1.setDouble("PushZ", this.pushZ);
            var1.setShort("Fuel", (short) this.fuel);
        } else if (this.minecartType == 1) {
            TagList var2 = new TagList();

            for (int var3 = 0; var3 < this.cargoItems.length; ++var3) {
                if (this.cargoItems[var3] != null) {
                    TagCompound var4 = new TagCompound();
                    var4.setByte("Slot", (byte) var3);
                    this.cargoItems[var3].writeToNBT(var4);
                    var2.setTag(var4);
                }
            }

            var1.setTag("Items", var2);
        }

    }

    @Override
    protected void readEntityFromNBT(TagCompound var1) {
        this.minecartType = var1.getInteger("Type");
        if (this.minecartType == 2) {
            this.pushX = var1.getDouble("PushX");
            this.pushZ = var1.getDouble("PushZ");
            this.fuel = var1.getShort("Fuel");
        } else if (this.minecartType == 1) {
            TagList var2 = var1.getTagList("Items");
            this.cargoItems = new ItemStack[this.getSizeInventory()];

            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                TagCompound var4 = (TagCompound) var2.tagAt(var3);
                int var5 = var4.getByte("Slot") & 255;
                if (var5 >= 0 && var5 < this.cargoItems.length) {
                    this.cargoItems[var5] = new ItemStack(var4);
                }
            }
        }

    }

    @Override
    @Side(CodeSide.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    @Override
    public void applyEntityCollision(Entity var1) {
        if (this.world.localWorld) {
            return;
        }

        if (var1 != this.riddenByEntity) {
            if (var1 instanceof EntityLiving && !(var1 instanceof EntityPlayer) && this.minecartType == 0 && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01D && this.riddenByEntity == null && var1.ridingEntity == null) {
                var1.mountEntity(this);
            }

            double var2 = var1.posX - this.posX;
            double var4 = var1.posZ - this.posZ;
            double var6 = var2 * var2 + var4 * var4;
            if (var6 >= 9.999999747378752E-5D) {
                var6 = MathHelper.sqrt(var6);
                var2 = var2 / var6;
                var4 = var4 / var6;
                double var8 = 1.0D / var6;
                if (var8 > 1.0D) {
                    var8 = 1.0D;
                }

                var2 = var2 * var8;
                var4 = var4 * var8;
                var2 = var2 * 0.10000000149011612D;
                var4 = var4 * 0.10000000149011612D;
                var2 = var2 * (double) (1.0F - this.entityCollisionReduction);
                var4 = var4 * (double) (1.0F - this.entityCollisionReduction);
                var2 = var2 * 0.5D;
                var4 = var4 * 0.5D;
                if (var1 instanceof EntityMinecart) {
                    double var10 = var1.posX - this.posX;
                    double var12 = var1.posZ - this.posZ;
                    double var14 = var10 * var1.motionZ + var12 * var1.prevPosX;
                    var14 = var14 * var14;
                    if (var14 > 5.0D) {
                        return;
                    }

                    double var16 = var1.motionX + this.motionX;
                    double var18 = var1.motionZ + this.motionZ;
                    if (((EntityMinecart) var1).minecartType == 2 && this.minecartType != 2) {
                        this.motionX *= 0.20000000298023224D;
                        this.motionZ *= 0.20000000298023224D;
                        this.addVelocity(var1.motionX - var2, 0.0D, var1.motionZ - var4);
                        var1.motionX *= 0.699999988079071D;
                        var1.motionZ *= 0.699999988079071D;
                    } else if (((EntityMinecart) var1).minecartType != 2 && this.minecartType == 2) {
                        var1.motionX *= 0.20000000298023224D;
                        var1.motionZ *= 0.20000000298023224D;
                        var1.addVelocity(this.motionX + var2, 0.0D, this.motionZ + var4);
                        this.motionX *= 0.699999988079071D;
                        this.motionZ *= 0.699999988079071D;
                    } else {
                        var16 = var16 / 2.0D;
                        var18 = var18 / 2.0D;
                        this.motionX *= 0.20000000298023224D;
                        this.motionZ *= 0.20000000298023224D;
                        this.addVelocity(var16 - var2, 0.0D, var18 - var4);
                        var1.motionX *= 0.20000000298023224D;
                        var1.motionZ *= 0.20000000298023224D;
                        var1.addVelocity(var16 + var2, 0.0D, var18 + var4);
                    }
                } else {
                    this.addVelocity(-var2, 0.0D, -var4);
                    var1.addVelocity(var2 / 4.0D, 0.0D, var4 / 4.0D);
                }
            }

        }
    }

    @Override
    public int getSizeInventory() {
        return 27;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return this.cargoItems[var1];
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2) {
        if (this.cargoItems[var1] != null) {
            if (this.cargoItems[var1].stackSize <= var2) {
                ItemStack var4 = this.cargoItems[var1];
                this.cargoItems[var1] = null;
                return var4;
            } else {
                ItemStack var3 = this.cargoItems[var1].splitStack(var2);
                if (this.cargoItems[var1].stackSize == 0) {
                    this.cargoItems[var1] = null;
                }

                return var3;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        this.cargoItems[var1] = var2;
        if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
            var2.stackSize = this.getInventoryStackLimit();
        }

    }

    @Override
    public String getInvName() {
        return "Minecart";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void onInventoryChanged() {
    }

    @Override
    public boolean interact(EntityPlayer var1) {
        if (this.minecartType == 0) {
            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != var1) {
                return true;
            }

            if (!this.world.localWorld) {
                var1.mountEntity(this);
            }
        } else if (this.minecartType == 1) {
            if (!this.world.localWorld) {
                var1.displayGUIChest(this);
            }
        } else if (this.minecartType == 2) {
            ItemStack var2 = var1.inventory.getCurrentItem();
            if (var2 != null && var2.itemID == Item.COAL.shiftedIndex) {
                if (--var2.stackSize == 0) {
                    var1.inventory.setInventorySlotContents(var1.inventory.currentItem, null);
                }

                this.fuel += 1200;
            }

            this.pushX = this.posX - var1.posX;
            this.pushZ = this.posZ - var1.posZ;
        }

        return true;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9) {
        this.prevX = var1;
        this.prevY = var3;
        this.prevZ = var5;
        this.field4 = var7;
        this.field5 = var8;
        this.field3 = var9 + 2;
        this.motionX = this.field6;
        this.motionY = this.field7;
        this.motionZ = this.field8;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public void setVelocity(double var1, double var3, double var5) {
        this.field6 = this.motionX = var1;
        this.field7 = this.motionY = var3;
        this.field8 = this.motionZ = var5;
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1) {
        if (this.isDead) {
            return false;
        } else {
            return var1.getDistanceSqToEntity(this) <= 64.0D;
        }
    }
}
