package net.minecraft.network;

import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

class NetworkAcceptThread extends Thread {
    final MinecraftServer mcServer;
    final NetworkListenThread listenThread;

    NetworkAcceptThread(NetworkListenThread listenThread, String name, MinecraftServer mcServer) {
        super(name);
        this.listenThread = listenThread;
        this.mcServer = mcServer;
    }

    @Override
    public void run() {
        Map<InetAddress, Long> map = new HashMap<>();

        while (this.listenThread.field_973_b) {
            try {
                Socket socket = NetworkListenThread.func_713_a(this.listenThread).accept();
                if (socket != null) {
                    InetAddress address = socket.getInetAddress();
                    if (map.containsKey(address) && !"127.0.0.1".equals(address.getHostAddress()) && System.currentTimeMillis() - map.get(address) < 5000L) {
                        map.put(address, System.currentTimeMillis());
                        socket.close();
                    } else {
                        map.put(address, System.currentTimeMillis());
                        NetLoginHandler loginHandler = new NetLoginHandler(this.mcServer, socket, "Connection #" + NetworkListenThread.func_712_b(this.listenThread));
                        NetworkListenThread.func_716_a(this.listenThread, loginHandler);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
