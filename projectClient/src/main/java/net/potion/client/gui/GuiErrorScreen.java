package net.potion.client.gui;

public class GuiErrorScreen extends GuiScreen {
    private int field_28098_a = 0;

    @Override
    public void updateScreen() {
        ++this.field_28098_a;
    }

    @Override
    public void initGui() {
    }

    @Override
    protected void actionPerformed(GuiButton button) {
    }

    @Override
    public void keyTyped(int keycode, int scancode, int action, int mods) {
    }

    @Override
    public void charTyped(char ch, int key) {
    }

    @Override
    public void drawScreen(int var1, int var2, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Out of memory!", this.width / 2, this.height / 4 - 60 + 20, 16777215);
        this.drawString(this.fontRenderer, "Potion has run out of memory.", this.width / 2 - 140, this.height / 4 - 60 + 60, 10526880);
        this.drawString(this.fontRenderer, "This could be caused by a bug in the game or by the", this.width / 2 - 140, this.height / 4 - 60 + 60 + 18, 10526880);
        this.drawString(this.fontRenderer, "Java Virtual Machine not being allocated enough", this.width / 2 - 140, this.height / 4 - 60 + 60 + 27, 10526880);
        this.drawString(this.fontRenderer, "memory. If you are playing in a web browser, try", this.width / 2 - 140, this.height / 4 - 60 + 60 + 36, 10526880);
        this.drawString(this.fontRenderer, "downloading the game and playing it offline.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 45, 10526880);
        this.drawString(this.fontRenderer, "To prevent level corruption, the current game has quit.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 63, 10526880);
        this.drawString(this.fontRenderer, "Please restart the game.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 81, 10526880);
        super.drawScreen(var1, var2, partialTicks);
    }
}
