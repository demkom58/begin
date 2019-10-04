package net.minecraft.client.gui;

import net.minecraft.util.UnexpectedThrowable;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PanelCrashReport extends Panel {
    public PanelCrashReport(UnexpectedThrowable unexpectedThrowable) {
        this.setBackground(new Color(3028036));
        this.setLayout(new BorderLayout());
        StringWriter writer = new StringWriter();
        unexpectedThrowable.throwable.printStackTrace(new PrintWriter(writer));
        String var3 = writer.toString();
        String vendor = "";
        String mcInfo = "";

        try {
            mcInfo = mcInfo + "Generated " + (new SimpleDateFormat()).format(new Date()) + "\n";
            mcInfo = mcInfo + "\n";
            mcInfo = mcInfo + "Minecraft: Minecraft Beta 1.7.3\n";
            mcInfo = mcInfo + "OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version") + "\n";
            mcInfo = mcInfo + "Java: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor") + "\n";
            mcInfo = mcInfo + "VM: " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor") + "\n";
            mcInfo = mcInfo + "LWJGL: " + Version.getVersion() + "\n";
            vendor = GL11.glGetString(GL11.GL_VENDOR);
            mcInfo = mcInfo + "OpenGL: " + GL11.glGetString(GL11.GL_RENDER) + " version " + GL11.glGetString(GL11.GL_VERSION) + ", " + GL11.glGetString(GL11.GL_VENDOR) + "\n";
        } catch (Throwable throwable) {
            mcInfo = mcInfo + "[failed to get system properties (" + throwable + ")]\n";
        }

        mcInfo = mcInfo + "\n";
        mcInfo = mcInfo + var3;

        String cardInfo = "";
        cardInfo = cardInfo + "\n";
        cardInfo = cardInfo + "\n";
        if (var3.contains("Pixel format not accelerated")) {
            cardInfo = cardInfo + "      Bad video card drivers!      \n";
            cardInfo = cardInfo + "      -----------------------      \n";
            cardInfo = cardInfo + "\n";
            cardInfo = cardInfo + "Minecraft was unable to start because it failed to find an accelerated OpenGL mode.\n";
            cardInfo = cardInfo + "This can usually be fixed by updating the video card drivers.\n";
            if (vendor.toLowerCase().contains("nvidia")) {
                cardInfo = cardInfo + "\n";
                cardInfo = cardInfo + "You might be able to find drivers for your video card here:\n";
                cardInfo = cardInfo + "  http://www.nvidia.com/\n";
            } else if (vendor.toLowerCase().contains("ati")) {
                cardInfo = cardInfo + "\n";
                cardInfo = cardInfo + "You might be able to find drivers for your video card here:\n";
                cardInfo = cardInfo + "  http://www.amd.com/\n";
            }
        } else {
            cardInfo = cardInfo + "      Minecraft has crashed!      \n";
            cardInfo = cardInfo + "      ----------------------      \n";
            cardInfo = cardInfo + "\n";
            cardInfo = cardInfo + "Minecraft has stopped running because it encountered a problem.\n";
            cardInfo = cardInfo + "\n";
            cardInfo = cardInfo + "If you wish to report this, please copy this entire text and email it to support@mojang.com.\n";
            cardInfo = cardInfo + "Please include a description of what you did when the error occurred.\n";
        }

        cardInfo = cardInfo + "\n";
        cardInfo = cardInfo + "\n";
        cardInfo = cardInfo + "\n";
        cardInfo = cardInfo + "--- BEGIN ERROR REPORT " + Integer.toHexString(cardInfo.hashCode()) + " --------\n";
        cardInfo = cardInfo + mcInfo;
        cardInfo = cardInfo + "--- END ERROR REPORT " + Integer.toHexString(cardInfo.hashCode()) + " ----------\n";
        cardInfo = cardInfo + "\n";
        cardInfo = cardInfo + "\n";

        TextArea area = new TextArea(cardInfo, 0, 0, 1);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        this.add(new CanvasMojangLogo(), "North");
        this.add(new CanvasCrashReport(80), "East");
        this.add(new CanvasCrashReport(80), "West");
        this.add(new CanvasCrashReport(100), "South");
        this.add(area, "Center");
    }
}
