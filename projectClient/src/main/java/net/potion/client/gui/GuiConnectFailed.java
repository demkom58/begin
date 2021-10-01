package net.potion.client.gui;

import net.potion.util.StringTranslate;

public class GuiConnectFailed extends GuiScreen {
    private String errorMessage;
    private String errorDetail;

    public GuiConnectFailed(String var1, String var2, Object... var3) {
        StringTranslate var4 = StringTranslate.getInstance();
        this.errorMessage = var4.translateKey(var1);
        if (var3 != null) {
            this.errorDetail = var4.translateKeyFormat(var2, var3);
        } else {
            this.errorDetail = var4.translateKey(var2);
        }

    }

    @Override
    public void updateScreen() {
    }

    @Override
    public void keyTyped(int keycode, int scancode, int action, int mods) {
    }

    @Override
    public void charTyped(char ch, int key) {
    }

    @Override
    public void initGui() {
        StringTranslate translate = StringTranslate.getInstance();
        this.buttons.clear();
        this.buttons.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, translate.translateKey("gui.toMenu")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.potion.displayGuiScreen(new GuiMainMenu());
        }

    }

    @Override
    public void drawScreen(int var1, int var2, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.errorMessage, this.width / 2, this.height / 2 - 50, 16777215);
        this.drawCenteredString(this.fontRenderer, this.errorDetail, this.width / 2, this.height / 2 - 10, 16777215);
        super.drawScreen(var1, var2, partialTicks);
    }
}
