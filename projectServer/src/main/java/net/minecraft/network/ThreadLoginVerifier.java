package net.minecraft.network;

import net.minecraft.network.packet.Packet1Login;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

class ThreadLoginVerifier extends Thread {
    // $FF: synthetic field
    final Packet1Login loginPacket;
    // $FF: synthetic field
    final NetLoginHandler loginHandler;

    ThreadLoginVerifier(NetLoginHandler var1, Packet1Login var2) {
        this.loginHandler = var1;
        this.loginPacket = var2;
    }

    public void run() {
        try {
            String var1 = NetLoginHandler.getServerId(this.loginHandler);
            URL var2 = new URL("http://www.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(this.loginPacket.username, StandardCharsets.UTF_8) + "&serverId=" + URLEncoder.encode(var1, StandardCharsets.UTF_8));
            BufferedReader var3 = new BufferedReader(new InputStreamReader(var2.openStream()));
            String var4 = var3.readLine();
            var3.close();
            if (var4.equals("YES")) {
                NetLoginHandler.setLoginPacket(this.loginHandler, this.loginPacket);
            } else {
                this.loginHandler.kickUser("Failed to verify username!");
            }
        } catch (Exception e) {
            this.loginHandler.kickUser("Failed to verify username! [internal error " + e + "]");
            e.printStackTrace();
        }

    }
}
