package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class GameWindowListener extends WindowAdapter {
    private final Minecraft mc;
    private final Thread mcThread;

    public GameWindowListener(Minecraft mc, Thread mcThread) {
        this.mc = mc;
        this.mcThread = mcThread;
    }

    @Override
    public void windowClosing(WindowEvent event) {
        this.mc.shutdown();

        try {
            this.mcThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}
