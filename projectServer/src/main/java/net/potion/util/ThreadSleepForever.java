package net.potion.util;

import net.potion.server.PotionServer;

public class ThreadSleepForever extends Thread {
    // $FF: synthetic field
    final PotionServer server;

    public ThreadSleepForever(PotionServer server) {
        this.server = server;
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2147483647L);
            } catch (InterruptedException e) {
            }
        }
    }
}
