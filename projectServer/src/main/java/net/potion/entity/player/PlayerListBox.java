package net.potion.entity.player;

import net.potion.server.PotionServer;

import javax.swing.*;
import java.util.Vector;

public class PlayerListBox extends JList implements IUpdatePlayerListBox {
    private PotionServer server;
    private int updateCounter = 0;

    public PlayerListBox(PotionServer server) {
        this.server = server;
        server.addPlayerListBox(this);
    }

    @Override
    public void update() {
        if (this.updateCounter++ % 20 == 0) {
            Vector<String> vec = new Vector<>();

            for (int i = 0; i < this.server.configManager.playerEntities.size(); ++i) {
                vec.add(this.server.configManager.playerEntities.get(i).username);
            }

            this.setListData(vec);
        }

    }
}
