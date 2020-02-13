package net.minecraft.server.gui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

class ServerGuiFocusAdapter extends FocusAdapter {
    // $FF: synthetic field
    final ServerGUI mcServerGui;

    ServerGuiFocusAdapter(ServerGUI var1) {
        this.mcServerGui = var1;
    }

    @Override
    public void focusGained(FocusEvent var1) {
    }
}
