package net.minecraft.client.gui;

import net.hypnosis.monitor.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.Tessellator;
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
    public GuiParticle guiParticle;
    protected Minecraft mc;
    protected List<GuiButton> buttons = new ArrayList<>();
    protected FontRenderer fontRenderer;
    private GuiButton selectedButton = null;

    public static String getClipboardString() {
        try {
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String) transferable.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (Exception ignored) {
        }

        return null;
    }

    public void drawScreen(int var1, int var2, float var3) {
        for (GuiButton guiButton : this.buttons) {
            guiButton.drawButton(this.mc, var1, var2);
        }
    }

    protected void keyTyped(char ch, int key) {
        if (key == 1) {
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        }
    }

    protected void mouseClicked(int x, int y, int var3) {
        if (var3 != 0)
            return;

        // foreach CME, on graphic quality change
        for (int i = 0; i < this.buttons.size(); i++) {
            GuiButton guiButton = buttons.get(i);
            if (guiButton.mousePressed(this.mc, x, y)) {
                this.selectedButton = guiButton;
                this.mc.soundManager.playSoundFX("random.click", 1.0F, 1.0F);
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
        this.guiParticle = new GuiParticle(mc);
        this.mc = mc;
        this.fontRenderer = mc.fontRenderer;
        this.width = width;
        this.height = height;
        this.buttons.clear();
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
        final Window window = this.mc.window;

        if (Mouse.getEventButtonState()) {
            int x = Mouse.getEventX() * this.width / window.getWidth();
            int y = this.height - Mouse.getEventY() * this.height / window.getHeight() - 1;
            this.mouseClicked(x, y, Mouse.getEventButton());
        } else {
            int x = Mouse.getEventX() * this.width / window.getWidth();
            int y = this.height - Mouse.getEventY() * this.height / window.getHeight() - 1;
            this.mouseMovedOrUp(x, y, Mouse.getEventButton());
        }

    }

    public void handleKeyboardInput() {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_F11) {
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
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.INSTANCE;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var3 = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(4210752);
        tessellator.addVertexWithUV(0.0D, this.height, 0.0D, 0.0D, (float) this.height / var3 + (float) var1);
        tessellator.addVertexWithUV(this.width, this.height, 0.0D, (float) this.width / var3, (float) this.height / var3 + (float) var1);
        tessellator.addVertexWithUV(this.width, 0.0D, 0.0D, (float) this.width / var3, 0 + var1);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0 + var1);
        tessellator.draw();
    }

    public boolean doesGuiPauseGame() {
        return true;
    }

    public void deleteWorld(boolean var1, int var2) {
    }

    public void selectNextField() {
    }
}
