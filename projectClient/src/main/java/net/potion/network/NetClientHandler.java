package net.potion.network;

import net.potion.block.Block;
import net.potion.client.PotionClient;
import net.potion.client.gui.GuiConnectFailed;
import net.potion.client.gui.GuiDownloadTerrain;
import net.potion.entity.*;
import net.potion.entity.item.EntityBoat;
import net.potion.entity.item.EntityItem;
import net.potion.entity.passive.EntityFish;
import net.potion.entity.player.*;
import net.potion.entity.projectile.EntityArrow;
import net.potion.entity.projectile.EntityEgg;
import net.potion.entity.projectile.EntityFireball;
import net.potion.entity.projectile.EntitySnowball;
import net.potion.inventory.Container;
import net.potion.inventory.InventoryBasic;
import net.potion.item.Item;
import net.potion.item.ItemMap;
import net.potion.item.ItemStack;
import net.potion.network.packet.*;
import net.potion.stats.StatList;
import net.potion.tileentity.TileEntity;
import net.potion.tileentity.TileEntityDispenser;
import net.potion.tileentity.TileEntityFurnace;
import net.potion.tileentity.TileEntitySign;
import net.potion.util.Explosion;
import net.hypnosis.util.math.MathHelper;
import net.potion.world.WorldClient;
import net.potion.world.chunk.Chunk;
import net.potion.world.chunk.ChunkCoordinates;
import net.potion.world.storage.MapStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class NetClientHandler extends NetHandler {
    public String field_1209_a;
    public MapStorage field_28118_b = new MapStorage(null);
    Random rand = new Random();
    private boolean disconnected = false;
    private final NetworkManager netManager;
    private final PotionClient potion;
    private WorldClient worldClient;
    private boolean field_1210_g = false;

    public NetClientHandler(PotionClient var1, String var2, int var3) throws IOException {
        this.potion = var1;
        Socket var4 = new Socket(InetAddress.getByName(var2), var3);
        this.netManager = new NetworkManager(var4, "Client", this);
    }

    public void processReadPackets() {
        if (!this.disconnected) {
            this.netManager.processReadPackets();
        }

        this.netManager.interrupt();
    }

    @Override
    public void handleLogin(Packet1Login var1) {
        this.potion.playerController = new PlayerControllerMP(this.potion, this);
        this.potion.statFileWriter.addStat(StatList.joinMultiplayerStat, 1);
        this.worldClient = new WorldClient(this, var1.mapSeed, var1.dimension);
        this.worldClient.localWorld = true;
        this.potion.changeWorld(this.worldClient);
        this.potion.thePlayer.dimension = var1.dimension;
        this.potion.displayGuiScreen(new GuiDownloadTerrain(this));
        this.potion.thePlayer.entityId = var1.protocolVersion;
    }

    @Override
    public void handlePickupSpawn(Packet21PickupSpawn var1) {
        double var2 = (double) var1.xPosition / 32.0D;
        double var4 = (double) var1.yPosition / 32.0D;
        double var6 = (double) var1.zPosition / 32.0D;
        EntityItem var8 = new EntityItem(this.worldClient, var2, var4, var6, new ItemStack(var1.itemID, var1.count, var1.itemDamage));
        var8.motionX = (double) var1.rotation / 128.0D;
        var8.motionY = (double) var1.pitch / 128.0D;
        var8.motionZ = (double) var1.roll / 128.0D;
        var8.serverPosX = var1.xPosition;
        var8.serverPosY = var1.yPosition;
        var8.serverPosZ = var1.zPosition;
        this.worldClient.func_712_a(var1.entityId, var8);
    }

    @Override
    public void handleVehicleSpawn(Packet23VehicleSpawn var1) {
        double var2 = (double) var1.xPosition / 32.0D;
        double var4 = (double) var1.yPosition / 32.0D;
        double var6 = (double) var1.zPosition / 32.0D;
        Entity var8 = null;
        if (var1.type == 10) {
            var8 = new EntityMinecart(this.worldClient, var2, var4, var6, 0);
        }

        if (var1.type == 11) {
            var8 = new EntityMinecart(this.worldClient, var2, var4, var6, 1);
        }

        if (var1.type == 12) {
            var8 = new EntityMinecart(this.worldClient, var2, var4, var6, 2);
        }

        if (var1.type == 90) {
            var8 = new EntityFish(this.worldClient, var2, var4, var6);
        }

        if (var1.type == 60) {
            var8 = new EntityArrow(this.worldClient, var2, var4, var6);
        }

        if (var1.type == 61) {
            var8 = new EntitySnowball(this.worldClient, var2, var4, var6);
        }

        if (var1.type == 63) {
            var8 = new EntityFireball(this.worldClient, var2, var4, var6, (double) var1.motionX / 8000.0D, (double) var1.motionY / 8000.0D, (double) var1.motionZ / 8000.0D);
            var1.ownerId = 0;
        }

        if (var1.type == 62) {
            var8 = new EntityEgg(this.worldClient, var2, var4, var6);
        }

        if (var1.type == 1) {
            var8 = new EntityBoat(this.worldClient, var2, var4, var6);
        }

        if (var1.type == 50) {
            var8 = new EntityTNTPrimed(this.worldClient, var2, var4, var6);
        }

        if (var1.type == 70) {
            var8 = new EntityFallingSand(this.worldClient, var2, var4, var6, Block.SAND.blockID);
        }

        if (var1.type == 71) {
            var8 = new EntityFallingSand(this.worldClient, var2, var4, var6, Block.GRAVEL.blockID);
        }

        if (var8 != null) {
            var8.serverPosX = var1.xPosition;
            var8.serverPosY = var1.yPosition;
            var8.serverPosZ = var1.zPosition;
            var8.rotationYaw = 0.0F;
            var8.rotationPitch = 0.0F;
            var8.entityId = var1.entityId;
            this.worldClient.func_712_a(var1.entityId, var8);
            if (var1.ownerId > 0) {
                if (var1.type == 60) {
                    Entity var9 = this.getEntityByID(var1.ownerId);
                    if (var9 instanceof EntityLiving) {
                        ((EntityArrow) var8).owner = (EntityLiving) var9;
                    }
                }

                var8.setVelocity((double) var1.motionX / 8000.0D, (double) var1.motionY / 8000.0D, (double) var1.motionZ / 8000.0D);
            }
        }

    }

    @Override
    public void handleWeather(Packet71Weather var1) {
        double var2 = (double) var1.x / 32.0D;
        double var4 = (double) var1.y / 32.0D;
        double var6 = (double) var1.z / 32.0D;
        EntityLightningBolt var8 = null;
        if (var1.field1 == 1) {
            var8 = new EntityLightningBolt(this.worldClient, var2, var4, var6);
        }

        if (var8 != null) {
            var8.serverPosX = var1.x;
            var8.serverPosY = var1.y;
            var8.serverPosZ = var1.z;
            var8.rotationYaw = 0.0F;
            var8.rotationPitch = 0.0F;
            var8.entityId = var1.entityId;
            this.worldClient.addWeatherEffect(var8);
        }

    }

    @Override
    public void handleEntityPainting(Packet25EntityPainting var1) {
        EntityPainting var2 = new EntityPainting(this.worldClient, var1.xPosition, var1.yPosition, var1.zPosition, var1.direction, var1.title);
        this.worldClient.func_712_a(var1.entityId, var2);
    }

    @Override
    public void handleEntityVelocity(Packet28EntityVelocity var1) {
        Entity var2 = this.getEntityByID(var1.entityId);
        if (var2 != null) {
            var2.setVelocity((double) var1.motionX / 8000.0D, (double) var1.motionY / 8000.0D, (double) var1.motionZ / 8000.0D);
        }
    }

    @Override
    public void handleEntityMetadata(Packet40EntityMetadata var1) {
        Entity var2 = this.getEntityByID(var1.entityId);
        if (var2 != null && var1.getObjects() != null) {
            var2.getDataWatcher().updateWatchedObjectsFromList(var1.getObjects());
        }

    }

    @Override
    public void handleNamedEntitySpawn(Packet20NamedEntitySpawn var1) {
        double var2 = (double) var1.xPosition / 32.0D;
        double var4 = (double) var1.yPosition / 32.0D;
        double var6 = (double) var1.zPosition / 32.0D;
        float var8 = (float) (var1.rotation * 360) / 256.0F;
        float var9 = (float) (var1.pitch * 360) / 256.0F;
        EntityOtherPlayerMP var10 = new EntityOtherPlayerMP(this.potion.theWorld, var1.name);
        var10.prevPosX = var10.lastTickPosX = var10.serverPosX = var1.xPosition;
        var10.prevPosY = var10.lastTickPosY = var10.serverPosY = var1.yPosition;
        var10.prevPosZ = var10.lastTickPosZ = var10.serverPosZ = var1.zPosition;
        int var11 = var1.currentItem;
        if (var11 == 0) {
            var10.inventory.mainInventory[var10.inventory.currentItem] = null;
        } else {
            var10.inventory.mainInventory[var10.inventory.currentItem] = new ItemStack(var11, 1, 0);
        }

        var10.setPositionAndRotation(var2, var4, var6, var8, var9);
        this.worldClient.func_712_a(var1.entityId, var10);
    }

    @Override
    public void handleEntityTeleport(Packet34EntityTeleport var1) {
        Entity var2 = this.getEntityByID(var1.entityId);
        if (var2 != null) {
            var2.serverPosX = var1.xPosition;
            var2.serverPosY = var1.yPosition;
            var2.serverPosZ = var1.zPosition;
            double var3 = (double) var2.serverPosX / 32.0D;
            double var5 = (double) var2.serverPosY / 32.0D + 0.015625D;
            double var7 = (double) var2.serverPosZ / 32.0D;
            float var9 = (float) (var1.yaw * 360) / 256.0F;
            float var10 = (float) (var1.pitch * 360) / 256.0F;
            var2.setPositionAndRotation2(var3, var5, var7, var9, var10, 3);
        }
    }

    @Override
    public void handleEntity(Packet30Entity var1) {
        Entity var2 = this.getEntityByID(var1.entityId);
        if (var2 != null) {
            var2.serverPosX += var1.xPosition;
            var2.serverPosY += var1.yPosition;
            var2.serverPosZ += var1.zPosition;
            double var3 = (double) var2.serverPosX / 32.0D;
            double var5 = (double) var2.serverPosY / 32.0D;
            double var7 = (double) var2.serverPosZ / 32.0D;
            float var9 = var1.rotating ? (float) (var1.yaw * 360) / 256.0F : var2.rotationYaw;
            float var10 = var1.rotating ? (float) (var1.pitch * 360) / 256.0F : var2.rotationPitch;
            var2.setPositionAndRotation2(var3, var5, var7, var9, var10, 3);
        }
    }

    @Override
    public void handleDestroyEntity(Packet29DestroyEntity var1) {
        this.worldClient.removeEntityFromWorld(var1.entityId);
    }

    @Override
    public void handleFlying(Packet10Flying var1) {
        EntityPlayerSP var2 = this.potion.thePlayer;
        double var3 = var2.posX;
        double var5 = var2.posY;
        double var7 = var2.posZ;
        float var9 = var2.rotationYaw;
        float var10 = var2.rotationPitch;
        if (var1.moving) {
            var3 = var1.xPosition;
            var5 = var1.yPosition;
            var7 = var1.zPosition;
        }

        if (var1.rotating) {
            var9 = var1.yaw;
            var10 = var1.pitch;
        }

        var2.ySize = 0.0F;
        var2.motionX = var2.motionY = var2.motionZ = 0.0D;
        var2.setPositionAndRotation(var3, var5, var7, var9, var10);
        var1.xPosition = var2.posX;
        var1.yPosition = var2.boundingBox.minY;
        var1.zPosition = var2.posZ;
        var1.stance = var2.posY;
        this.netManager.addToSendQueue(var1);
        if (!this.field_1210_g) {
            this.potion.thePlayer.prevPosX = this.potion.thePlayer.posX;
            this.potion.thePlayer.prevPosY = this.potion.thePlayer.posY;
            this.potion.thePlayer.prevPosZ = this.potion.thePlayer.posZ;
            this.field_1210_g = true;
            this.potion.displayGuiScreen(null);
        }

    }

    @Override
    public void handlePreChunk(Packet50PreChunk var1) {
        this.worldClient.doPreChunk(var1.xPosition, var1.yPosition, var1.mode);
    }

    @Override
    public void handleMultiBlockChange(Packet52MultiBlockChange var1) {
        Chunk var2 = this.worldClient.getChunkFromChunkCoords(var1.xPosition, var1.zPosition);
        int var3 = var1.xPosition * 16;
        int var4 = var1.zPosition * 16;

        for (int var5 = 0; var5 < var1.size; ++var5) {
            short var6 = var1.coordinateArray[var5];
            int var7 = var1.typeArray[var5] & 255;
            byte var8 = var1.metadataArray[var5];
            int var9 = var6 >> 12 & 15;
            int var10 = var6 >> 8 & 15;
            int var11 = var6 & 255;
            var2.setBlockIDWithMetadata(var9, var11, var10, var7, var8);
            this.worldClient.func_711_c(var9 + var3, var11, var10 + var4, var9 + var3, var11, var10 + var4);
            this.worldClient.markBlocksDirty(var9 + var3, var11, var10 + var4, var9 + var3, var11, var10 + var4);
        }

    }

    @Override
    public void handleMapChunk(Packet51MapChunk var1) {
        this.worldClient.func_711_c(var1.xPosition, var1.yPosition, var1.zPosition, var1.xPosition + var1.xSize - 1, var1.yPosition + var1.ySize - 1, var1.zPosition + var1.zSize - 1);
        this.worldClient.setChunkData(var1.xPosition, var1.yPosition, var1.zPosition, var1.xSize, var1.ySize, var1.zSize, var1.chunk);
    }

    @Override
    public void handleBlockChange(Packet53BlockChange var1) {
        this.worldClient.func_714_c(var1.xPosition, var1.yPosition, var1.zPosition, var1.type, var1.metadata);
    }

    @Override
    public void handleKickDisconnect(Packet255KickDisconnect var1) {
        this.netManager.networkShutdown("disconnect.kicked");
        this.disconnected = true;
        this.potion.changeWorld(null);
        this.potion.displayGuiScreen(new GuiConnectFailed("disconnect.disconnected", "disconnect.genericReason", var1.reason));
    }

    @Override
    public void handleErrorMessage(String var1, Object[] var2) {
        if (!this.disconnected) {
            this.disconnected = true;
            this.potion.changeWorld(null);
            this.potion.displayGuiScreen(new GuiConnectFailed("disconnect.lost", var1, var2));
        }
    }

    public void func_28117_a(Packet var1) {
        if (!this.disconnected) {
            this.netManager.addToSendQueue(var1);
            this.netManager.serverShutdown();
        }
    }

    public void addToSendQueue(Packet var1) {
        if (!this.disconnected) {
            this.netManager.addToSendQueue(var1);
        }
    }

    @Override
    public void handleCollect(Packet22Collect var1) {
        Entity var2 = this.getEntityByID(var1.collectedEntityId);
        Object var3 = this.getEntityByID(var1.collectorEntityId);
        if (var3 == null) {
            var3 = this.potion.thePlayer;
        }

        if (var2 != null) {
            this.worldClient.playSoundAtEntity(var2, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            this.potion.effectRenderer.addEffect(new EntityPickupFX(this.potion.theWorld, var2, (Entity) var3, -0.5F));
            this.worldClient.removeEntityFromWorld(var1.collectedEntityId);
        }

    }

    @Override
    public void handleChat(Packet3Chat chat) {
        this.potion.ingameGUI.addChatMessage(chat.message);
    }

    @Override
    public void handleArmAnimation(Packet18Animation var1) {
        Entity var2 = this.getEntityByID(var1.entityId);
        if (var2 != null) {
            if (var1.animate == 1) {
                EntityPlayer var3 = (EntityPlayer) var2;
                var3.swingItem();
            } else if (var1.animate == 2) {
                var2.performHurtAnimation();
            } else if (var1.animate == 3) {
                EntityPlayer var4 = (EntityPlayer) var2;
                var4.wakeUpPlayer(false, false, false);
            } else if (var1.animate == 4) {
                EntityPlayer var5 = (EntityPlayer) var2;
                var5.playRespawnAnimation();
            }

        }
    }

    @Override
    public void handleSleep(Packet17Sleep var1) {
        Entity var2 = this.getEntityByID(var1.entityId);
        if (var2 != null) {
            if (var1.field1 == 0) {
                EntityPlayer var3 = (EntityPlayer) var2;
                var3.sleepInBedAt(var1.x, var1.y, var1.z);
            }

        }
    }

    @Override
    public void handleHandshake(Packet2Handshake var1) {
        if (var1.username.equals("-")) {
            this.addToSendQueue(new Packet1Login(this.potion.session.username, 14));
        } else {
            try {
                URL var2 = new URL("http://www.minecraft.net/game/joinserver.jsp?user=" + this.potion.session.username + "&sessionId=" + this.potion.session.sessionId + "&serverId=" + var1.username);
                BufferedReader var3 = new BufferedReader(new InputStreamReader(var2.openStream()));
                String var4 = var3.readLine();
                var3.close();
                if (var4.equalsIgnoreCase("ok")) {
                    this.addToSendQueue(new Packet1Login(this.potion.session.username, 14));
                } else {
                    this.netManager.networkShutdown("disconnect.loginFailedInfo", var4);
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.netManager.networkShutdown("disconnect.genericReason", "Internal client error: " + e);
            }
        }

    }

    public void disconnect() {
        this.disconnected = true;
        this.netManager.interrupt();
        this.netManager.networkShutdown("disconnect.closed");
    }

    @Override
    public void handleMobSpawn(Packet24MobSpawn var1) {
        double var2 = (double) var1.xPosition / 32.0D;
        double var4 = (double) var1.yPosition / 32.0D;
        double var6 = (double) var1.zPosition / 32.0D;
        float var8 = (float) (var1.yaw * 360) / 256.0F;
        float var9 = (float) (var1.pitch * 360) / 256.0F;
        EntityLiving var10 = (EntityLiving) EntityList.createEntity(var1.type, this.potion.theWorld);
        var10.serverPosX = var1.xPosition;
        var10.serverPosY = var1.yPosition;
        var10.serverPosZ = var1.zPosition;
        var10.entityId = var1.entityId;
        var10.setPositionAndRotation(var2, var4, var6, var8, var9);
        var10.isMultiplayerEntity = true;
        this.worldClient.func_712_a(var1.entityId, var10);
        List var11 = var1.getMetadata();
        if (var11 != null) {
            var10.getDataWatcher().updateWatchedObjectsFromList(var11);
        }

    }

    @Override
    public void handleUpdateTime(Packet4UpdateTime var1) {
        this.potion.theWorld.setWorldTime(var1.time);
    }

    @Override
    public void handleSpawnPosition(Packet6SpawnPosition var1) {
        this.potion.thePlayer.setPlayerSpawnCoordinate(new ChunkCoordinates(var1.xPosition, var1.yPosition, var1.zPosition));
        this.potion.theWorld.getWorldInfo().setSpawn(var1.xPosition, var1.yPosition, var1.zPosition);
    }

    @Override
    public void handleAttachEntity(Packet39AttachEntity var1) {
        Object var2 = this.getEntityByID(var1.entityId);
        Entity var3 = this.getEntityByID(var1.vehicleEntityId);
        if (var1.entityId == this.potion.thePlayer.entityId) {
            var2 = this.potion.thePlayer;
        }

        if (var2 != null) {
            ((Entity) var2).mountEntity(var3);
        }
    }

    @Override
    public void handleEntityStatus(Packet38EntityStatus var1) {
        Entity var2 = this.getEntityByID(var1.entityId);
        if (var2 != null) {
            var2.handleHealthUpdate(var1.entityStatus);
        }

    }

    private Entity getEntityByID(int var1) {
        return (var1 == this.potion.thePlayer.entityId ? this.potion.thePlayer : this.worldClient.func_709_b(var1));
    }

    @Override
    public void handleHealth(Packet8UpdateHealth var1) {
        this.potion.thePlayer.setHealth(var1.healthMP);
    }

    @Override
    public void handleRespawnPacket(Packet9Respawn var1) {
        if (var1.dimension != this.potion.thePlayer.dimension) {
            this.field_1210_g = false;
            this.worldClient = new WorldClient(this, this.worldClient.getWorldInfo().getRandomSeed(), var1.dimension);
            this.worldClient.localWorld = true;
            this.potion.changeWorld(this.worldClient);
            this.potion.thePlayer.dimension = var1.dimension;
            this.potion.displayGuiScreen(new GuiDownloadTerrain(this));
        }

        this.potion.respawn(true, var1.dimension);
    }

    @Override
    public void handleExplosion(Packet60Explosion var1) {
        Explosion var2 = new Explosion(this.potion.theWorld, null, var1.explosionX, var1.explosionY, var1.explosionZ, var1.explosionSize);
        var2.destroyedBlockPositions = var1.destroyedBlockPositions;
        var2.doExplosionB(true);
    }

    @Override
    public void handleOpenWindow(Packet100OpenWindow var1) {
        if (var1.inventoryType == 0) {
            InventoryBasic var2 = new InventoryBasic(var1.windowTitle, var1.slotsCount);
            this.potion.thePlayer.displayGUIChest(var2);
            this.potion.thePlayer.craftingInventory.windowId = var1.windowId;
        } else if (var1.inventoryType == 2) {
            TileEntityFurnace var3 = new TileEntityFurnace();
            this.potion.thePlayer.displayGUIFurnace(var3);
            this.potion.thePlayer.craftingInventory.windowId = var1.windowId;
        } else if (var1.inventoryType == 3) {
            TileEntityDispenser var4 = new TileEntityDispenser();
            this.potion.thePlayer.displayGUIDispenser(var4);
            this.potion.thePlayer.craftingInventory.windowId = var1.windowId;
        } else if (var1.inventoryType == 1) {
            EntityPlayerSP var5 = this.potion.thePlayer;
            this.potion.thePlayer.displayWorkbenchGUI(MathHelper.floor(var5.posX), MathHelper.floor(var5.posY), MathHelper.floor(var5.posZ));
            this.potion.thePlayer.craftingInventory.windowId = var1.windowId;
        }

    }

    @Override
    public void handleSetSlot(Packet103SetSlot var1) {
        if (var1.windowId == -1) {
            this.potion.thePlayer.inventory.setItemStack(var1.myItemStack);
        } else if (var1.windowId == 0 && var1.itemSlot >= 36 && var1.itemSlot < 45) {
            ItemStack var2 = this.potion.thePlayer.inventorySlots.getSlot(var1.itemSlot).getStack();
            if (var1.myItemStack != null && (var2 == null || var2.stackSize < var1.myItemStack.stackSize)) {
                var1.myItemStack.animationsToGo = 5;
            }

            this.potion.thePlayer.inventorySlots.putStackInSlot(var1.itemSlot, var1.myItemStack);
        } else if (var1.windowId == this.potion.thePlayer.craftingInventory.windowId) {
            this.potion.thePlayer.craftingInventory.putStackInSlot(var1.itemSlot, var1.myItemStack);
        }

    }

    @Override
    public void handleTransaction(Packet106Transaction var1) {
        Container var2 = null;
        if (var1.windowId == 0) {
            var2 = this.potion.thePlayer.inventorySlots;
        } else if (var1.windowId == this.potion.thePlayer.craftingInventory.windowId) {
            var2 = this.potion.thePlayer.craftingInventory;
        }

        if (var2 != null) {
            if (var1.field1) {
                var2.func_20113_a(var1.shortWindowId);
            } else {
                var2.func_20110_b(var1.shortWindowId);
                this.addToSendQueue(new Packet106Transaction(var1.windowId, var1.shortWindowId, true));
            }
        }

    }

    @Override
    public void handleWindowItems(Packet104WindowItems var1) {
        if (var1.windowId == 0) {
            this.potion.thePlayer.inventorySlots.putStacksInSlots(var1.itemStack);
        } else if (var1.windowId == this.potion.thePlayer.craftingInventory.windowId) {
            this.potion.thePlayer.craftingInventory.putStacksInSlots(var1.itemStack);
        }

    }

    @Override
    public void handleUpdateSign(Packet130UpdateSign var1) {
        if (this.potion.theWorld.blockExists(var1.xPosition, var1.yPosition, var1.zPosition)) {
            TileEntity var2 = this.potion.theWorld.getBlockTileEntity(var1.xPosition, var1.yPosition, var1.zPosition);
            if (var2 instanceof TileEntitySign) {
                TileEntitySign var3 = (TileEntitySign) var2;

                System.arraycopy(var1.signLines, 0, var3.signText, 0, 4);

                var3.onInventoryChanged();
            }
        }

    }

    @Override
    public void handleUpdateProgressbar(Packet105UpdateProgressbar var1) {
        this.registerPacket(var1);
        if (this.potion.thePlayer.craftingInventory != null && this.potion.thePlayer.craftingInventory.windowId == var1.windowId) {
            this.potion.thePlayer.craftingInventory.func_20112_a(var1.progressBar, var1.progressBarValue);
        }

    }

    @Override
    public void handlePlayerInventory(Packet5PlayerInventory var1) {
        Entity var2 = this.getEntityByID(var1.entityID);
        if (var2 != null) {
            var2.outfitWithItem(var1.slot, var1.itemID, var1.itemDamage);
        }

    }

    @Override
    public void handleCloseWindow(Packet101CloseWindow var1) {
        this.potion.thePlayer.closeScreen();
    }

    @Override
    public void handleNotePlay(Packet54PlayNoteBlock var1) {
        this.potion.theWorld.playNoteAt(var1.xLocation, var1.yLocation, var1.zLocation, var1.instrumentType, var1.pitch);
    }

    @Override
    public void handleBed(Packet70Bed var1) {
        int var2 = var1.field1;
        if (var2 >= 0 && var2 < Packet70Bed.MESSAGES.length && Packet70Bed.MESSAGES[var2] != null) {
            this.potion.thePlayer.addChatMessage(Packet70Bed.MESSAGES[var2]);
        }

        if (var2 == 1) {
            this.worldClient.getWorldInfo().setRaining(true);
            this.worldClient.setRainingStrength(1.0F);
        } else if (var2 == 2) {
            this.worldClient.getWorldInfo().setRaining(false);
            this.worldClient.setRainingStrength(0.0F);
        }

    }

    @Override
    public void handleMapData(Packet131MapData var1) {
        if (var1.field1 == Item.MAP.shiftedIndex) {
            ItemMap.method1(var1.field2, this.potion.theWorld).method4(var1.field3);
        } else {
            System.out.println("Unknown itemid: " + var1.field2);
        }

    }

    @Override
    public void handleDoorChange(Packet61DoorChange var1) {
        this.potion.theWorld.playEffects(var1.field1, var1.x, var1.y, var1.z, var1.field2);
    }

    @Override
    public void handleStatistic(Packet200Statistic var1) {
        ((EntityClientPlayerMP) this.potion.thePlayer).addGlobalStat(StatList.getStat(var1.statId), var1.value);
    }

    @Override
    public boolean isServerHandler() {
        return false;
    }
}
