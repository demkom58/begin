package net.potion.client.gui;

import net.potion.client.GameSettings;
import net.potion.util.StringTranslate;

public class GuiOptions extends GuiScreen {
    private static EnumOption[] options = new EnumOption[]{
            EnumOption.MUSIC, EnumOption.SOUND, EnumOption.INVERT_MOUSE, EnumOption.SENSITIVITY, EnumOption.DIFFICULTY
    };

    protected String screenTitle = "Options";
    private GuiScreen parentScreen;
    private GameSettings gameSettings;

    public GuiOptions(GuiScreen var1, GameSettings var2) {
        this.parentScreen = var1;
        this.gameSettings = var2;
    }

    @Override
    public void initGui() {
        StringTranslate vartranslate = StringTranslate.getInstance();
        this.screenTitle = vartranslate.translateKey("options.title");
        int var2 = 0;

        for (EnumOption option : options) {
            if (!option.getFloatType()) {
                this.buttons.add(new GuiSmallButton(option.ordinal(), this.width / 2 - 155 + var2 % 2 * 160, this.height / 6 + 24 * (var2 >> 1), option, this.gameSettings.getKeyBinding(option)));
            } else {
                this.buttons.add(new GuiSlider(option.ordinal(), this.width / 2 - 155 + var2 % 2 * 160, this.height / 6 + 24 * (var2 >> 1), option, this.gameSettings.getKeyBinding(option), this.gameSettings.getOptionFloatValue(option)));
            }

            ++var2;
        }

        this.buttons.add(new GuiButton(101, this.width / 2 - 100, this.height / 6 + 96 + 12, vartranslate.translateKey("options.video")));
        this.buttons.add(new GuiButton(100, this.width / 2 - 100, this.height / 6 + 120 + 12, vartranslate.translateKey("options.controls")));
        this.buttons.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, vartranslate.translateKey("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (!button.enabled)
            return;

        if (button.id < 100 && button instanceof GuiSmallButton) {
            this.gameSettings.setOptionValue(((GuiSmallButton) button).returnEnumOptions(), 1);
            button.displayString = this.gameSettings.getKeyBinding(EnumOption.getEnumOptions(button.id));
        }

        if (button.id == 101) {
            this.potion.gameSettings.saveOptions();
            this.potion.displayGuiScreen(new GuiVideoSettings(this, this.gameSettings));
        }

        if (button.id == 100) {
            this.potion.gameSettings.saveOptions();
            this.potion.displayGuiScreen(new GuiControls(this, this.gameSettings));
        }

        if (button.id == 200) {
            this.potion.gameSettings.saveOptions();
            this.potion.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void drawScreen(int var1, int var2, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
        super.drawScreen(var1, var2, partialTicks);
    }
}
