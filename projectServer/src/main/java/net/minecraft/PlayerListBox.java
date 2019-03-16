package net.minecraft;

import net.minecraft.server.MinecraftServer;

import javax.swing.*;
import java.util.Vector;

public class PlayerListBox extends JList implements IUpdatePlayerListBox {
    private MinecraftServer mcServer;
    private int updateCounter = 0;

    public PlayerListBox(MinecraftServer mcServer) {
        this.mcServer = mcServer;
        mcServer.addPlayerListBox(this);
    }

    public void update() {
        if (this.updateCounter++ % 20 == 0) {
            Vector<String> vec = new Vector<>();

            for (int i = 0; i < this.mcServer.configManager.playerEntities.size(); ++i) {
                vec.add(this.mcServer.configManager.playerEntities.get(i).username);
            }

            this.setListData(vec);
        }

    }
}
