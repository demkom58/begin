package net.minecraft.client.gui;

import net.minecraft.client.GameSettings;
import net.minecraft.util.StringTranslate;

public class GuiControls extends GuiScreen {
    protected String screenTitle = "Controls";
    private GuiScreen parentScreen;
    private GameSettings options;
    private int buttonId = -1;

    public GuiControls(GuiScreen parentScreen, GameSettings options) {
        this.parentScreen = parentScreen;
        this.options = options;
    }

    private int func_20080_j() {
        return this.width / 2 - 155;
    }

    public void initGui() {
        StringTranslate translate = StringTranslate.getInstance();
        int var2 = this.func_20080_j();

        for (int i = 0; i < this.options.keyBindings.length; ++i) {
            this.buttons.add(new GuiSmallButton(i, var2 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 70, 20, this.options.getOptionDisplayString(i)));
        }

        this.buttons.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, translate.translateKey("gui.done")));
        this.screenTitle = translate.translateKey("controls.title");
    }

    protected void actionPerformed(GuiButton button) {
        for (int i = 0; i < this.options.keyBindings.length; ++i) {
            this.buttons.get(i).displayString = this.options.getOptionDisplayString(i);
        }

        if (button.id == 200) {
            this.mc.displayGuiScreen(this.parentScreen);
        } else {
            this.buttonId = button.id;
            button.displayString = "> " + this.options.getOptionDisplayString(button.id) + " <";
        }

    }

    public void keyTyped(int keycode, int scancode, int action, int mods) {
        if (this.buttonId >= 0) {
            this.options.setKeyBinding(this.buttonId, keycode, scancode);
            this.buttons.get(this.buttonId).displayString = this.options.getOptionDisplayString(this.buttonId);
            this.buttonId = -1;
        } else {
            super.keyTyped(keycode, scancode, action, mods);
        }

    }

    public void drawScreen(int var1, int var2, float var3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
        int var4 = this.func_20080_j();

        for (int i = 0; i < this.options.keyBindings.length; ++i) {
            this.drawString(this.fontRenderer, this.options.getKeyBindingDescription(i), var4 + i % 2 * 160 + 70 + 6, this.height / 6 + 24 * (i >> 1) + 7, -1);
        }

        super.drawScreen(var1, var2, var3);
    }
}
