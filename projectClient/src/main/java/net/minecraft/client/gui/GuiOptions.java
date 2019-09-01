package net.minecraft.client.gui;

import net.minecraft.client.GameSettings;
import net.minecraft.util.StringTranslate;

public class GuiOptions extends GuiScreen {
    private static EnumOptions[] field_22135_k = new EnumOptions[]{EnumOptions.MUSIC, EnumOptions.SOUND, EnumOptions.INVERT_MOUSE, EnumOptions.SENSITIVITY, EnumOptions.DIFFICULTY};
    protected String screenTitle = "Options";
    private GuiScreen parentScreen;
    private GameSettings options;

    public GuiOptions(GuiScreen var1, GameSettings var2) {
        this.parentScreen = var1;
        this.options = var2;
    }

    public void initGui() {
        StringTranslate var1 = StringTranslate.getInstance();
        this.screenTitle = var1.translateKey("options.title");
        int var2 = 0;

        for (EnumOptions var6 : field_22135_k) {
            if (!var6.getEnumFloat()) {
                this.buttons.add(new GuiSmallButton(var6.returnEnumOrdinal(), this.width / 2 - 155 + var2 % 2 * 160, this.height / 6 + 24 * (var2 >> 1), var6, this.options.getKeyBinding(var6)));
            } else {
                this.buttons.add(new GuiSlider(var6.returnEnumOrdinal(), this.width / 2 - 155 + var2 % 2 * 160, this.height / 6 + 24 * (var2 >> 1), var6, this.options.getKeyBinding(var6), this.options.getOptionFloatValue(var6)));
            }

            ++var2;
        }

        this.buttons.add(new GuiButton(101, this.width / 2 - 100, this.height / 6 + 96 + 12, var1.translateKey("options.video")));
        this.buttons.add(new GuiButton(100, this.width / 2 - 100, this.height / 6 + 120 + 12, var1.translateKey("options.controls")));
        this.buttons.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, var1.translateKey("gui.done")));
    }

    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id < 100 && button instanceof GuiSmallButton) {
                this.options.setOptionValue(((GuiSmallButton) button).returnEnumOptions(), 1);
                button.displayString = this.options.getKeyBinding(EnumOptions.getEnumOptions(button.id));
            }

            if (button.id == 101) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiVideoSettings(this, this.options));
            }

            if (button.id == 100) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiControls(this, this.options));
            }

            if (button.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentScreen);
            }

        }
    }

    public void drawScreen(int var1, int var2, float var3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
        super.drawScreen(var1, var2, var3);
    }
}
