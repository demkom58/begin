package net.minecraft.client.gui;

import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

public class GuiChat extends GuiScreen {
    protected String message = "";
    private int updateCounter = 0;

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void updateScreen() {
        ++this.updateCounter;
    }

    protected void keyTyped(char ch, int key) {
        if (key == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(null);
        } else if (key == Keyboard.KEY_RETURN) {
            String trim = this.message.trim();
            if (trim.length() > 0) {
                String msg = this.message.trim();
                if (!this.mc.lineIsCommand(msg)) {
                    this.mc.thePlayer.sendChatMessage(msg);
                }
            }

            this.mc.displayGuiScreen(null);
        } else {
            if (key == Keyboard.KEY_BACK && this.message.length() > 0) {
                this.message = this.message.substring(0, this.message.length() - 1);
            }

            if (ChatAllowedCharacters.ALLOWED_CHARACTERS.indexOf(ch) >= 0 && this.message.length() < 100) {
                this.message = this.message + ch;
            }

        }
    }

    public void drawScreen(int var1, int var2, float var3) {
        this.drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        this.drawString(this.fontRenderer, "> " + this.message + (this.updateCounter / 6 % 2 == 0 ? "_" : ""), 4, this.height - 12, 14737632);
        super.drawScreen(var1, var2, var3);
    }

    protected void mouseClicked(int x, int y, int var3) {
        if (var3 == 0) {
            if (this.mc.ingameGUI.field_933_a != null) {
                if (this.message.length() > 0 && !this.message.endsWith(" ")) {
                    this.message = this.message + " ";
                }

                this.message = this.message + this.mc.ingameGUI.field_933_a;
                byte var4 = 100;
                if (this.message.length() > var4) {
                    this.message = this.message.substring(0, var4);
                }
            } else {
                super.mouseClicked(x, y, var3);
            }
        }

    }
}
