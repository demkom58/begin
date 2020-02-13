package net.minecraft.util;

import net.minecraft.server.MinecraftServer;

public class ConvertProgressUpdater implements IProgressUpdatable {
    // $FF: synthetic field
    final MinecraftServer mcServer;
    private long lastTimeMillis;

    public ConvertProgressUpdater(MinecraftServer var1) {
        this.mcServer = var1;
        this.lastTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void display(String var1) {
    }

    @Override
    public void setLoadingProgress(int var1) {
        if (System.currentTimeMillis() - this.lastTimeMillis >= 1000L) {
            this.lastTimeMillis = System.currentTimeMillis();
            MinecraftServer.LOGGER.info("Converting... " + var1 + "%");
        }

    }

    @Override
    public void displayLoadingString(String var1) {
    }
}
