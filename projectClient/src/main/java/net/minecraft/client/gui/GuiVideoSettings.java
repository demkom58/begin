package net.minecraft.client.gui;

import net.minecraft.client.GameSettings;
import net.minecraft.client.render.ScaledResolution;
import net.minecraft.util.StringTranslate;

public class GuiVideoSettings extends GuiScreen {
    private static EnumOption[] options = new EnumOption[]{
            EnumOption.GRAPHICS,
            EnumOption.RENDER_DISTANCE,
            EnumOption.AMBIENT_OCCLUSION,
            EnumOption.FRAMERATE_LIMIT,
            EnumOption.ANAGLYPH,
            EnumOption.VIEW_BOBBING,
            EnumOption.GUI_SCALE,
            EnumOption.ADVANCED_OPENGL
    };

    protected String titleText = "Video Settings";
    private GuiScreen guiScreen;
    private GameSettings gameSettings;

    public GuiVideoSettings(GuiScreen guiScreen, GameSettings gameSettings) {
        this.guiScreen = guiScreen;
        this.gameSettings = gameSettings;
    }

    @Override
    public void initGui() {
        StringTranslate translate = StringTranslate.getInstance();
        this.titleText = translate.translateKey("options.videoTitle");
        int var2 = 0;

        for (EnumOption option : options) {
            if (!option.getFloatType()) {
                this.buttons.add(new GuiSmallButton(option.ordinal(), this.width / 2 - 155 + var2 % 2 * 160, this.height / 6 + 24 * (var2 >> 1), option, this.gameSettings.getKeyBinding(option)));
            } else {
                this.buttons.add(new GuiSlider(option.ordinal(), this.width / 2 - 155 + var2 % 2 * 160, this.height / 6 + 24 * (var2 >> 1), option, this.gameSettings.getKeyBinding(option), this.gameSettings.getOptionFloatValue(option)));
            }

            ++var2;
        }

        this.buttons.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, translate.translateKey("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (!button.enabled)
            return;

        if (button.id < 100 && button instanceof GuiSmallButton) {
            this.gameSettings.setOptionValue(((GuiSmallButton) button).returnEnumOptions(), 1);
            button.displayString = this.gameSettings.getKeyBinding(EnumOption.getEnumOptions(button.id));
        }

        if (button.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.guiScreen);
        }

        ScaledResolution var2 = new ScaledResolution(this.mc.gameSettings, this.mc.window.getWidth(), this.mc.window.getHeight());
        int var3 = var2.getScaledWidth();
        int var4 = var2.getScaledHeight();
        this.setWorldAndResolution(this.mc, var3, var4);
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.titleText, this.width / 2, 20, 16777215);
        super.drawScreen(var1, var2, var3);
    }
}
