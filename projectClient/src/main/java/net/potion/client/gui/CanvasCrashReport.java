package net.potion.client.gui;

import java.awt.*;

class CanvasCrashReport extends Canvas {
    public CanvasCrashReport(int size) {
        this.setPreferredSize(new Dimension(size, size));
        this.setMinimumSize(new Dimension(size, size));
    }
}
