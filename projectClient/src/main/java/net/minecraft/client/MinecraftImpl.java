package net.minecraft.client;

import net.minecraft.client.gui.PanelCrashReport;
import net.minecraft.util.UnexpectedThrowable;

import java.awt.*;

public final class MinecraftImpl extends Minecraft {
    private final Frame mcFrame;

    public MinecraftImpl(Component component, Canvas mcCanvas, MinecraftApplet mcApplet, int displayWidth, int displayHeight, boolean fullscreen, Frame frame) {
        super(component, mcCanvas, mcApplet, displayWidth, displayHeight, fullscreen);
        this.mcFrame = frame;
    }

    public void displayUnexpectedThrowable(UnexpectedThrowable throwable) {
        this.mcFrame.removeAll();
        this.mcFrame.add(new PanelCrashReport(throwable), "Center");
        this.mcFrame.validate();
    }
}
