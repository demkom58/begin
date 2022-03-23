package net.potion.entity.player;

import net.potion.block.EnumBedStatus;
import net.potion.entity.Entity;
import net.potion.entity.EntityTracker;
import net.potion.entity.ICrafting;
import net.potion.entity.item.EntityItem;
import net.potion.entity.projectile.EntityArrow;
import net.potion.inventory.*;
import net.potion.item.Item;
import net.potion.item.ItemInWorldManager;
import net.potion.item.ItemMapBase;
import net.potion.item.ItemStack;
import net.potion.network.NetServerHandler;
import net.potion.network.packet.*;
import net.potion.server.PotionServer;
import net.potion.stats.StatBase;
import net.potion.tileentity.TileEntity;
import net.potion.tileentity.TileEntityDispenser;
import net.potion.tileentity.TileEntityFurnace;
import net.potion.util.StringTranslate;
import net.potion.world.World;
import net.potion.world.WorldServer;
import net.potion.world.chunk.ChunkCoordIntPair;
import net.potion.world.chunk.ChunkCoordinates;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EntityPlayerMP extends EntityPlayer implements ICrafting {
    public NetServerHandler playerNetServerHandler;
    public PotionServer server;
    public ItemInWorldManager itemInWorldManager;
    public double field_9155_d;
    public double field_9154_e;
    public List<ChunkCoordIntPair> loadedChunks = new LinkedList<>();
    public Set<ChunkCoordIntPair> field_420_ah = new HashSet<>();
    public boolean isChangingQuantityOnly;
    private int lastHealth = -99999999;
    private int ticksOfInvuln = 60;
    private ItemStack[] playerInventory = new ItemStack[]{null, null, null, null, null};
    private int currentWindowId = 0;

    public EntityPlayerMP(PotionServer var1, World var2, String var3, ItemInWorldManager var4) {
        super(var2);
        var4.thisPlayer = this;
        this.itemInWorldManager = var4;
        ChunkCoordinates var5 = var2.getSpawnPoint();
        int var6 = var5.x;
        int var7 = var5.z;
        int var8 = var5.y;
        if (!var2.worldProvider.hasNoSky) {
            var6 += this.rand.nextInt(20) - 10;
            var8 = var2.findTopSolidBlock(var6, var7);
            var7 += this.rand.nextInt(20) - 10;
        }

        this.setLocationAndAngles((double) var6 + 0.5D, var8, (double) var7 + 0.5D, 0.0F, 0.0F);
        this.server = var1;
        this.stepHeight = 0.0F;
        this.username = var3;
        this.yOffset = 0.0F;
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        this.itemInWorldManager = new ItemInWorldManager((WorldServer) world);
        this.itemInWorldManager.thisPlayer = this;
    }

    public void func_20057_k() {
        this.craftingInventory.addViewer(this);
    }

    @Override
    public ItemStack[] getInventory() {
        return this.playerInventory;
    }

    @Override
    protected void resetHeight() {
        this.yOffset = 0.0F;
    }

    @Override
    public float getEyeHeight() {
        return 1.62F;
    }

    @Override
    public void onUpdate() {
        this.itemInWorldManager.func_328_a();
        --this.ticksOfInvuln;
        this.craftingInventory.updateCraftingMatrix();

        for (int var1 = 0; var1 < 5; ++var1) {
            ItemStack var2 = this.getEquipmentInSlot(var1);
            if (var2 != this.playerInventory[var1]) {
                this.server.getEntityTracker(this.dimension).sendPacketToTrackedPlayers(this, new Packet5PlayerInventory(this.entityId, var1, var2));
                this.playerInventory[var1] = var2;
            }
        }

    }

    public ItemStack getEquipmentInSlot(int var1) {
        return var1 == 0 ? this.inventory.getCurrentItem() : this.inventory.armorInventory[var1 - 1];
    }

    @Override
    public void onDeath(Entity var1) {
        this.inventory.dropAllItems();
    }

    @Override
    public boolean attackEntityFrom(Entity var1, int var2) {
        if (this.ticksOfInvuln > 0) {
            return false;
        } else {
            if (!this.server.pvpOn) {
                if (var1 instanceof EntityPlayer) {
                    return false;
                }

                if (var1 instanceof EntityArrow) {
                    EntityArrow var3 = (EntityArrow) var1;
                    if (var3.owner instanceof EntityPlayer) {
                        return false;
                    }
                }
            }

            return super.attackEntityFrom(var1, var2);
        }
    }

    @Override
    protected boolean isPvpEnabled() {
        return this.server.pvpOn;
    }

    @Override
    public void heal(int var1) {
        super.heal(var1);
    }

    public void onUpdateEntity(boolean var1) {
        super.onUpdate();

        for (int var2 = 0; var2 < this.inventory.getSizeInventory(); ++var2) {
            ItemStack var3 = this.inventory.getStackInSlot(var2);
            if (var3 != null && Item.ITEMS_LIST[var3.itemID].shouldRotateAroundWhenRendering() && this.playerNetServerHandler.getNumChunkDataPackets() <= 2) {
                Packet var4 = ((ItemMapBase) Item.ITEMS_LIST[var3.itemID]).method1(var3, this.world, this);
                if (var4 != null) {
                    this.playerNetServerHandler.sendPacket(var4);
                }
            }
        }

        if (var1 && !this.loadedChunks.isEmpty()) {
            ChunkCoordIntPair var7 = this.loadedChunks.get(0);
            if (var7 != null) {
                boolean var8 = false;
                if (this.playerNetServerHandler.getNumChunkDataPackets() < 4) {
                    var8 = true;
                }

                if (var8) {
                    WorldServer var9 = this.server.getWorldServer(this.dimension);
                    this.loadedChunks.remove(var7);
                    this.playerNetServerHandler.sendPacket(new Packet51MapChunk(var7.chunkXPos * 16, 0, var7.chunkZPos * 16, 16, 128, 16, var9));
                    List<TileEntity> var5 = var9.getTileEntityList(var7.chunkXPos * 16, 0, var7.chunkZPos * 16, var7.chunkXPos * 16 + 16, 128, var7.chunkZPos * 16 + 16);

                    for (int var6 = 0; var6 < var5.size(); ++var6) {
                        this.getTileEntityInfo(var5.get(var6));
                    }
                }
            }
        }

        if (this.inPortal) {
            if (this.server.propertyManagerObj.getBooleanProperty("allow-nether", true)) {
                if (this.craftingInventory != this.inventorySlots) {
                    this.closeScreen();
                }

                if (this.ridingEntity != null) {
                    this.mountEntity(this.ridingEntity);
                } else {
                    this.timeInPortal += 0.0125F;
                    if (this.timeInPortal >= 1.0F) {
                        this.timeInPortal = 1.0F;
                        this.timeUntilPortal = 10;
                        this.server.configManager.sendPlayerToOtherDimension(this);
                    }
                }

                this.inPortal = false;
            }
        } else {
            if (this.timeInPortal > 0.0F) {
                this.timeInPortal -= 0.05F;
            }

            if (this.timeInPortal < 0.0F) {
                this.timeInPortal = 0.0F;
            }
        }

        if (this.timeUntilPortal > 0) {
            --this.timeUntilPortal;
        }

        if (this.health != this.lastHealth) {
            this.playerNetServerHandler.sendPacket(new Packet8UpdateHealth(this.health));
            this.lastHealth = this.health;
        }

    }

    private void getTileEntityInfo(TileEntity var1) {
        if (var1 != null) {
            Packet var2 = var1.getDescriptionPacket();
            if (var2 != null) {
                this.playerNetServerHandler.sendPacket(var2);
            }
        }

    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    @Override
    public void onItemPickup(Entity var1, int var2) {
        if (!var1.isDead) {
            EntityTracker var3 = this.server.getEntityTracker(this.dimension);
            if (var1 instanceof EntityItem) {
                var3.sendPacketToTrackedPlayers(var1, new Packet22Collect(var1.entityId, this.entityId));
            }

            if (var1 instanceof EntityArrow) {
                var3.sendPacketToTrackedPlayers(var1, new Packet22Collect(var1.entityId, this.entityId));
            }
        }

        super.onItemPickup(var1, var2);
        this.craftingInventory.updateCraftingMatrix();
    }

    @Override
    public void swingItem() {
        if (!this.isSwinging) {
            this.swingProgressInt = -1;
            this.isSwinging = true;
            EntityTracker var1 = this.server.getEntityTracker(this.dimension);
            var1.sendPacketToTrackedPlayers(this, new Packet18Animation(this, 1));
        }

    }

    public void func_22068_s() {
    }

    @Override
    public EnumBedStatus sleepInBedAt(int var1, int var2, int var3) {
        EnumBedStatus var4 = super.sleepInBedAt(var1, var2, var3);
        if (var4 == EnumBedStatus.OK) {
            EntityTracker var5 = this.server.getEntityTracker(this.dimension);
            Packet17Sleep var6 = new Packet17Sleep(this, 0, var1, var2, var3);
            var5.sendPacketToTrackedPlayers(this, var6);
            this.playerNetServerHandler.teleportTo(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.playerNetServerHandler.sendPacket(var6);
        }

        return var4;
    }

    @Override
    public void wakeUpPlayer(boolean var1, boolean var2, boolean var3) {
        if (this.isSleeping()) {
            EntityTracker var4 = this.server.getEntityTracker(this.dimension);
            var4.sendPacketToTrackedPlayersAndTrackedEntity(this, new Packet18Animation(this, 3));
        }

        super.wakeUpPlayer(var1, var2, var3);
        if (this.playerNetServerHandler != null) {
            this.playerNetServerHandler.teleportTo(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }

    }

    @Override
    public void mountEntity(Entity var1) {
        super.mountEntity(var1);
        this.playerNetServerHandler.sendPacket(new Packet39AttachEntity(this, this.ridingEntity));
        this.playerNetServerHandler.teleportTo(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    }

    @Override
    protected void updateFallState(double var1, boolean var3) {
    }

    public void handleFalling(double var1, boolean var3) {
        super.updateFallState(var1, var3);
    }

    private void getNextWidowId() {
        this.currentWindowId = this.currentWindowId % 100 + 1;
    }

    @Override
    public void displayWorkbenchGUI(int var1, int var2, int var3) {
        this.getNextWidowId();
        this.playerNetServerHandler.sendPacket(new Packet100OpenWindow(this.currentWindowId, 1, "Crafting", 9));
        this.craftingInventory = new ContainerWorkbench(this.inventory, this.world, var1, var2, var3);
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.addViewer(this);
    }

    @Override
    public void displayGUIChest(IInventory var1) {
        this.getNextWidowId();
        this.playerNetServerHandler.sendPacket(new Packet100OpenWindow(this.currentWindowId, 0, var1.getInvName(), var1.getSizeInventory()));
        this.craftingInventory = new ContainerChest(this.inventory, var1);
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.addViewer(this);
    }

    @Override
    public void displayGUIFurnace(TileEntityFurnace var1) {
        this.getNextWidowId();
        this.playerNetServerHandler.sendPacket(new Packet100OpenWindow(this.currentWindowId, 2, var1.getInvName(), var1.getSizeInventory()));
        this.craftingInventory = new ContainerFurnace(this.inventory, var1);
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.addViewer(this);
    }

    @Override
    public void displayGUIDispenser(TileEntityDispenser var1) {
        this.getNextWidowId();
        this.playerNetServerHandler.sendPacket(new Packet100OpenWindow(this.currentWindowId, 3, var1.getInvName(), var1.getSizeInventory()));
        this.craftingInventory = new ContainerDispenser(this.inventory, var1);
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.addViewer(this);
    }

    @Override
    public void updateCraftingInventorySlot(Container var1, int var2, ItemStack var3) {
        if (!(var1.getSlot(var2) instanceof SlotCrafting)) {
            if (!this.isChangingQuantityOnly) {
                this.playerNetServerHandler.sendPacket(new Packet103SetSlot(var1.windowId, var2, var3));
            }
        }
    }

    public void func_28017_a(Container var1) {
        this.updateCraftingInventory(var1, var1.getItemStacks());
    }

    @Override
    public void updateCraftingInventory(Container var1, List<ItemStack> var2) {
        this.playerNetServerHandler.sendPacket(new Packet104WindowItems(var1.windowId, var2));
        this.playerNetServerHandler.sendPacket(new Packet103SetSlot(-1, -1, this.inventory.getItemStack()));
    }

    @Override
    public void updateCraftingInventoryInfo(Container var1, int var2, int var3) {
        this.playerNetServerHandler.sendPacket(new Packet105UpdateProgressbar(var1.windowId, var2, var3));
    }

    @Override
    public void onItemStackChanged(ItemStack var1) {
    }

    @Override
    public void closeScreen() {
        this.playerNetServerHandler.sendPacket(new Packet101CloseWindow(this.craftingInventory.windowId));
        this.closeCraftingGui();
    }

    public void updateHeldItem() {
        if (!this.isChangingQuantityOnly) {
            this.playerNetServerHandler.sendPacket(new Packet103SetSlot(-1, -1, this.inventory.getItemStack()));
        }
    }

    public void closeCraftingGui() {
        this.craftingInventory.onCraftGuiClosed(this);
        this.craftingInventory = this.inventorySlots;
    }

    public void setMovementType(float var1, float var2, boolean var3, boolean var4, float var5, float var6) {
        this.moveStrafing = var1;
        this.moveForward = var2;
        this.isJumping = var3;
        this.setSneaking(var4);
        this.rotationPitch = var5;
        this.rotationYaw = var6;
    }

    @Override
    public void addStat(StatBase var1, int var2) {
        if (var1 != null) {
            if (!var1.clientSide) {
                while (var2 > 100) {
                    this.playerNetServerHandler.sendPacket(new Packet200Statistic(var1.statId, 100));
                    var2 -= 100;
                }

                this.playerNetServerHandler.sendPacket(new Packet200Statistic(var1.statId, var2));
            }

        }
    }

    public void func_30002_A() {
        if (this.ridingEntity != null) {
            this.mountEntity(this.ridingEntity);
        }

        if (this.riddenByEntity != null) {
            this.riddenByEntity.mountEntity(this);
        }

        if (this.sleeping) {
            this.wakeUpPlayer(true, false, false);
        }

    }

    public void func_30001_B() {
        this.lastHealth = -99999999;
    }

    @Override
    public void addChatMessage(String var1) {
        StringTranslate var2 = StringTranslate.getInstance();
        String var3 = var2.translateKey(var1);
        this.playerNetServerHandler.sendPacket(new Packet3Chat(var3));
    }
}
