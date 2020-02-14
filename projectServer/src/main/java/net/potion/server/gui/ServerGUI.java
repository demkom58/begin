package net.potion.server.gui;

import net.potion.server.PotionServer;
import net.potion.server.ICommandListener;
import net.potion.entity.player.PlayerListBox;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.logging.Logger;

public class ServerGUI extends JComponent implements ICommandListener {
    public static Logger logger = Logger.getLogger("Potion");
    private PotionServer server;

    public ServerGUI(PotionServer server) {
        this.server = server;
        this.setPreferredSize(new Dimension(854, 480));
        this.setLayout(new BorderLayout());

        try {
            this.add(this.getLogComponent(), "Center");
            this.add(this.getStatsComponent(), "West");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void initGui(PotionServer var0) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

        }

        ServerGUI var1 = new ServerGUI(var0);
        JFrame var2 = new JFrame("Potion server");
        var2.add(var1);
        var2.pack();
        var2.setLocationRelativeTo(null);
        var2.setVisible(true);
        var2.addWindowListener(new ServerWindowAdapter(var0));
    }

    // $FF: synthetic method
    static PotionServer getPotionServer(ServerGUI var0) {
        return var0.server;
    }

    private JComponent getStatsComponent() {
        JPanel var1 = new JPanel(new BorderLayout());
        var1.add(new GuiStatsComponent(), "North");
        var1.add(this.getPlayerListComponent(), "Center");
        var1.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
        return var1;
    }

    private JComponent getPlayerListComponent() {
        PlayerListBox var1 = new PlayerListBox(this.server);
        JScrollPane var2 = new JScrollPane(var1, 22, 30);
        var2.setBorder(new TitledBorder(new EtchedBorder(), "Players"));
        return var2;
    }

    private JComponent getLogComponent() {
        JPanel var1 = new JPanel(new BorderLayout());
        JTextArea var2 = new JTextArea();
        logger.addHandler(new GuiLogOutputHandler(var2));
        JScrollPane var3 = new JScrollPane(var2, 22, 30);
        var2.setEditable(false);
        JTextField var4 = new JTextField();
        var4.addActionListener(new ServerGuiCommandListener(this, var4));
        var2.addFocusListener(new ServerGuiFocusAdapter(this));
        var1.add(var3, "Center");
        var1.add(var4, "South");
        var1.setBorder(new TitledBorder(new EtchedBorder(), "Log and chat"));
        return var1;
    }

    @Override
    public void log(String var1) {
        logger.info(var1);
    }

    @Override
    public String getUsername() {
        return "CONSOLE";
    }
}
