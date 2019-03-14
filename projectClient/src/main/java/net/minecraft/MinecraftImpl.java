package net.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;

import java.awt.*;

public final class MinecraftImpl extends Minecraft {
    // $FF: synthetic field
    final Frame mcFrame;

    public MinecraftImpl(Component var1, Canvas var2, MinecraftApplet var3, int var4, int var5, boolean var6, Frame var7) {
        super(var1, var2, var3, var4, var5, var6);
        this.mcFrame = var7;
    }

    public void displayUnexpectedThrowable(UnexpectedThrowable throwable) {
        this.mcFrame.removeAll();
        this.mcFrame.add(new PanelCrashReport(throwable), "Center");
        this.mcFrame.validate();
    }
}
