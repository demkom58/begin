package net.minecraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.client.gui.PanelCrashReport;
import net.minecraft.util.UnexpectedThrowable;

import java.awt.*;

public class MinecraftAppletImpl extends Minecraft {
    // $FF: synthetic field
    final MinecraftApplet mainFrame;

    public MinecraftAppletImpl(MinecraftApplet var1, Component var2, Canvas var3, MinecraftApplet var4, int var5, int var6, boolean var7) {
        super(var2, var3, var4, var5, var6, var7);
        this.mainFrame = var1;
    }

    public void displayUnexpectedThrowable(UnexpectedThrowable throwable) {
        this.mainFrame.removeAll();
        this.mainFrame.setLayout(new BorderLayout());
        this.mainFrame.add(new PanelCrashReport(throwable), "Center");
        this.mainFrame.validate();
    }
}
