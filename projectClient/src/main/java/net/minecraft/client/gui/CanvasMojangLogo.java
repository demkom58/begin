package net.minecraft.client.gui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

class CanvasMojangLogo extends Canvas {
    private BufferedImage logo;

    public CanvasMojangLogo() {
        try {
            this.logo = ImageIO.read(PanelCrashReport.class.getResource("/gui/logo.png"));
        } catch (IOException ignored) {
        }

        byte size = 100;
        this.setPreferredSize(new Dimension(size, size));
        this.setMinimumSize(new Dimension(size, size));
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.drawImage(this.logo, this.getWidth() / 2 - this.logo.getWidth() / 2, 32, null);
    }
}
