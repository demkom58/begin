package net.minecraft.network;

import net.minecraft.client.gui.GuiConnectFailed;
import net.minecraft.client.gui.GuiConnecting;
import net.minecraft.network.packet.Packet2Handshake;
import net.minecraft.client.Minecraft;

import java.net.ConnectException;
import java.net.UnknownHostException;

public class ThreadConnectToServer extends Thread {
    // $FF: synthetic field
    final Minecraft mc;
    // $FF: synthetic field
    final String hostName;
    // $FF: synthetic field
    final int port;
    // $FF: synthetic field
    final GuiConnecting connectingGui;

    public ThreadConnectToServer(GuiConnecting var1, Minecraft var2, String var3, int var4) {
        this.connectingGui = var1;
        this.mc = var2;
        this.hostName = var3;
        this.port = var4;
    }

    public void run() {
        try {
            GuiConnecting.setNetClientHandler(this.connectingGui, new NetClientHandler(this.mc, this.hostName, this.port));
            if (GuiConnecting.isCancelled(this.connectingGui)) {
                return;
            }

            GuiConnecting.getNetClientHandler(this.connectingGui).addToSendQueue(new Packet2Handshake(this.mc.session.username));
        } catch (UnknownHostException e) {
            if (GuiConnecting.isCancelled(this.connectingGui)) {
                return;
            }

            this.mc.displayGuiScreen(new GuiConnectFailed("connect.failed", "disconnect.genericReason", "Unknown host '" + this.hostName + "'"));
        } catch (ConnectException e) {
            if (GuiConnecting.isCancelled(this.connectingGui)) {
                return;
            }

            this.mc.displayGuiScreen(new GuiConnectFailed("connect.failed", "disconnect.genericReason", e.getMessage()));
        } catch (Exception e) {
            if (GuiConnecting.isCancelled(this.connectingGui)) {
                return;
            }

            e.printStackTrace();
            this.mc.displayGuiScreen(new GuiConnectFailed("connect.failed", "disconnect.genericReason", e.toString()));
        }

    }
}
