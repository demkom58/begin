package net.potion.util;

import net.potion.client.PotionClient;

public class ThreadSleepForever extends Thread {
    // $FF: synthetic field
    final PotionClient potion;

    public ThreadSleepForever(PotionClient potion, String var2) {
        super(var2);
        this.potion = potion;
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        while (this.potion.running) {
            try {
                Thread.sleep(2147483647L);
            } catch (InterruptedException ignored) {
            }
        }

    }
}
