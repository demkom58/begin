package net.minecraft.server.gui;

import net.minecraft.server.MinecraftServer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

final class ServerWindowAdapter extends WindowAdapter {
    // $FF: synthetic field
    final MinecraftServer server;

    ServerWindowAdapter(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void windowClosing(WindowEvent var1) {
        this.server.initiateShutdown();

        while (!this.server.serverStopped) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }
}
