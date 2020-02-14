package net.potion.client.gui;

import net.potion.client.PotionClient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class GameWindowListener extends WindowAdapter {
    private final PotionClient potion;
    private final Thread gameThread;

    public GameWindowListener(PotionClient potion, Thread gameThread) {
        this.potion = potion;
        this.gameThread = gameThread;
    }

    @Override
    public void windowClosing(WindowEvent event) {
        this.potion.shutdown();

        try {
            this.gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}
