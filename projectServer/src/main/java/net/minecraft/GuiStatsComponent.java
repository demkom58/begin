package net.minecraft;

import net.minecraft.server.MinecraftServer;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class GuiStatsComponent extends JComponent {
    private final DecimalFormat format;
    private int[] memoryUse = new int[256];
    private int updateCounter = 0;
    private String[] displayStrings = new String[10];

    public GuiStatsComponent() {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        format = new DecimalFormat(".##", formatSymbols);

        this.setPreferredSize(new Dimension(256, 196));
        this.setMinimumSize(new Dimension(256, 196));
        this.setMaximumSize(new Dimension(256, 196));
        new Timer(500, new GuiStatsListener(this)).start();
        this.setBackground(Color.BLACK);
    }

    // $FF: synthetic method
    static void update(GuiStatsComponent var0) {
        var0.updateStats();
    }

    private void updateStats() {
        final MinecraftServer server = MinecraftServer.SERVER;

        long var1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.gc();
        this.displayStrings[0] = "Memory use: " + var1 / 1024L / 1024L + " mb (" + Runtime.getRuntime().freeMemory() * 100L / Runtime.getRuntime().maxMemory() + "% free)";
        this.displayStrings[1] = "Threads: " + NetworkManager.numReadThreads + " readers and " + NetworkManager.numWriteThreads + " writers";

        String tps1 = format.format(server.tps1.getAverage());
        String tps5 = format.format(server.tps5.getAverage());
        String tps15 = format.format(server.tps15.getAverage());
        this.displayStrings[2] = "TPS 15m, 5m, 1m: " + tps15 + ", " + tps5 + ", " + tps1;

        this.memoryUse[this.updateCounter++ & 255] = (int) (var1 * 100L / Runtime.getRuntime().maxMemory());
        this.repaint();
    }

    public void paint(Graphics var1) {
        var1.setColor(new Color(16777215));
        var1.fillRect(0, 0, 256, 192);

        for (int var2 = 0; var2 < 256; ++var2) {
            int var3 = this.memoryUse[var2 + this.updateCounter & 255];
            var1.setColor(new Color(var3 + 28 << 16));
            var1.fillRect(var2, 100 - var3, 1, var3);
        }

        var1.setColor(Color.BLACK);

        for (int var4 = 0; var4 < this.displayStrings.length; ++var4) {
            String var5 = this.displayStrings[var4];
            if (var5 != null) {
                var1.drawString(var5, 32, 116 + var4 * 16);
            }
        }

    }
}
