package net.minecraft.server.gui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

class ServerGuiFocusAdapter extends FocusAdapter {
    // $FF: synthetic field
    final ServerGUI serverGui;

    ServerGuiFocusAdapter(ServerGUI serverGui) {
        this.serverGui = serverGui;
    }

    @Override
    public void focusGained(FocusEvent var1) {
    }
}
