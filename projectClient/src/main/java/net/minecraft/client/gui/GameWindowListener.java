package net.minecraft.client.gui;

import net.minecraft.client.MinecraftClient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class GameWindowListener extends WindowAdapter {
    private final MinecraftClient client;
    private final Thread gameThread;

    public GameWindowListener(MinecraftClient client, Thread gameThread) {
        this.client = client;
        this.gameThread = gameThread;
    }

    @Override
    public void windowClosing(WindowEvent event) {
        this.client.shutdown();

        try {
            this.gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}
