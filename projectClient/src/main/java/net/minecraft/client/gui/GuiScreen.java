package net.minecraft.client.gui;

import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

public class GuiScreen extends Gui {
    public int width;
    public int height;
    public boolean field_948_f = false;
    public GuiParticle field_25091_h;
    protected Minecraft mc;
    protected List controlList = new ArrayList();
    protected FontRenderer fontRenderer;
    private GuiButton selectedButton = null;

    public static String getClipboardString() {
        try {
            Transferable var0 = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (var0 != null && var0.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String var1 = (String) var0.getTransferData(DataFlavor.stringFlavor);
                return var1;
            }
        } catch (Exception e) {

        }

        return null;
    }

    public void drawScreen(int var1, int var2, float var3) {
        for (int var4 = 0; var4 < this.controlList.size(); ++var4) {
            GuiButton var5 = (GuiButton) this.controlList.get(var4);
            var5.drawButton(this.mc, var1, var2);
        }

    }

    protected void keyTyped(char var1, int var2) {
        if (var2 == 1) {
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        }

    }

    protected void mouseClicked(int var1, int var2, int var3) {
        if (var3 != 0) {
            return;
        }

        for (int i = 0; i < this.controlList.size(); ++i) {
            GuiButton guiButton = (GuiButton) this.controlList.get(i);
            if (guiButton.mousePressed(this.mc, var1, var2)) {
                this.selectedButton = guiButton;
                this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                this.actionPerformed(guiButton);
            }
        }

    }

    protected void mouseMovedOrUp(int var1, int var2, int var3) {
        if (this.selectedButton != null && var3 == 0) {
            this.selectedButton.mouseReleased(var1, var2);
            this.selectedButton = null;
        }

    }

    protected void actionPerformed(GuiButton button) {
    }

    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        this.field_25091_h = new GuiParticle(mc);
        this.mc = mc;
        this.fontRenderer = mc.fontRenderer;
        this.width = width;
        this.height = height;
        this.controlList.clear();
        this.initGui();
    }

    public void initGui() {
    }

    public void handleInput() {
        while (Mouse.next()) {
            this.handleMouseInput();
        }

        while (Keyboard.next()) {
            this.handleKeyboardInput();
        }

    }

    public void handleMouseInput() {
        if (Mouse.getEventButtonState()) {
            int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
            this.mouseClicked(x, y, Mouse.getEventButton());
        } else {
            int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
            this.mouseMovedOrUp(x, y, Mouse.getEventButton());
        }

    }

    public void handleKeyboardInput() {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == 87) {
                this.mc.toggleFullscreen();
                return;
            }

            this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
        }

    }

    public void updateScreen() {
    }

    public void onGuiClosed() {
    }

    public void drawDefaultBackground() {
        this.drawWorldBackground(0);
    }

    public void drawWorldBackground(int var1) {
        if (this.mc.theWorld != null) {
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        } else {
            this.drawBackground(var1);
        }

    }

    public void drawBackground(int var1) {
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glDisable(2912 /*GL_FOG*/);
        Tessellator var2 = Tessellator.INSTANCE;
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var3 = 32.0F;
        var2.startDrawingQuads();
        var2.setColorOpaque_I(4210752);
        var2.addVertexWithUV(0.0D, (double) this.height, 0.0D, 0.0D, (double) ((float) this.height / var3 + (float) var1));
        var2.addVertexWithUV((double) this.width, (double) this.height, 0.0D, (double) ((float) this.width / var3), (double) ((float) this.height / var3 + (float) var1));
        var2.addVertexWithUV((double) this.width, 0.0D, 0.0D, (double) ((float) this.width / var3), (double) (0 + var1));
        var2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double) (0 + var1));
        var2.draw();
    }

    public boolean doesGuiPauseGame() {
        return true;
    }

    public void deleteWorld(boolean var1, int var2) {
    }

    public void selectNextField() {
    }
}
