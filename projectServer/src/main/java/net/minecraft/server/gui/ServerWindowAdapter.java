package net.minecraft.server.gui;

import net.minecraft.server.MinecraftServer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

final class ServerWindowAdapter extends WindowAdapter {
    // $FF: synthetic field
    final MinecraftServer mcServer;

    ServerWindowAdapter(MinecraftServer var1) {
        this.mcServer = var1;
    }

    public void windowClosing(WindowEvent var1) {
        this.mcServer.initiateShutdown();

        while (!this.mcServer.serverStopped) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }
}
