package net.potion.server.gui;

import net.potion.server.PotionServer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

final class ServerWindowAdapter extends WindowAdapter {
    // $FF: synthetic field
    final PotionServer server;

    ServerWindowAdapter(PotionServer server) {
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
