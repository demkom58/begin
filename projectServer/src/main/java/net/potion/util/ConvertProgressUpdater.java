package net.potion.util;

import net.potion.server.PotionServer;

public class ConvertProgressUpdater implements IProgressUpdatable {
    // $FF: synthetic field
    final PotionServer server;
    private long lastTimeMillis;

    public ConvertProgressUpdater(PotionServer server) {
        this.server = server;
        this.lastTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void display(String var1) {
    }

    @Override
    public void setLoadingProgress(int var1) {
        if (System.currentTimeMillis() - this.lastTimeMillis >= 1000L) {
            this.lastTimeMillis = System.currentTimeMillis();
            PotionServer.LOGGER.info("Converting... " + var1 + "%");
        }

    }

    @Override
    public void displayLoadingString(String var1) {
    }
}
