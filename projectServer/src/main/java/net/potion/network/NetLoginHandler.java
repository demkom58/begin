package net.potion.network;

import net.potion.world.chunk.ChunkCoordinates;
import net.potion.entity.player.EntityPlayerMP;
import net.potion.network.packet.*;
import net.potion.server.PotionServer;
import net.potion.world.WorldServer;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Logger;

public class NetLoginHandler extends NetHandler {
    public static Logger logger = Logger.getLogger("Potion");
    private static Random rand = new Random();
    public NetworkManager netManager;
    public boolean finishedProcessing = false;
    private PotionServer server;
    private int loginTimer = 0;
    private String username = null;
    private Packet1Login packet1login = null;
    private String serverId = "";

    public NetLoginHandler(PotionServer server, Socket var2, String var3) throws IOException {
        this.server = server;
        this.netManager = new NetworkManager(var2, var3, this);
        this.netManager.chunkDataSendCounter = 0;
    }

    // $FF: synthetic method
    static String getServerId(NetLoginHandler var0) {
        return var0.serverId;
    }

    // $FF: synthetic method
    static Packet1Login setLoginPacket(NetLoginHandler var0, Packet1Login var1) {
        return var0.packet1login = var1;
    }

    public void tryLogin() {
        if (this.packet1login != null) {
            this.doLogin(this.packet1login);
            this.packet1login = null;
        }

        if (this.loginTimer++ == 600) {
            this.kickUser("Took too long to log in");
        } else {
            this.netManager.processReadPackets();
        }

    }

    public void kickUser(String var1) {
        try {
            logger.info("Disconnecting " + this.getUserAndIPString() + ": " + var1);
            this.netManager.addToSendQueue(new Packet255KickDisconnect(var1));
            this.netManager.serverShutdown();
            this.finishedProcessing = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handleHandshake(Packet2Handshake var1) {
        if (this.server.onlineMode) {
            this.serverId = Long.toHexString(rand.nextLong());
            this.netManager.addToSendQueue(new Packet2Handshake(this.serverId));
        } else {
            this.netManager.addToSendQueue(new Packet2Handshake("-"));
        }

    }

    @Override
    public void handleLogin(Packet1Login var1) {
        this.username = var1.username;
        if (var1.protocolVersion != 14) {
            if (var1.protocolVersion > 14) {
                this.kickUser("Outdated server!");
            } else {
                this.kickUser("Outdated client!");
            }

        } else {
            if (!this.server.onlineMode) {
                this.doLogin(var1);
            } else {
                (new ThreadLoginVerifier(this, var1)).start();
            }

        }
    }

    public void doLogin(Packet1Login var1) {
        EntityPlayerMP var2 = this.server.configManager.login(this, var1.username);
        if (var2 != null) {
            this.server.configManager.readPlayerDataFromFile(var2);
            var2.setWorldHandler(this.server.getWorldServer(var2.dimension));
            logger.info(this.getUserAndIPString() + " logged in with entity id " + var2.entityId + " at (" + var2.posX + ", " + var2.posY + ", " + var2.posZ + ")");
            WorldServer var3 = this.server.getWorldServer(var2.dimension);
            ChunkCoordinates var4 = var3.getSpawnPoint();
            NetServerHandler var5 = new NetServerHandler(this.server, this.netManager, var2);
            var5.sendPacket(new Packet1Login("", var2.entityId, var3.getRandomSeed(), (byte) var3.worldProvider.worldType));
            var5.sendPacket(new Packet6SpawnPosition(var4.posX, var4.posY, var4.posZ));
            this.server.configManager.func_28170_a(var2, var3);
            this.server.configManager.sendPacketToAllPlayers(new Packet3Chat("\u00a7e" + var2.username + " joined the game."));
            this.server.configManager.playerLoggedIn(var2);
            var5.teleportTo(var2.posX, var2.posY, var2.posZ, var2.rotationYaw, var2.rotationPitch);
            this.server.networkServer.addPlayer(var5);
            var5.sendPacket(new Packet4UpdateTime(var3.getWorldTime()));
            var2.func_20057_k();
        }

        this.finishedProcessing = true;
    }

    @Override
    public void handleErrorMessage(String var1, Object[] var2) {
        logger.info(this.getUserAndIPString() + " lost connection");
        this.finishedProcessing = true;
    }

    @Override
    public void registerPacket(Packet var1) {
        this.kickUser("Protocol error");
    }

    public String getUserAndIPString() {
        return this.username != null ? this.username + " [" + this.netManager.getRemoteAddress().toString() + "]" : this.netManager.getRemoteAddress().toString();
    }

    @Override
    public boolean isServerHandler() {
        return true;
    }
}
