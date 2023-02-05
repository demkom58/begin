package net.minecraft.client.gui;

import net.hypnosis.input.mouse.Mouse;
import net.hypnosis.monitor.Window;
import net.hypnosis.render.Tessellator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.FontRenderer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

public class GuiScreen extends Gui {
    public int width;
    public int height;
    public boolean inputable = false;
    public GuiParticle guiParticle;
    protected MinecraftClient client;
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

    public void drawScreen(int var1, int var2, float partialTicks) {
        for (GuiButton guiButton : this.buttons) {
            guiButton.drawButton(this.client, var1, var2);
        }
    }

    public void charTyped(char ch, int key) {
    }

    public void keyTyped(int keycode, int scancode, int action, int mods) {
        if (action != GLFW.GLFW_PRESS)
            return;

        if (keycode == GLFW.GLFW_KEY_ESCAPE) {
            this.client.displayGuiScreen(null);
            this.client.setIngameFocus();
        }
    }

    protected void mouseClicked(int x, int y, int button) {
        if (button != 0)
            return;

        // foreach CME, on graphic quality change
        for (int i = 0; i < this.buttons.size(); i++) {
            GuiButton guiButton = buttons.get(i);
            if (guiButton.mousePressed(this.client, x, y)) {
                this.selectedButton = guiButton;
                this.client.soundManager.playSoundFX("random.click", 1.0F, 1.0F);
                this.actionPerformed(guiButton);
            }
        }
    }

    protected void mouseMovedOrUp(int x, int y, int button) {
        if (this.selectedButton != null && button == 0) {
            this.selectedButton.mouseReleased(x, y);
            this.selectedButton = null;
        }
    }

    protected void actionPerformed(GuiButton button) {
    }

    public void setWorldAndResolution(MinecraftClient client, int width, int height) {
        this.guiParticle = new GuiParticle(client);
        this.client = client;
        this.fontRenderer = client.fontRenderer;
        this.width = width;
        this.height = height;
        this.buttons.clear();
        this.initGui();
    }

    public void initGui() {
    }

    public void handleMouseInput(long windowPointer, int button, int action, int mods) {
        final Window window = this.client.window;
        final Mouse mouse = this.client.mouse;

        if (action == GLFW.GLFW_PRESS) {
            int x = (int) (mouse.getX() * this.width / window.getWidth());
            int y = (int) (this.height - mouse.getY() * this.height / window.getHeight() - 1);
            this.mouseClicked(x, y, button);
        } else {
            int x = (int) (mouse.getX() * this.width / window.getWidth());
            int y = (int) (this.height - mouse.getY() * this.height / window.getHeight() - 1);
            this.mouseMovedOrUp(x, y, button);
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
        if (this.client.theWorld != null) {
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        } else {
            this.drawBackground(var1);
        }
    }

    public void drawBackground(int var1) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.INSTANCE;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.client.renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var3 = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0x404040);
        tessellator.addVertexWithUV(0.0D, this.height, 0.0D, 0.0D, (float) this.height / var3 + (float) var1);
        tessellator.addVertexWithUV(this.width, this.height, 0.0D, (float) this.width / var3, (float) this.height / var3 + (float) var1);
        tessellator.addVertexWithUV(this.width, 0.0D, 0.0D, (float) this.width / var3, var1);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, var1);
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
