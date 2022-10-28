package net.potion.entity.player;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.hypnosis.util.math.MathConstants;
import net.potion.achievement.AchievementList;
import net.potion.block.Block;
import net.potion.block.BlockBed;
import net.potion.block.EnumBedStatus;
import net.potion.entity.Entity;
import net.potion.entity.EntityLiving;
import net.potion.entity.EntityMinecart;
import net.potion.entity.EntityMob;
import net.potion.entity.item.EntityBoat;
import net.potion.entity.item.EntityItem;
import net.potion.entity.monster.EntityCreeper;
import net.potion.entity.monster.EntityGhast;
import net.potion.entity.passive.EntityFish;
import net.potion.entity.passive.EntityPig;
import net.potion.entity.passive.EntityWolf;
import net.potion.entity.projectile.EntityArrow;
import net.potion.inventory.Container;
import net.potion.inventory.ContainerPlayer;
import net.potion.inventory.IInventory;
import net.potion.inventory.InventoryPlayer;
import net.potion.item.Item;
import net.potion.item.ItemStack;
import net.potion.material.Material;
import net.potion.nbt.TagCompound;
import net.potion.nbt.TagList;
import net.potion.stats.StatBase;
import net.potion.stats.StatList;
import net.potion.tileentity.TileEntityDispenser;
import net.potion.tileentity.TileEntityFurnace;
import net.potion.tileentity.TileEntitySign;
import net.potion.util.AxisAlignedBB;
import net.hypnosis.util.math.MathHelper;
import net.potion.world.World;
import net.potion.world.chunk.ChunkCoordinates;
import net.potion.world.chunk.IChunkProvider;

import java.util.List;

public abstract class EntityPlayer extends EntityLiving {
    public InventoryPlayer inventory = new InventoryPlayer(this);
    public Container inventorySlots;
    public Container craftingInventory;
    public byte field20 = 0;
    public int score = 0;
    public float prevCameraYaw;
    public float cameraYaw;
    public boolean isSwinging = false;
    public int swingProgressInt = 0;
    public String username;
    public int dimension;
    @Side(CodeSide.CLIENT)
    public String playerCloakUrl;
    public double prevChasingPosX;
    public double prevChasingPosY;
    public double prevChasingPosZ;
    public double chasingPosX;
    public double chasingPosY;
    public double chasingPosZ;
    public ChunkCoordinates bedChunkCoordinates;
    public float renderOffsetX;
    @Side(CodeSide.CLIENT)
    public float renderOffsetY;
    public float renderOffsetZ;
    public int timeUntilPortal = 20;
    public float timeInPortal;
    public float prevTimeInPortal;
    public EntityFish fishEntity = null;
    protected boolean sleeping;
    protected boolean inPortal = false;
    private int sleepTimer;
    private ChunkCoordinates playerSpawnCoordinate;
    private ChunkCoordinates startMinecartRidingCoordinate;
    private int damageRemainder = 0;

    public EntityPlayer(World var1) {
        super(var1);
        this.inventorySlots = new ContainerPlayer(this.inventory, !var1.localWorld);
        this.craftingInventory = this.inventorySlots;
        this.yOffset = 1.62F;
        ChunkCoordinates var2 = var1.getSpawnPoint();
        this.setLocationAndAngles((double) var2.x + 0.5D, var2.y + 1, (double) var2.z + 0.5D, 0.0F, 0.0F);
        this.health = 20;
        this.entityType = "humanoid";
        this.field14 = 180.0F;
        this.fireResistance = 20;
        this.texture = "/mob/char.png";
    }

    public static ChunkCoordinates func_25060_a(World world, ChunkCoordinates coord) {
        IChunkProvider provider = world.getIChunkProvider();
        provider.prepareChunk(coord.x - 3 >> 4, coord.z - 3 >> 4);
        provider.prepareChunk(coord.x + 3 >> 4, coord.z - 3 >> 4);
        provider.prepareChunk(coord.x - 3 >> 4, coord.z + 3 >> 4);
        provider.prepareChunk(coord.x + 3 >> 4, coord.z + 3 >> 4);

        if (world.getBlockId(coord.x, coord.y, coord.z) != Block.BED.blockID)
            return null;

        return BlockBed.getNearestEmptyChunkCoordinates(world, coord.x, coord.y, coord.z, 0);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte) 0);
    }

    @Override
    public void onUpdate() {
        if (this.isSleeping()) {
            ++this.sleepTimer;
            if (this.sleepTimer > 100)
                this.sleepTimer = 100;

            if (!this.world.localWorld) {
                if (!this.isInBed()) {
                    this.wakeUpPlayer(true, true, false);
                } else if (this.world.isDaytime()) {
                    this.wakeUpPlayer(false, true, true);
                }
            }
        } else if (this.sleepTimer > 0) {
            ++this.sleepTimer;
            if (this.sleepTimer >= 110)
                this.sleepTimer = 0;
        }

        super.onUpdate();
        if (!this.world.localWorld && this.craftingInventory != null && !this.craftingInventory.isUsableByPlayer(this)) {
            this.closeScreen();
            this.craftingInventory = this.inventorySlots;
        }

        this.prevChasingPosX = this.chasingPosX;
        this.prevChasingPosY = this.chasingPosY;
        this.prevChasingPosZ = this.chasingPosZ;
        double var1 = this.posX - this.chasingPosX;
        double var3 = this.posY - this.chasingPosY;
        double var5 = this.posZ - this.chasingPosZ;
        double var7 = 10.0D;

        if (var1 > var7)
            this.prevChasingPosX = this.chasingPosX = this.posX;

        if (var5 > var7)
            this.prevChasingPosZ = this.chasingPosZ = this.posZ;

        if (var3 > var7)
            this.prevChasingPosY = this.chasingPosY = this.posY;

        if (var1 < -var7)
            this.prevChasingPosX = this.chasingPosX = this.posX;

        if (var5 < -var7)
            this.prevChasingPosZ = this.chasingPosZ = this.posZ;

        if (var3 < -var7)
            this.prevChasingPosY = this.chasingPosY = this.posY;

        this.chasingPosX += var1 * 0.25D;
        this.chasingPosZ += var5 * 0.25D;
        this.chasingPosY += var3 * 0.25D;

        this.addStat(StatList.minutesPlayedStat, 1);
        if (this.ridingEntity == null) {
            this.startMinecartRidingCoordinate = null;
        }

    }

    @Override
    protected boolean isMovementBlocked() {
        return this.health <= 0 || this.isSleeping();
    }

    protected void closeScreen() {
        this.craftingInventory = this.inventorySlots;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public void updateCloak() {
        this.playerCloakUrl = "http://s3.amazonaws.com/MinecraftCloaks/" + this.username + ".png";
        this.cloakUrl = this.playerCloakUrl;
    }

    @Override
    public void updateRidden() {
        double var1 = this.posX;
        double var3 = this.posY;
        double var5 = this.posZ;
        super.updateRidden();
        this.prevCameraYaw = this.cameraYaw;
        this.cameraYaw = 0.0F;
        this.addMountedMovementStat(this.posX - var1, this.posY - var3, this.posZ - var5);
    }

    @Override
    @Side(CodeSide.CLIENT)
    public void preparePlayerToSpawn() {
        this.yOffset = 1.62F;
        this.setSize(0.6F, 1.8F);
        super.preparePlayerToSpawn();
        this.health = 20;
        this.deathTime = 0;
    }

    @Override
    protected void updatePlayerActionState() {
        if (this.isSwinging) {
            ++this.swingProgressInt;
            if (this.swingProgressInt >= 8) {
                this.swingProgressInt = 0;
                this.isSwinging = false;
            }
        } else {
            this.swingProgressInt = 0;
        }

        this.swingProgress = (float) this.swingProgressInt / 8.0F;
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.difficultySetting == 0 && this.health < 20 && this.ticksExisted % 20 * 12 == 0) {
            this.heal(1);
        }

        this.inventory.decrementAnimations();
        this.prevCameraYaw = this.cameraYaw;
        super.onLivingUpdate();
        float var1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float var2 = (float) Math.atan(-this.motionY * 0.20000000298023224D) * 15.0F;
        if (var1 > 0.1F) {
            var1 = 0.1F;
        }

        if (!this.onGround || this.health <= 0) {
            var1 = 0.0F;
        }

        if (this.onGround || this.health <= 0) {
            var2 = 0.0F;
        }

        this.cameraYaw += (var1 - this.cameraYaw) * 0.4F;
        this.cameraPitch += (var2 - this.cameraPitch) * 0.8F;
        if (this.health > 0) {
            List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0D, 0.0D, 1.0D));
            if (entities == null) {
                return;
            }

            for (int i = 0; i < entities.size(); ++i) {
                Entity entity = entities.get(i);
                if (!entity.isDead) {
                    this.collideWithPlayer(entity);
                }
            }
        }

    }

    private void collideWithPlayer(Entity var1) {
        var1.onCollideWithPlayer(this);
    }

    public int getScore() {
        return this.score;
    }

    @Override
    public void onDeath(Entity var1) {
        super.onDeath(var1);
        this.setSize(0.2F, 0.2F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionY = 0.10000000149011612D;
        if (this.username.equals("Notch")) {
            this.dropPlayerItemWithRandomChoice(new ItemStack(Item.APPLE_RED, 1), true);
        }

        this.inventory.dropAllItems();
        if (var1 != null) {
            this.motionX = -MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * MathConstants.PI / 180.0F) * 0.1F;
            this.motionZ = -MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * MathConstants.PI / 180.0F) * 0.1F;
        } else {
            this.motionX = this.motionZ = 0.0D;
        }

        this.yOffset = 0.1F;
        this.addStat(StatList.deathsStat, 1);
    }

    @Override
    public void addToPlayerScore(Entity var1, int var2) {
        this.score += var2;
        if (var1 instanceof EntityPlayer) {
            this.addStat(StatList.playerKillsStat, 1);
        } else {
            this.addStat(StatList.mobKillsStat, 1);
        }

    }

    public void dropCurrentItem() {
        this.dropPlayerItemWithRandomChoice(this.inventory.decrStackSize(this.inventory.currentItem, 1), false);
    }

    public void dropPlayerItem(ItemStack var1) {
        this.dropPlayerItemWithRandomChoice(var1, false);
    }

    public void dropPlayerItemWithRandomChoice(ItemStack stack, boolean var2) {
        if (stack == null) {
            return;
        }

        EntityItem item = new EntityItem(this.world, this.posX, this.posY - 0.30000001192092896D + (double) this.getEyeHeight(), this.posZ, stack);
        item.delayBeforeCanPickup = 40;
        float var4 = 0.1F;
        if (var2) {
            float var5 = this.rand.nextFloat() * 0.5F;
            float var6 = this.rand.nextFloat() * MathConstants.PI * 2.0F;
            item.motionX = -MathHelper.sin(var6) * var5;
            item.motionZ = MathHelper.cos(var6) * var5;
            item.motionY = 0.20000000298023224D;
        } else {
            var4 = 0.3F;
            item.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * MathConstants.PI) * MathHelper.cos(this.rotationPitch / 180.0F * MathConstants.PI) * var4;
            item.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * MathConstants.PI) * MathHelper.cos(this.rotationPitch / 180.0F * MathConstants.PI) * var4;
            item.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * MathConstants.PI) * var4 + 0.1F;
            var4 = 0.02F;
            float var10 = this.rand.nextFloat() * MathConstants.PI * 2.0F;
            var4 = var4 * this.rand.nextFloat();
            item.motionX += Math.cos(var10) * (double) var4;
            item.motionY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
            item.motionZ += Math.sin(var10) * (double) var4;
        }

        this.joinEntityItemWithWorld(item);
        this.addStat(StatList.dropStat, 1);
    }

    protected void joinEntityItemWithWorld(EntityItem var1) {
        this.world.entityJoinedWorld(var1);
    }

    public float getCurrentPlayerStrVsBlock(Block var1) {
        float var2 = this.inventory.getStrVsBlock(var1);
        if (this.isInsideOfMaterial(Material.WATER))
            var2 /= 5.0F;

        if (!this.onGround)
            var2 /= 5.0F;

        return var2;
    }

    public boolean canHarvestBlock(Block var1) {
        return this.inventory.canHarvestBlock(var1);
    }

    @Override
    public void readEntityFromNBT(TagCompound var1) {
        super.readEntityFromNBT(var1);
        TagList var2 = var1.getTagList("Inventory");
        this.inventory.readFromNBT(var2);
        this.dimension = var1.getInteger("Dimension");
        this.sleeping = var1.getBoolean("Sleeping");
        this.sleepTimer = var1.getShort("SleepTimer");
        if (this.sleeping) {
            this.bedChunkCoordinates = new ChunkCoordinates(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));
            this.wakeUpPlayer(true, true, false);
        }

        if (var1.hasKey("SpawnX") && var1.hasKey("SpawnY") && var1.hasKey("SpawnZ")) {
            this.playerSpawnCoordinate = new ChunkCoordinates(var1.getInteger("SpawnX"), var1.getInteger("SpawnY"), var1.getInteger("SpawnZ"));
        }

    }

    @Override
    public void writeEntityToNBT(TagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("Inventory", this.inventory.writeToNBT(new TagList()));
        compound.setInteger("Dimension", this.dimension);
        compound.setBoolean("Sleeping", this.sleeping);
        compound.setShort("SleepTimer", (short) this.sleepTimer);
        if (this.playerSpawnCoordinate != null) {
            compound.setInteger("SpawnX", this.playerSpawnCoordinate.x);
            compound.setInteger("SpawnY", this.playerSpawnCoordinate.y);
            compound.setInteger("SpawnZ", this.playerSpawnCoordinate.z);
        }

    }

    public void displayGUIChest(IInventory var1) {
    }

    public void displayWorkbenchGUI(int var1, int var2, int var3) {
    }

    public void onItemPickup(Entity var1, int var2) {
    }

    @Override
    public float getEyeHeight() {
        return 0.12F;
    }

    protected void resetHeight() {
        this.yOffset = 1.62F;
    }

    @Override
    public boolean attackEntityFrom(Entity var1, int var2) {
        this.entityAge = 0;
        if (this.health <= 0)
            return false;

        if (this.isSleeping() && !this.world.localWorld) {
            this.wakeUpPlayer(true, true, false);
        }

        if (var1 instanceof EntityMob || var1 instanceof EntityArrow) {
            if (this.world.difficultySetting == 0) {
                var2 = 0;
            }

            if (this.world.difficultySetting == 1) {
                var2 = var2 / 3 + 1;
            }

            if (this.world.difficultySetting == 3) {
                var2 = var2 * 3 / 2;
            }
        }

        if (var2 == 0)
            return false;

        Object var3 = var1;
        if (var1 instanceof EntityArrow && ((EntityArrow) var1).owner != null) {
            var3 = ((EntityArrow) var1).owner;
        }

        if (var3 instanceof EntityLiving) {
            this.alertWolves((EntityLiving) var3, false);
        }

        this.addStat(StatList.damageTakenStat, var2);
        return super.attackEntityFrom(var1, var2);
    }

    protected boolean isPvpEnabled() {
        return false;
    }

    protected void alertWolves(EntityLiving var1, boolean var2) {
        if (!(var1 instanceof EntityCreeper) && !(var1 instanceof EntityGhast)) {
            if (var1 instanceof EntityWolf) {
                EntityWolf var3 = (EntityWolf) var1;
                if (var3.isWolfTamed() && this.username.equals(var3.getOwner())) {
                    return;
                }
            }

            if (!(var1 instanceof EntityPlayer) || this.isPvpEnabled()) {
                for (Entity var5 : this.world.getEntitiesWithinAABB(EntityWolf.class, AxisAlignedBB.getBoundingBoxFromPool(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D).expand(16.0D, 4.0D, 16.0D))) {
                    EntityWolf var6 = (EntityWolf) var5;
                    if (var6.isWolfTamed() && var6.getTarget() == null && this.username.equals(var6.getOwner()) && (!var2 || !var6.isSitting())) {
                        var6.setSitting(false);
                        var6.setTarget(var1);
                    }
                }

            }
        }
    }

    @Override
    protected void damageEntity(int var1) {
        int var2 = 25 - this.inventory.getTotalArmorValue();
        int var3 = var1 * var2 + this.damageRemainder;
        this.inventory.damageArmor(var1);
        var1 = var3 / 25;
        this.damageRemainder = var3 % 25;
        super.damageEntity(var1);
    }

    public void displayGUIFurnace(TileEntityFurnace var1) {
    }

    public void displayGUIDispenser(TileEntityDispenser var1) {
    }

    public void displayGUIEditSign(TileEntitySign var1) {
    }

    public void useCurrentItemOnEntity(Entity entity) {
        if (!entity.interact(this)) {
            ItemStack handItem = this.getCurrentEquippedItem();
            if (handItem != null && entity instanceof EntityLiving) {
                handItem.useItemOnEntity((EntityLiving) entity);
                if (handItem.stackSize <= 0) {
                    handItem.onItemDestroyedByUse(this);
                    this.destroyCurrentEquippedItem();
                }
            }

        }
    }

    public ItemStack getCurrentEquippedItem() {
        return this.inventory.getCurrentItem();
    }

    public void destroyCurrentEquippedItem() {
        this.inventory.setInventorySlotContents(this.inventory.currentItem, null);
    }

    @Override
    public double getYOffset() {
        return this.yOffset - 0.5F;
    }

    public void swingItem() {
        this.swingProgressInt = -1;
        this.isSwinging = true;
    }

    public void attackTargetEntityWithCurrentItem(Entity entity) {
        int damage = this.inventory.getDamageVsEntity(entity);
        if (damage <= 0) {
            return;
        }

        if (this.motionY < 0.0D) {
            ++damage;
        }

        entity.attackEntityFrom(this, damage);
        ItemStack stack = this.getCurrentEquippedItem();
        if (stack != null && entity instanceof EntityLiving) {
            stack.hitEntity((EntityLiving) entity, this);
            if (stack.stackSize <= 0) {
                stack.onItemDestroyedByUse(this);
                this.destroyCurrentEquippedItem();
            }
        }

        if (entity instanceof EntityLiving) {
            if (entity.isEntityAlive())
                this.alertWolves((EntityLiving) entity, true);

            this.addStat(StatList.damageDealtStat, damage);
        }
    }

    @Side(CodeSide.CLIENT)
    public void respawnPlayer() {
    }

    @Side(CodeSide.CLIENT)
    public void playRespawnAnimation() {

    }

    public void onItemStackChanged(ItemStack var1) {
    }

    @Override
    public void setEntityDead() {
        super.setEntityDead();
        this.inventorySlots.onCraftGuiClosed(this);
        if (this.craftingInventory != null) {
            this.craftingInventory.onCraftGuiClosed(this);
        }

    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return !this.sleeping && super.isEntityInsideOpaqueBlock();
    }

    public EnumBedStatus sleepInBedAt(int var1, int var2, int var3) {
        if (!this.world.localWorld) {
            if (this.isSleeping() || !this.isEntityAlive()) {
                return EnumBedStatus.OTHER_PROBLEM;
            }

            if (this.world.worldProvider.isNether) {
                return EnumBedStatus.NOT_POSSIBLE_HERE;
            }

            if (this.world.isDaytime()) {
                return EnumBedStatus.NOT_POSSIBLE_NOW;
            }

            if (Math.abs(this.posX - (double) var1) > 3.0D || Math.abs(this.posY - (double) var2) > 2.0D || Math.abs(this.posZ - (double) var3) > 3.0D) {
                return EnumBedStatus.TOO_FAR_AWAY;
            }
        }

        this.setSize(0.2F, 0.2F);
        this.yOffset = 0.2F;
        if (this.world.blockExists(var1, var2, var3)) {
            int var4 = this.world.getBlockMetadata(var1, var2, var3);
            int var5 = BlockBed.getDirectionFromMetadata(var4);
            float var6 = 0.5F;
            float var7 = 0.5F;
            switch (var5) {
                case 0 -> var7 = 0.9F;
                case 1 -> var6 = 0.1F;
                case 2 -> var7 = 0.1F;
                case 3 -> var6 = 0.9F;
            }

            this.func_22052_e(var5);
            this.setPosition((float) var1 + var6, (float) var2 + 0.9375F, (float) var3 + var7);
        } else {
            this.setPosition((float) var1 + 0.5F, (float) var2 + 0.9375F, (float) var3 + 0.5F);
        }

        this.sleeping = true;
        this.sleepTimer = 0;
        this.bedChunkCoordinates = new ChunkCoordinates(var1, var2, var3);
        this.motionX = this.motionZ = this.motionY = 0.0D;
        if (!this.world.localWorld) {
            this.world.updateAllPlayersSleepingFlag();
        }

        return EnumBedStatus.OK;
    }

    private void func_22052_e(int var1) {
        this.renderOffsetX = 0.0F;
        this.renderOffsetZ = 0.0F;
        switch (var1) {
            case 0 -> this.renderOffsetZ = -1.8F;
            case 1 -> this.renderOffsetX = 1.8F;
            case 2 -> this.renderOffsetZ = 1.8F;
            case 3 -> this.renderOffsetX = -1.8F;
        }

    }

    public void wakeUpPlayer(boolean var1, boolean var2, boolean var3) {
        this.setSize(0.6F, 1.8F);
        this.resetHeight();
        ChunkCoordinates var4 = this.bedChunkCoordinates;
        ChunkCoordinates var5 = this.bedChunkCoordinates;
        if (var4 != null && this.world.getBlockId(var4.x, var4.y, var4.z) == Block.BED.blockID) {
            BlockBed.setBedOccupied(this.world, var4.x, var4.y, var4.z, false);
            var5 = BlockBed.getNearestEmptyChunkCoordinates(this.world, var4.x, var4.y, var4.z, 0);
            if (var5 == null) {
                var5 = new ChunkCoordinates(var4.x, var4.y + 1, var4.z);
            }

            this.setPosition((float) var5.x + 0.5F, (float) var5.y + this.yOffset + 0.1F, (float) var5.z + 0.5F);
        }

        this.sleeping = false;
        if (!this.world.localWorld && var2) {
            this.world.updateAllPlayersSleepingFlag();
        }

        if (var1) {
            this.sleepTimer = 0;
        } else {
            this.sleepTimer = 100;
        }

        if (var3) {
            this.setPlayerSpawnCoordinate(this.bedChunkCoordinates);
        }

    }

    private boolean isInBed() {
        return this.world.getBlockId(this.bedChunkCoordinates.x, this.bedChunkCoordinates.y, this.bedChunkCoordinates.z) == Block.BED.blockID;
    }

    @Side(CodeSide.CLIENT)
    public float getBedOrientationInDegrees() {
        if (this.bedChunkCoordinates != null) {
            int var1 = this.world.getBlockMetadata(this.bedChunkCoordinates.x, this.bedChunkCoordinates.y, this.bedChunkCoordinates.z);
            int var2 = BlockBed.getDirectionFromMetadata(var1);
            switch (var2) {
                case 0:
                    return 90.0F;
                case 1:
                    return 0.0F;
                case 2:
                    return 270.0F;
                case 3:
                    return 180.0F;
            }
        }

        return 0.0F;
    }

    @Override
    public boolean isSleeping() {
        return this.sleeping;
    }

    public boolean isPlayerFullyAsleep() {
        return this.sleeping && this.sleepTimer >= 100;
    }

    @Side(CodeSide.CLIENT)
    public int getSleepTimer() {
        return this.sleepTimer;
    }

    public void addChatMessage(String var1) {
    }

    public ChunkCoordinates getPlayerSpawnCoordinate() {
        return this.playerSpawnCoordinate;
    }

    public void setPlayerSpawnCoordinate(ChunkCoordinates var1) {
        if (var1 != null) {
            this.playerSpawnCoordinate = new ChunkCoordinates(var1);
        } else {
            this.playerSpawnCoordinate = null;
        }

    }

    public void triggerAchievement(StatBase var1) {
        this.addStat(var1, 1);
    }

    public void addStat(StatBase var1, int var2) {
    }

    @Override
    protected void jump() {
        super.jump();
        this.addStat(StatList.jumpStat, 1);
    }

    @Override
    public void moveEntityWithHeading(float var1, float var2) {
        double var3 = this.posX;
        double var5 = this.posY;
        double var7 = this.posZ;
        super.moveEntityWithHeading(var1, var2);
        this.addMovementStat(this.posX - var3, this.posY - var5, this.posZ - var7);
    }

    private void addMovementStat(double var1, double var3, double var5) {
        if (this.ridingEntity == null) {
            if (this.isInsideOfMaterial(Material.WATER)) {
                int var7 = Math.round(MathHelper.sqrt(var1 * var1 + var3 * var3 + var5 * var5) * 100.0F);
                if (var7 > 0) {
                    this.addStat(StatList.distanceDoveStat, var7);
                }
            } else if (this.isInWater()) {
                int var8 = Math.round(MathHelper.sqrt(var1 * var1 + var5 * var5) * 100.0F);
                if (var8 > 0) {
                    this.addStat(StatList.distanceSwumStat, var8);
                }
            } else if (this.isOnLadder()) {
                if (var3 > 0.0D) {
                    this.addStat(StatList.distanceClimbedStat, (int) Math.round(var3 * 100.0D));
                }
            } else if (this.onGround) {
                int var9 = Math.round(MathHelper.sqrt(var1 * var1 + var5 * var5) * 100.0F);
                if (var9 > 0) {
                    this.addStat(StatList.distanceWalkedStat, var9);
                }
            } else {
                int var10 = Math.round(MathHelper.sqrt(var1 * var1 + var5 * var5) * 100.0F);
                if (var10 > 25) {
                    this.addStat(StatList.distanceFlownStat, var10);
                }
            }

        }
    }

    private void addMountedMovementStat(double var1, double var3, double var5) {
        if (this.ridingEntity != null) {
            int var7 = Math.round(MathHelper.sqrt(var1 * var1 + var3 * var3 + var5 * var5) * 100.0F);
            if (var7 > 0) {
                if (this.ridingEntity instanceof EntityMinecart) {
                    this.addStat(StatList.distanceByMinecartStat, var7);
                    if (this.startMinecartRidingCoordinate == null) {
                        this.startMinecartRidingCoordinate = new ChunkCoordinates(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));
                    } else if (this.startMinecartRidingCoordinate.getSqDistanceTo(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)) >= 1000.0D) {
                        this.addStat(AchievementList.onARail, 1);
                    }
                } else if (this.ridingEntity instanceof EntityBoat) {
                    this.addStat(StatList.distanceByBoatStat, var7);
                } else if (this.ridingEntity instanceof EntityPig) {
                    this.addStat(StatList.distanceByPigStat, var7);
                }
            }
        }

    }

    @Override
    protected void fall(float var1) {
        if (var1 >= 2.0F) {
            this.addStat(StatList.distanceFallenStat, (int) Math.round((double) var1 * 100.0D));
        }

        super.fall(var1);
    }

    @Override
    public void onKillEntity(EntityLiving var1) {
        if (var1 instanceof EntityMob) {
            this.triggerAchievement(AchievementList.killEnemy);
        }

    }

    @Side(CodeSide.CLIENT)
    @Override
    public int getItemIcon(ItemStack var1) {
        int var2 = super.getItemIcon(var1);
        if (var1.itemID == Item.FISHING_ROD.shiftedIndex && this.fishEntity != null) {
            var2 = var1.getIconIndex() + 16;
        }

        return var2;
    }

    @Override
    public void setInPortal() {
        if (this.timeUntilPortal > 0) {
            this.timeUntilPortal = 10;
        } else {
            this.inPortal = true;
        }
    }
}
