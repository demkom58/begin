package net.minecraft.client.gui;

import net.minecraft.client.render.FontRenderer;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.glfw.GLFW;

public class GuiTextField extends Gui {
    private final FontRenderer fontRenderer;
    private final int xPos;
    private final int yPos;
    private final int width;
    private final int height;
    public boolean isFocused = false;
    public boolean isEnabled = true;
    private String text;
    private int maxStringLength;
    private int cursorCounter;
    private GuiScreen parentGuiScreen;

    public GuiTextField(GuiScreen parentGuiScreen, FontRenderer fontRenderer, int xPos, int yPos, int width, int height, String text) {
        this.parentGuiScreen = parentGuiScreen;
        this.fontRenderer = fontRenderer;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.setText(text);
    }

    public String getText() {
        return this.text;
    }

    public void setText(String var1) {
        this.text = var1;
    }

    public void updateCursorCounter() {
        ++this.cursorCounter;
    }

    public void textboxKeyTyped(char ch, int key) {
        if (!this.isEnabled || !this.isFocused)
            return;

        if (ch == '\t')
            this.parentGuiScreen.selectNextField();

        if (ch == GLFW.GLFW_KEY_U) {
            String var3 = GuiScreen.getClipboardString();
            if (var3 == null) {
                var3 = "";
            }

            int var4 = 32 - this.text.length();
            if (var4 > var3.length()) {
                var4 = var3.length();
            }

            if (var4 > 0) {
                this.text = this.text + var3.substring(0, var4);
            }
        }

        if (key == GLFW.GLFW_KEY_BACKSPACE && this.text.length() > 0)
            this.text = this.text.substring(0, this.text.length() - 1);

        if (ChatAllowedCharacters.ALLOWED_CHARACTERS.indexOf(ch) >= 0
                && (this.text.length() < this.maxStringLength || this.maxStringLength == 0)) {
            this.text = this.text + ch;
        }
    }

    public void mouseClicked(int x, int y, int var3) {
        this.setFocused(this.isEnabled && x >= this.xPos && x < this.xPos + this.width && y >= this.yPos && y < this.yPos + this.height);
    }

    public void setFocused(boolean focused) {
        if (focused && !this.isFocused) {
            this.cursorCounter = 0;
        }

        this.isFocused = focused;
    }

    public void drawTextBox() {
        this.drawRect(this.xPos - 1, this.yPos - 1, this.xPos + this.width + 1, this.yPos + this.height + 1, -6250336);
        this.drawRect(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, -16777216);
        if (this.isEnabled) {
            boolean var1 = this.isFocused && this.cursorCounter / 6 % 2 == 0;
            this.drawString(this.fontRenderer, this.text + (var1 ? "_" : ""), this.xPos + 4, this.yPos + (this.height - 8) / 2, 14737632);
        } else {
            this.drawString(this.fontRenderer, this.text, this.xPos + 4, this.yPos + (this.height - 8) / 2, 7368816);
        }

    }

    public void setMaxStringLength(int var1) {
        this.maxStringLength = var1;
    }
}
