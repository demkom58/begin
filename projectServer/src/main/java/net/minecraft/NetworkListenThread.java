package net.minecraft;

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

    private void addPendingConnection(NetLoginHandler var1) {
        if (var1 == null) {
            throw new IllegalArgumentException("Got null pendingconnection!");
        }

        this.pendingConnections.add(var1);
    }

    public void handleNetworkListenThread() {
        for (int i = 0; i < this.pendingConnections.size(); ++i) {
            NetLoginHandler var2 = this.pendingConnections.get(i);

            try {
                var2.tryLogin();
            } catch (Exception e) {
                var2.kickUser("Internal server error");
                logger.log(Level.WARNING, "Failed to handle packet: " + e, e);
            }

            if (var2.finishedProcessing) {
                this.pendingConnections.remove(i--);
            }

            var2.netManager.func_28138_a();
        }

        for (int i = 0; i < this.playerList.size(); ++i) {
            NetServerHandler var7 = this.playerList.get(i);

            try {
                var7.handlePackets();
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to handle packet: " + e, e);
                var7.kickPlayer("Internal server error");
            }

            if (var7.connectionClosed) {
                this.playerList.remove(i--);
            }

            var7.netManager.func_28138_a();
        }

    }
}
