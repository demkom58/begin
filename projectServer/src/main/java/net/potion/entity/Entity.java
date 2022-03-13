package net.potion.entity;

import com.demkom58.timings.PotionTimings;
import com.demkom58.timings.Timing;
import net.hypnosis.entity.Rotatable;
import net.potion.block.Block;
import net.potion.block.BlockFluid;
import net.potion.block.StepSound;
import net.potion.entity.item.EntityItem;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.ItemStack;
import net.potion.material.Material;
import net.potion.nbt.TagCompound;
import net.potion.nbt.TagDouble;
import net.potion.nbt.TagFloat;
import net.potion.nbt.TagList;
import net.potion.util.AxisAlignedBB;
import net.potion.util.DataWatcher;
import net.hypnosis.util.math.MathHelper;
import net.potion.world.World;
import net.hypnosis.util.math.Vec3d;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public abstract class Entity implements Rotatable {
    private static final Logger LOGGER = Logger.getLogger(Entity.class.getName());

    public Timing tickTimer = PotionTimings.getEntityTimings(this);
    private static int nextEntityID = 0;
    public final AxisAlignedBB boundingBox;
    public int entityId;
    public double renderDistanceWeight;
    public boolean preventEntitySpawning;
    public Entity riddenByEntity;
    public Entity ridingEntity;
    public World worldObj;
    public double prevPosX;
    public double prevPosY;
    public double prevPosZ;
    public double posX;
    public double posY;
    public double posZ;
    public double motionX;
    public double motionY;
    public double motionZ;
    public float rotationYaw;
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;
    public boolean onGround;
    public boolean isCollidedHorizontally;
    public boolean isCollidedVertically;
    public boolean isCollided;
    public boolean beenAttacked;
    public boolean isInWeb;
    public boolean field_9077_F;
    public boolean isDead;
    public float yOffset;
    public float width;
    public float height;
    public float prevDistanceWalkedModified;
    public float distanceWalkedModified;
    public double lastTickPosX;
    public double lastTickPosY;
    public double lastTickPosZ;
    public float ySize;
    public float stepHeight;
    public boolean noClip;
    public float entityCollisionReduction;
    public int ticksExisted;
    public int fireResistance;
    public int fire;
    public int heartsLife;
    public int air;
    public float entityBrightness;
    public boolean addedToChunk;
    public int chunkCoordX;
    public int chunkCoordY;
    public int chunkCoordZ;
    public boolean ignoreFrustumCheck;
    protected float fallDistance;
    protected Random rand;
    protected int maxAir;
    protected boolean inWater;
    protected boolean isImmuneToFire;
    protected DataWatcher dataWatcher;
    private int nextStepDistance;
    private boolean firstUpdate;
    private double entityRiderPitchDelta;
    private double entityRiderYawDelta;

    public Entity(World world) {
        this.entityId = nextEntityID++;
        this.renderDistanceWeight = 1.0D;
        this.preventEntitySpawning = false;
        this.boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.onGround = false;
        this.isCollided = false;
        this.beenAttacked = false;
        this.field_9077_F = true;
        this.isDead = false;
        this.yOffset = 0.0F;
        this.width = 0.6F;
        this.height = 1.8F;
        this.prevDistanceWalkedModified = 0.0F;
        this.distanceWalkedModified = 0.0F;
        this.fallDistance = 0.0F;
        this.nextStepDistance = 1;
        this.ySize = 0.0F;
        this.stepHeight = 0.0F;
        this.noClip = false;
        this.entityCollisionReduction = 0.0F;
        this.rand = new Random();
        this.ticksExisted = 0;
        this.fireResistance = 1;
        this.fire = 0;
        this.maxAir = 300;
        this.inWater = false;
        this.heartsLife = 0;
        this.air = 300;
        this.firstUpdate = true;
        this.isImmuneToFire = false;
        this.dataWatcher = new DataWatcher();
        this.entityBrightness = 0.0F;
        this.addedToChunk = false;
        this.worldObj = world;
        this.setPosition(0.0D, 0.0D, 0.0D);
        this.dataWatcher.addObject(0, (byte) 0);
        this.entityInit();
    }

    protected abstract void entityInit();

    public DataWatcher getDataWatcher() {
        return this.dataWatcher;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Entity)
            return ((Entity) obj).entityId == this.entityId;

        return false;
    }

    public int hashCode() {
        return this.entityId;
    }

    public void setEntityDead() {
        this.isDead = true;
    }

    protected void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double getX() {
        return posX;
    }

    @Override
    public double getY() {
        return posY;
    }

    @Override
    public double getZ() {
        return posZ;
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        setYaw(yaw % 360.0F);
        setPitch(pitch % 360.0F);
    }

    @Override
    public float getYaw() {
        return rotationYaw;
    }

    @Override
    public void setYaw(float yaw) {
        if (!Float.isFinite(yaw)) {
            LOGGER.severe("Invalid entity rotation: " + yaw + ", discarding.");
        } else {
            this.rotationYaw = yaw;
        }
    }

    @Override
    public float getPitch() {
        return rotationPitch;
    }

    @Override
    public void setPitch(float pitch) {
        if (!Float.isFinite(pitch)) {
            LOGGER.severe("Invalid entity rotation: " + pitch + ", discarding.");
        } else {
            this.rotationPitch = pitch;
        }
    }

    @Override
    public final Vec3d getRotationVec(float tickDelta) {
        return this.getRotationVector(this.getPitch(tickDelta), this.getYaw(tickDelta));
    }

    @Override
    public float getPitch(float tickDelta) {
        return tickDelta == 1.0F ? this.getPitch() : MathHelper.lerp(tickDelta, this.prevRotationPitch, this.getPitch());
    }

    @Override
    public float getYaw(float tickDelta) {
        return tickDelta == 1.0F ? this.getYaw() : MathHelper.lerp(tickDelta, this.prevRotationYaw, this.getYaw());
    }

    protected final Vec3d getRotationVector(float pitch, float yaw) {
        float p = pitch * 0.017453292F;
        float y = -yaw * 0.017453292F;

        float cYaw = MathHelper.cos(y);
        float sYaw = MathHelper.sin(y);

        float cosPitch = MathHelper.cos(p);
        float sinPitch = MathHelper.sin(p);

        return new Vec3d(sYaw * cosPitch, -sinPitch, cYaw * cosPitch);
    }

    public void setPosition(double x, double y, double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        float var7 = this.width / 2.0F;
        float var8 = this.height;
        this.boundingBox.setBounds(x - (double) var7, y - (double) this.yOffset + (double) this.ySize, z - (double) var7, x + (double) var7, y - (double) this.yOffset + (double) this.ySize + (double) var8, z + (double) var7);
    }

    public void onUpdate() {
        this.onEntityUpdate();
    }

    public void onEntityUpdate() {
        if (this.ridingEntity != null && this.ridingEntity.isDead) {
            this.ridingEntity = null;
        }

        ++this.ticksExisted;
        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;
        if (this.handleWaterMovement()) {
            if (!this.inWater && !this.firstUpdate) {
                float var1 = MathHelper.sqrt(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.2F;
                if (var1 > 1.0F) {
                    var1 = 1.0F;
                }

                this.worldObj.playSoundAtEntity(this, "random.splash", var1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                float var2 = (float) MathHelper.floor(this.boundingBox.minY);

                for (int var3 = 0; (float) var3 < 1.0F + this.width * 20.0F; ++var3) {
                    float var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                    float var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                    this.worldObj.spawnParticle("bubble", this.posX + (double) var4, var2 + 1.0F, this.posZ + (double) var5, this.motionX, this.motionY - (double) (this.rand.nextFloat() * 0.2F), this.motionZ);
                }

                for (int var6 = 0; (float) var6 < 1.0F + this.width * 20.0F; ++var6) {
                    float var7 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                    float var8 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                    this.worldObj.spawnParticle("splash", this.posX + (double) var7, var2 + 1.0F, this.posZ + (double) var8, this.motionX, this.motionY, this.motionZ);
                }
            }

            this.fallDistance = 0.0F;
            this.inWater = true;
            this.fire = 0;
        } else {
            this.inWater = false;
        }

        if (this.worldObj.singleplayerWorld) {
            this.fire = 0;
        } else if (this.fire > 0) {
            if (this.isImmuneToFire) {
                this.fire -= 4;
                if (this.fire < 0) {
                    this.fire = 0;
                }
            } else {
                if (this.fire % 20 == 0) {
                    this.attackEntityFrom(null, 1);
                }

                --this.fire;
            }
        }

        if (this.handleLavaMovement()) {
            this.setOnFireFromLava();
        }

        if (this.posY < -64.0D) {
            this.kill();
        }

        if (!this.worldObj.singleplayerWorld) {
            this.setFlag(0, this.fire > 0);
            this.setFlag(2, this.ridingEntity != null);
        }

        this.firstUpdate = false;
    }

    protected void setOnFireFromLava() {
        if (!this.isImmuneToFire) {
            this.attackEntityFrom(null, 4);
            this.fire = 600;
        }

    }

    protected void kill() {
        this.setEntityDead();
    }

    public boolean isOffsetPositionInLiquid(double var1, double var3, double var5) {
        AxisAlignedBB var7 = this.boundingBox.getOffsetBoundingBox(var1, var3, var5);
        List<AxisAlignedBB> var8 = this.worldObj.getCollidingBoundingBoxes(this, var7);
        if (var8.size() > 0) {
            return false;
        }

        return !this.worldObj.isAnyLiquid(var7);
    }

    public void moveEntity(double x, double y, double z) {
        if (this.noClip) {
            this.boundingBox.offset(x, y, z);
            this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
            this.posY = this.boundingBox.minY + (double) this.yOffset - (double) this.ySize;
            this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
            return;
        }

        this.ySize *= 0.4F;
        final double copyX = this.posX;
        final double copyZ = this.posZ;

        if (this.isInWeb) {
            this.isInWeb = false;
            x *= 0.25D;
            y *= 0.05000000074505806D;
            z *= 0.25D;
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
        }

        double updX = x;
        double updY = y;
        double updZ = z;

        AxisAlignedBB var17 = this.boundingBox.copy();
        boolean var18 = this.onGround && this.isSneaking();
        if (var18) {
            double var19;
            for (var19 = 0.05D; x != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(x, -1.0D, 0.0D)).size() == 0; updX = x) {
                if (x < var19 && x >= -var19) {
                    x = 0.0D;
                } else if (x > 0.0D) {
                    x -= var19;
                } else {
                    x += var19;
                }
            }

            for (; z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(0.0D, -1.0D, z)).size() == 0; updZ = z) {
                if (z < var19 && z >= -var19) {
                    z = 0.0D;
                } else if (z > 0.0D) {
                    z -= var19;
                } else {
                    z += var19;
                }
            }
        }

        List<AxisAlignedBB> boundingBoxes = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(x, y, z));
        for (int i = 0; i < boundingBoxes.size(); ++i)
            y = boundingBoxes.get(i).calculateYOffset(this.boundingBox, y);

        this.boundingBox.offset(0.0D, y, 0.0D);
        if (!this.field_9077_F && updY != y) {
            z = 0.0D;
            y = 0.0D;
            x = 0.0D;
        }

        boolean var38 = this.onGround || updY != y && updY < 0.0D;

        for (int i = 0; i < boundingBoxes.size(); ++i)
            x = boundingBoxes.get(i).calculateXOffset(this.boundingBox, x);

        this.boundingBox.offset(x, 0.0D, 0.0D);
        if (!this.field_9077_F && updX != x) {
            z = 0.0D;
            y = 0.0D;
            x = 0.0D;
        }

        for (int i = 0; i < boundingBoxes.size(); ++i)
            z = boundingBoxes.get(i).calculateZOffset(this.boundingBox, z);

        this.boundingBox.offset(0.0D, 0.0D, z);
        if (!this.field_9077_F && updZ != z) {
            z = 0.0D;
            y = 0.0D;
            x = 0.0D;
        }

        if (this.stepHeight > 0.0F && var38 && (var18 || this.ySize < 0.05F) && (updX != x || updZ != z)) {
            double var40 = x;
            double var23 = y;
            double var25 = z;
            x = updX;
            y = this.stepHeight;
            z = updZ;
            AxisAlignedBB bbCopy = this.boundingBox.copy();
            this.boundingBox.setBB(var17);
            boundingBoxes = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(updX, y, updZ));

            for (int i = 0; i < boundingBoxes.size(); ++i)
                y = boundingBoxes.get(i).calculateYOffset(this.boundingBox, y);

            this.boundingBox.offset(0.0D, y, 0.0D);
            if (!this.field_9077_F && updY != y) {
                z = 0.0D;
                y = 0.0D;
                x = 0.0D;
            }

            for (int i = 0; i < boundingBoxes.size(); ++i)
                x = boundingBoxes.get(i).calculateXOffset(this.boundingBox, x);

            this.boundingBox.offset(x, 0.0D, 0.0D);
            if (!this.field_9077_F && updX != x) {
                z = 0.0D;
                y = 0.0D;
                x = 0.0D;
            }

            for (int i = 0; i < boundingBoxes.size(); ++i)
                z = boundingBoxes.get(i).calculateZOffset(this.boundingBox, z);

            this.boundingBox.offset(0.0D, 0.0D, z);
            if (!this.field_9077_F && updZ != z) {
                z = 0.0D;
                y = 0.0D;
                x = 0.0D;
            }

            if (!this.field_9077_F && updY != y) {
                z = 0.0D;
                y = 0.0D;
                x = 0.0D;
            } else {
                y = -this.stepHeight;

                for (int i = 0; i < boundingBoxes.size(); ++i)
                    y = boundingBoxes.get(i).calculateYOffset(this.boundingBox, y);

                this.boundingBox.offset(0.0D, y, 0.0D);
            }

            if (var40 * var40 + var25 * var25 >= x * x + z * z) {
                x = var40;
                y = var23;
                z = var25;
                this.boundingBox.setBB(bbCopy);
            } else {
                double var51 = this.boundingBox.minY - (double) ((int) this.boundingBox.minY);
                if (var51 > 0.0D) {
                    this.ySize = (float) ((double) this.ySize + var51 + 0.01D);
                }
            }
        }

        this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
        this.posY = this.boundingBox.minY + (double) this.yOffset - (double) this.ySize;
        this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
        this.isCollidedHorizontally = updX != x || updZ != z;
        this.isCollidedVertically = updY != y;
        this.onGround = updY != y && updY < 0.0D;
        this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
        this.updateFallState(y, this.onGround);

        if (updX != x)
            this.motionX = 0.0D;

        if (updY != y)
            this.motionY = 0.0D;

        if (updZ != z)
            this.motionZ = 0.0D;

        double var41 = this.posX - copyX;
        double var42 = this.posZ - copyZ;

        if (this.canTriggerWalking() && !var18 && this.ridingEntity == null) {
            this.distanceWalkedModified = (float) ((double) this.distanceWalkedModified + (double) MathHelper.sqrt(var41 * var41 + var42 * var42) * 0.6D);
            int blockX = MathHelper.floor(this.posX);
            int blockY = MathHelper.floor(this.posY - 0.20000000298023224D - (double) this.yOffset);
            int blockZ = MathHelper.floor(this.posZ);
            int walkBlockId = this.worldObj.getBlockId(blockX, blockY, blockZ);

            if (this.worldObj.getBlockId(blockX, blockY - 1, blockZ) == Block.FENCE.blockID)
                walkBlockId = this.worldObj.getBlockId(blockX, blockY - 1, blockZ);

            if (this.distanceWalkedModified > (float) this.nextStepDistance && walkBlockId > 0) {
                ++this.nextStepDistance;
                StepSound stepSound = Block.BLOCKS_LIST[walkBlockId].stepSound;
                if (this.worldObj.getBlockId(blockX, blockY + 1, blockZ) == Block.SNOW.blockID) {
                    stepSound = Block.SNOW.stepSound;
                    this.worldObj.playSoundAtEntity(this, stepSound.getFormattedName(), stepSound.getVolume() * 0.15F, stepSound.getPitch());
                } else if (!Block.BLOCKS_LIST[walkBlockId].blockMaterial.isLiquid()) {
                    this.worldObj.playSoundAtEntity(this, stepSound.getFormattedName(), stepSound.getVolume() * 0.15F, stepSound.getPitch());
                }

                Block.BLOCKS_LIST[walkBlockId].onEntityWalking(this.worldObj, blockX, blockY, blockZ, this);
            }
        }

        int minX = MathHelper.floor(this.boundingBox.minX + 0.001D);
        int minY = MathHelper.floor(this.boundingBox.minY + 0.001D);
        int minZ = MathHelper.floor(this.boundingBox.minZ + 0.001D);
        int maxX = MathHelper.floor(this.boundingBox.maxX - 0.001D);
        int maxY = MathHelper.floor(this.boundingBox.maxY - 0.001D);
        int maxZ = MathHelper.floor(this.boundingBox.maxZ - 0.001D);

        if (this.worldObj.checkChunksExist(minX, minY, minZ, maxX, maxY, maxZ)) {
            for (int iX = minX; iX <= maxX; ++iX) {
                for (int iY = minY; iY <= maxY; ++iY) {
                    for (int iZ = minZ; iZ <= maxZ; ++iZ) {
                        int blockId = this.worldObj.getBlockId(iX, iY, iZ);
                        if (blockId > 0)
                            Block.BLOCKS_LIST[blockId].onEntityCollidedWithBlock(this.worldObj, iX, iY, iZ, this);
                    }
                }
            }
        }

        boolean var56 = this.isWet();
        if (this.worldObj.isBoundingBoxBurning(this.boundingBox.getInsetBoundingBox(0.001D, 0.001D, 0.001D))) {
            this.dealFireDamage(1);
            if (!var56) {
                ++this.fire;
                if (this.fire == 0) {
                    this.fire = 300;
                }
            }
        } else if (this.fire <= 0) {
            this.fire = -this.fireResistance;
        }

        if (var56 && this.fire > 0) {
            this.worldObj.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
            this.fire = -this.fireResistance;
        }
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking() {
        return true;
    }

    protected void updateFallState(double var1, boolean var3) {
        if (var3) {
            if (this.fallDistance > 0.0F) {
                this.fall(this.fallDistance);
                this.fallDistance = 0.0F;
            }
        } else if (var1 < 0.0D) {
            this.fallDistance = (float) ((double) this.fallDistance - var1);
        }

    }

    public AxisAlignedBB getBoundingBox() {
        return null;
    }

    protected void dealFireDamage(int var1) {
        if (!this.isImmuneToFire) {
            this.attackEntityFrom(null, var1);
        }

    }

    protected void fall(float var1) {
        if (this.riddenByEntity != null)
            this.riddenByEntity.fall(var1);
    }

    public boolean isWet() {
        return this.inWater || this.worldObj.canLightningStrikeAt(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));
    }

    public boolean isInWater() {
        return this.inWater;
    }

    public boolean handleWaterMovement() {
        return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D).getInsetBoundingBox(0.001D, 0.001D, 0.001D), Material.WATER, this);
    }

    public boolean isInsideOfMaterial(Material var1) {
        double var2 = this.posY + (double) this.getEyeHeight();
        int var4 = MathHelper.floor(this.posX);
        int var5 = MathHelper.floor((float) MathHelper.floor(var2));
        int var6 = MathHelper.floor(this.posZ);
        int var7 = this.worldObj.getBlockId(var4, var5, var6);
        if (var7 != 0 && Block.BLOCKS_LIST[var7].blockMaterial == var1) {
            float var8 = BlockFluid.setFluidHeight(this.worldObj.getBlockMetadata(var4, var5, var6)) - 0.11111111F;
            float var9 = (float) (var5 + 1) - var8;
            return var2 < (double) var9;
        }

        return false;
    }

    public float getEyeHeight() {
        return 0.0F;
    }

    public boolean handleLavaMovement() {
        return this.worldObj.isMaterialInBB(this.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.LAVA);
    }

    public void moveFlying(float var1, float var2, float var3) {
        float var4 = MathHelper.sqrt(var1 * var1 + var2 * var2);
        if (var4 >= 0.01F) {
            if (var4 < 1.0F) {
                var4 = 1.0F;
            }

            var4 = var3 / var4;
            var1 = var1 * var4;
            var2 = var2 * var4;
            float var5 = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F);
            float var6 = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F);
            this.motionX += var1 * var6 - var2 * var5;
            this.motionZ += var2 * var6 + var1 * var5;
        }
    }

    public float getEntityBrightness(float var1) {
        int var2 = MathHelper.floor(this.posX);
        double var3 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66D;
        int var5 = MathHelper.floor(this.posY - (double) this.yOffset + var3);
        int var6 = MathHelper.floor(this.posZ);
        if (this.worldObj.checkChunksExist(MathHelper.floor(this.boundingBox.minX), MathHelper.floor(this.boundingBox.minY), MathHelper.floor(this.boundingBox.minZ), MathHelper.floor(this.boundingBox.maxX), MathHelper.floor(this.boundingBox.maxY), MathHelper.floor(this.boundingBox.maxZ))) {
            float var7 = this.worldObj.getLightBrightness(var2, var5, var6);
            if (var7 < this.entityBrightness) {
                var7 = this.entityBrightness;
            }

            return var7;
        }

        return this.entityBrightness;
    }

    public void setWorldHandler(World var1) {
        this.worldObj = var1;
    }

    public void setPositionAndRotation(double var1, double var3, double var5, float var7, float var8) {
        this.prevPosX = this.posX = var1;
        this.prevPosY = this.posY = var3;
        this.prevPosZ = this.posZ = var5;
        this.prevRotationYaw = this.rotationYaw = var7;
        this.prevRotationPitch = this.rotationPitch = var8;
        this.ySize = 0.0F;
        double var9 = this.prevRotationYaw - var7;
        if (var9 < -180.0D) {
            this.prevRotationYaw += 360.0F;
        }

        if (var9 >= 180.0D) {
            this.prevRotationYaw -= 360.0F;
        }

        this.setPosition(this.posX, this.posY, this.posZ);
        this.setRotation(var7, var8);
    }

    public void setLocationAndAngles(double var1, double var3, double var5, float var7, float var8) {
        this.lastTickPosX = this.prevPosX = this.posX = var1;
        this.lastTickPosY = this.prevPosY = this.posY = var3 + (double) this.yOffset;
        this.lastTickPosZ = this.prevPosZ = this.posZ = var5;
        this.rotationYaw = var7;
        this.rotationPitch = var8;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    public float getDistanceToEntity(Entity var1) {
        float var2 = (float) (this.posX - var1.posX);
        float var3 = (float) (this.posY - var1.posY);
        float var4 = (float) (this.posZ - var1.posZ);
        return MathHelper.sqrt(var2 * var2 + var3 * var3 + var4 * var4);
    }

    public double getDistanceSq(double var1, double var3, double var5) {
        double var7 = this.posX - var1;
        double var9 = this.posY - var3;
        double var11 = this.posZ - var5;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    public double getDistance(double var1, double var3, double var5) {
        double var7 = this.posX - var1;
        double var9 = this.posY - var3;
        double var11 = this.posZ - var5;
        return MathHelper.sqrt(var7 * var7 + var9 * var9 + var11 * var11);
    }

    public double getDistanceSqToEntity(Entity var1) {
        double var2 = this.posX - var1.posX;
        double var4 = this.posY - var1.posY;
        double var6 = this.posZ - var1.posZ;
        return var2 * var2 + var4 * var4 + var6 * var6;
    }

    public void onCollideWithPlayer(EntityPlayer var1) {
    }

    public void applyEntityCollision(Entity var1) {
        if (var1.riddenByEntity != this && var1.ridingEntity != this) {
            double var2 = var1.posX - this.posX;
            double var4 = var1.posZ - this.posZ;
            double var6 = MathHelper.absMax(var2, var4);
            if (var6 >= 0.009999999776482582D) {
                var6 = MathHelper.sqrt(var6);
                var2 = var2 / var6;
                var4 = var4 / var6;
                double var8 = 1.0D / var6;
                if (var8 > 1.0D) {
                    var8 = 1.0D;
                }

                var2 = var2 * var8;
                var4 = var4 * var8;
                var2 = var2 * 0.05000000074505806D;
                var4 = var4 * 0.05000000074505806D;
                var2 = var2 * (double) (1.0F - this.entityCollisionReduction);
                var4 = var4 * (double) (1.0F - this.entityCollisionReduction);
                this.addVelocity(-var2, 0.0D, -var4);
                var1.addVelocity(var2, 0.0D, var4);
            }

        }
    }

    public void addVelocity(double var1, double var3, double var5) {
        this.motionX += var1;
        this.motionY += var3;
        this.motionZ += var5;
    }

    protected void setBeenAttacked() {
        this.beenAttacked = true;
    }

    public boolean attackEntityFrom(Entity var1, int var2) {
        this.setBeenAttacked();
        return false;
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean canBePushed() {
        return false;
    }

    public void addToPlayerScore(Entity var1, int var2) {
    }

    public boolean addEntityID(TagCompound var1) {
        String var2 = this.getEntityString();
        if (!this.isDead && var2 != null) {
            var1.setString("id", var2);
            this.writeToNBT(var1);
            return true;
        } else {
            return false;
        }
    }

    public void writeToNBT(TagCompound var1) {
        var1.setTag("Pos", this.newDoubleNBTList(this.posX, this.posY + (double) this.ySize, this.posZ));
        var1.setTag("Motion", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
        var1.setTag("Rotation", this.newFloatNBTList(this.rotationYaw, this.rotationPitch));
        var1.setFloat("FallDistance", this.fallDistance);
        var1.setShort("Fire", (short) this.fire);
        var1.setShort("Air", (short) this.air);
        var1.setBoolean("OnGround", this.onGround);
        this.writeEntityToNBT(var1);
    }

    public void readFromNBT(TagCompound var1) {
        TagList var2 = var1.getTagList("Pos");
        TagList var3 = var1.getTagList("Motion");
        TagList var4 = var1.getTagList("Rotation");
        this.motionX = ((TagDouble) var3.tagAt(0)).doubleValue;
        this.motionY = ((TagDouble) var3.tagAt(1)).doubleValue;
        this.motionZ = ((TagDouble) var3.tagAt(2)).doubleValue;
        if (Math.abs(this.motionX) > 10.0D) {
            this.motionX = 0.0D;
        }

        if (Math.abs(this.motionY) > 10.0D) {
            this.motionY = 0.0D;
        }

        if (Math.abs(this.motionZ) > 10.0D) {
            this.motionZ = 0.0D;
        }

        this.prevPosX = this.lastTickPosX = this.posX = ((TagDouble) var2.tagAt(0)).doubleValue;
        this.prevPosY = this.lastTickPosY = this.posY = ((TagDouble) var2.tagAt(1)).doubleValue;
        this.prevPosZ = this.lastTickPosZ = this.posZ = ((TagDouble) var2.tagAt(2)).doubleValue;
        this.prevRotationYaw = this.rotationYaw = ((TagFloat) var4.tagAt(0)).floatValue;
        this.prevRotationPitch = this.rotationPitch = ((TagFloat) var4.tagAt(1)).floatValue;
        this.fallDistance = var1.getFloat("FallDistance");
        this.fire = var1.getShort("Fire");
        this.air = var1.getShort("Air");
        this.onGround = var1.getBoolean("OnGround");
        this.setPosition(this.posX, this.posY, this.posZ);
        this.setRotation(this.rotationYaw, this.rotationPitch);
        this.readEntityFromNBT(var1);
    }

    protected final String getEntityString() {
        return EntityList.getEntityString(this);
    }

    protected abstract void readEntityFromNBT(TagCompound var1);

    protected abstract void writeEntityToNBT(TagCompound var1);

    protected TagList newDoubleNBTList(double... var1) {
        TagList var2 = new TagList();

        for (double var6 : var1) {
            var2.setTag(new TagDouble(var6));
        }

        return var2;
    }

    protected TagList newFloatNBTList(float... var1) {
        TagList var2 = new TagList();

        for (float var6 : var1) {
            var2.setTag(new TagFloat(var6));
        }

        return var2;
    }

    public EntityItem dropItem(int var1, int var2) {
        return this.dropItemWithOffset(var1, var2, 0.0F);
    }

    public EntityItem dropItemWithOffset(int var1, int var2, float var3) {
        return this.entityDropItem(new ItemStack(var1, var2, 0), var3);
    }

    public EntityItem entityDropItem(ItemStack var1, float var2) {
        EntityItem var3 = new EntityItem(this.worldObj, this.posX, this.posY + (double) var2, this.posZ, var1);
        var3.delayBeforeCanPickup = 10;
        this.worldObj.entityJoinedWorld(var3);
        return var3;
    }

    public boolean isEntityAlive() {
        return !this.isDead;
    }

    public boolean isEntityInsideOpaqueBlock() {
        for (int var1 = 0; var1 < 8; ++var1) {
            float var2 = ((float) ((var1) % 2) - 0.5F) * this.width * 0.9F;
            float var3 = ((float) ((var1 >> 1) % 2) - 0.5F) * 0.1F;
            float var4 = ((float) ((var1 >> 2) % 2) - 0.5F) * this.width * 0.9F;
            int var5 = MathHelper.floor(this.posX + (double) var2);
            int var6 = MathHelper.floor(this.posY + (double) this.getEyeHeight() + (double) var3);
            int var7 = MathHelper.floor(this.posZ + (double) var4);
            if (this.worldObj.isBlockNormalCube(var5, var6, var7)) {
                return true;
            }
        }

        return false;
    }

    public boolean interact(EntityPlayer var1) {
        return false;
    }

    public AxisAlignedBB func_89_d(Entity var1) {
        return null;
    }

    public void updateRidden() {
        if (this.ridingEntity.isDead) {
            this.ridingEntity = null;
        } else {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.onUpdate();
            if (this.ridingEntity != null) {
                this.ridingEntity.updateRiderPosition();
                this.entityRiderYawDelta += this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw;

                for (this.entityRiderPitchDelta += this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch; this.entityRiderYawDelta >= 180.0D; this.entityRiderYawDelta -= 360.0D) {
                }

                while (this.entityRiderYawDelta < -180.0D) {
                    this.entityRiderYawDelta += 360.0D;
                }

                while (this.entityRiderPitchDelta >= 180.0D) {
                    this.entityRiderPitchDelta -= 360.0D;
                }

                while (this.entityRiderPitchDelta < -180.0D) {
                    this.entityRiderPitchDelta += 360.0D;
                }

                double var1 = this.entityRiderYawDelta * 0.5D;
                double var3 = this.entityRiderPitchDelta * 0.5D;
                float var5 = 10.0F;
                if (var1 > (double) var5) {
                    var1 = var5;
                }

                if (var1 < (double) (-var5)) {
                    var1 = -var5;
                }

                if (var3 > (double) var5) {
                    var3 = var5;
                }

                if (var3 < (double) (-var5)) {
                    var3 = -var5;
                }

                this.entityRiderYawDelta -= var1;
                this.entityRiderPitchDelta -= var3;
                this.rotationYaw = (float) ((double) this.rotationYaw + var1);
                this.rotationPitch = (float) ((double) this.rotationPitch + var3);
            }
        }
    }

    public void updateRiderPosition() {
        this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
    }

    public double getYOffset() {
        return this.yOffset;
    }

    public double getMountedYOffset() {
        return (double) this.height * 0.75D;
    }

    public void mountEntity(Entity var1) {
        this.entityRiderPitchDelta = 0.0D;
        this.entityRiderYawDelta = 0.0D;
        if (var1 == null) {
            if (this.ridingEntity != null) {
                this.setLocationAndAngles(this.ridingEntity.posX, this.ridingEntity.boundingBox.minY + (double) this.ridingEntity.height, this.ridingEntity.posZ, this.rotationYaw, this.rotationPitch);
                this.ridingEntity.riddenByEntity = null;
            }

            this.ridingEntity = null;
        } else if (this.ridingEntity == var1) {
            this.ridingEntity.riddenByEntity = null;
            this.ridingEntity = null;
            this.setLocationAndAngles(var1.posX, var1.boundingBox.minY + (double) var1.height, var1.posZ, this.rotationYaw, this.rotationPitch);
        } else {
            if (this.ridingEntity != null) {
                this.ridingEntity.riddenByEntity = null;
            }

            if (var1.riddenByEntity != null) {
                var1.riddenByEntity.ridingEntity = null;
            }

            this.ridingEntity = var1;
            var1.riddenByEntity = this;
        }
    }

    public Vec3d getLookVec() {
        return null;
    }

    public void setInPortal() {
    }

    public ItemStack[] getInventory() {
        return null;
    }

    public boolean isSneaking() {
        return this.getFlag(1);
    }

    public void setSneaking(boolean var1) {
        this.setFlag(1, var1);
    }

    protected boolean getFlag(int var1) {
        return (this.dataWatcher.getWatchableObjectByte(0) & 1 << var1) != 0;
    }

    protected void setFlag(int var1, boolean var2) {
        byte var3 = this.dataWatcher.getWatchableObjectByte(0);
        if (var2) {
            this.dataWatcher.updateObject(0, (byte) (var3 | 1 << var1));
        } else {
            this.dataWatcher.updateObject(0, (byte) (var3 & ~(1 << var1)));
        }

    }

    public void onStruckByLightning(EntityLightningBolt var1) {
        this.dealFireDamage(5);
        ++this.fire;
        if (this.fire == 0) {
            this.fire = 300;
        }

    }

    public void func_27010_a(EntityLiving var1) {
    }

    protected boolean func_28005_g(double var1, double var3, double var5) {
        int var7 = MathHelper.floor(var1);
        int var8 = MathHelper.floor(var3);
        int var9 = MathHelper.floor(var5);
        double var10 = var1 - (double) var7;
        double var12 = var3 - (double) var8;
        double var14 = var5 - (double) var9;
        if (this.worldObj.isBlockNormalCube(var7, var8, var9)) {
            boolean var16 = !this.worldObj.isBlockNormalCube(var7 - 1, var8, var9);
            boolean var17 = !this.worldObj.isBlockNormalCube(var7 + 1, var8, var9);
            boolean var18 = !this.worldObj.isBlockNormalCube(var7, var8 - 1, var9);
            boolean var19 = !this.worldObj.isBlockNormalCube(var7, var8 + 1, var9);
            boolean var20 = !this.worldObj.isBlockNormalCube(var7, var8, var9 - 1);
            boolean var21 = !this.worldObj.isBlockNormalCube(var7, var8, var9 + 1);
            byte var22 = -1;
            double var23 = 9999.0D;
            if (var16 && var10 < var23) {
                var23 = var10;
                var22 = 0;
            }

            if (var17 && 1.0D - var10 < var23) {
                var23 = 1.0D - var10;
                var22 = 1;
            }

            if (var18 && var12 < var23) {
                var23 = var12;
                var22 = 2;
            }

            if (var19 && 1.0D - var12 < var23) {
                var23 = 1.0D - var12;
                var22 = 3;
            }

            if (var20 && var14 < var23) {
                var23 = var14;
                var22 = 4;
            }

            if (var21 && 1.0D - var14 < var23) {
                var23 = 1.0D - var14;
                var22 = 5;
            }

            float var25 = this.rand.nextFloat() * 0.2F + 0.1F;
            if (var22 == 0) {
                this.motionX = -var25;
            }

            if (var22 == 1) {
                this.motionX = var25;
            }

            if (var22 == 2) {
                this.motionY = -var25;
            }

            if (var22 == 3) {
                this.motionY = var25;
            }

            if (var22 == 4) {
                this.motionZ = -var25;
            }

            if (var22 == 5) {
                this.motionZ = var25;
            }
        }

        return false;
    }
}
