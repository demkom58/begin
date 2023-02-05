package net.minecraft.client.gui;

import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.glfw.GLFW;

public class GuiChat extends GuiScreen {
    protected String message = "";
    private int updateCounter = 0;

    @Override
    public void initGui() { }

    @Override
    public void onGuiClosed() { }

    @Override
    public void updateScreen() {
        ++this.updateCounter;
    }

    @Override
    public void charTyped(char ch, int key) {
        if (updateCounter == 0)
            return;

        if (ChatAllowedCharacters.ALLOWED_CHARACTERS.indexOf(ch) >= 0 && this.message.length() < 100) {
            this.message = this.message + ch;
        }
    }

    @Override
    public void keyTyped(int keycode, int scancode, int action, int mods) {
        if (action == GLFW.GLFW_RELEASE)
            return;

        if (keycode == GLFW.GLFW_KEY_ESCAPE) {
            this.client.displayGuiScreen(null);
            return;
        }

        if (keycode == GLFW.GLFW_KEY_ENTER) {
            String trim = this.message.trim();
            if (trim.length() > 0) {
                String msg = this.message.trim();
                if (!this.client.lineIsCommand(msg)) {
                    this.client.thePlayer.sendChatMessage(msg);
                }
            }

            this.client.displayGuiScreen(null);
            return;
        }

        if (keycode == GLFW.GLFW_KEY_BACKSPACE && this.message.length() > 0) {
            this.message = this.message.substring(0, this.message.length() - 1);
        }
    }

    @Override
    public void drawScreen(int var1, int var2, float partialTicks) {
        this.drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        this.drawString(this.fontRenderer, "> " + this.message + (this.updateCounter / 6 % 2 == 0 ? "_" : ""), 4, this.height - 12, 14737632);
        super.drawScreen(var1, var2, partialTicks);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        if (button != 0)
            return;

        if (this.client.ingameGUI.field_933_a != null) {
            if (this.message.length() > 0 && !this.message.endsWith(" ")) {
                this.message = this.message + " ";
            }

            this.message = this.message + this.client.ingameGUI.field_933_a;
            byte var4 = 100;
            if (this.message.length() > var4) {
                this.message = this.message.substring(0, var4);
            }
        } else {
            super.mouseClicked(x, y, button);
        }

    }
}
