package net.minecraft.isom;

import net.minecraft.util.CanvasIsomPreview;

import java.applet.Applet;
import java.awt.*;

public class IsomPreviewApplet extends Applet {
    private CanvasIsomPreview a = new CanvasIsomPreview();

    public IsomPreviewApplet() {
        this.setLayout(new BorderLayout());
        this.add(this.a, "Center");
    }

    public void start() {
        this.a.func_1272_b();
    }

    public void stop() {
        this.a.exit();
    }
}
