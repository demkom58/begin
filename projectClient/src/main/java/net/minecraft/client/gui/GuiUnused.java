package net.minecraft.client.gui;

public class GuiUnused extends GuiScreen {
    private String message1;
    private String message2;

    public void initGui() {
    }

    public void drawScreen(int var1, int var2, float var3) {
        this.drawGradientRect(0, 0, this.width, this.height, -12574688, -11530224);
        this.drawCenteredString(this.fontRenderer, this.message1, this.width / 2, 90, 16777215);
        this.drawCenteredString(this.fontRenderer, this.message2, this.width / 2, 110, 16777215);
        super.drawScreen(var1, var2, var3);
    }

    @Override
    public void charTyped(char ch, int key) {
    }

    @Override
    public void keyTyped(int keycode, int scancode, int action, int mods) {
    }

}
