package net.minecraft.server.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ServerGuiCommandListener implements ActionListener {
    // $FF: synthetic field
    final JTextField textField;
    // $FF: synthetic field
    final ServerGUI serverGui;

    ServerGuiCommandListener(ServerGUI serverGUI, JTextField textField) {
        this.serverGui = serverGUI;
        this.textField = textField;
    }

    @Override
    public void actionPerformed(ActionEvent var1) {
        String var2 = this.textField.getText().trim();
        if (var2.length() > 0) {
            ServerGUI.getServer(this.serverGui).addCommand(var2, this.serverGui);
        }

        this.textField.setText("");
    }
}
