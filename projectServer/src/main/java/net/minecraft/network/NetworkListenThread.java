package net.minecraft.network;

import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkListenThread {
    public static Logger logger = Logger.getLogger("Minecraft");
    public volatile boolean field_973_b;
    public MinecraftServer mcServer;
    private ServerSocket serverSocket;
    private Thread networkAcceptThread;
    private int field_977_f = 0;
    private ArrayList<NetLoginHandler> pendingConnections = new ArrayList<>();
    private ArrayList<NetServerHandler> playerList = new ArrayList<>();

    public NetworkListenThread(MinecraftServer var1, InetAddress var2, int var3) throws IOException {
        this.mcServer = var1;
        this.serverSocket = new ServerSocket(var3, 0, var2);
        this.serverSocket.setPerformancePreferences(0, 2, 1);
        this.field_973_b = true;
        this.networkAcceptThread = new NetworkAcceptThread(this, "Listen thread", var1);
        this.networkAcceptThread.start();
    }

    static ServerSocket func_713_a(NetworkListenThread var0) {
        return var0.serverSocket;
    }

    static int func_712_b(NetworkListenThread var0) {
        return var0.field_977_f++;
    }

    static void func_716_a(NetworkListenThread var0, NetLoginHandler var1) {
        var0.addPendingConnection(var1);
    }

    public void addPlayer(NetServerHandler var1) {
        this.playerList.add(var1);
    }

    private void addPendingConnection(NetLoginHandler loginHandler) {
        if (loginHandler == null) {
            throw new IllegalArgumentException("Got null pending connection!");
        }

        this.pendingConnections.add(loginHandler);
    }

    public void handleNetworkListenThread() {
        for (int i = 0; i < this.pendingConnections.size(); ++i) {
            NetLoginHandler loginHandler = this.pendingConnections.get(i);

            try {
                loginHandler.tryLogin();
            } catch (Exception e) {
                loginHandler.kickUser("Internal server error");
                logger.log(Level.WARNING, "Failed to handle packet: " + e, e);
            }

            if (loginHandler.finishedProcessing) {
                this.pendingConnections.remove(i--);
            }

            loginHandler.netManager.interrupt();
        }

        for (int i = 0; i < this.playerList.size(); ++i) {
            NetServerHandler serverHandler = this.playerList.get(i);

            try {
                serverHandler.handlePackets();
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to handle packet: " + e, e);
                serverHandler.kickPlayer("Internal server error");
            }

            if (serverHandler.connectionClosed) {
                this.playerList.remove(i--);
            }

            serverHandler.netManager.interrupt();
        }

    }
}
