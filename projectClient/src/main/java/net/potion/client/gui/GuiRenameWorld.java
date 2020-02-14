package net.potion.client.gui;

import net.potion.util.StringTranslate;
import net.potion.world.WorldInfo;
import net.potion.world.storage.ISaveFormat;

public class GuiRenameWorld extends GuiScreen {
    private final String worldName;
    private GuiScreen guiScreen;
    private GuiTextField renameField;

    public GuiRenameWorld(GuiScreen guiScreen, String worldName) {
        this.guiScreen = guiScreen;
        this.worldName = worldName;
    }

    @Override
    public void updateScreen() {
        this.renameField.updateCursorCounter();
    }

    @Override
    public void initGui() {
        StringTranslate translate = StringTranslate.getInstance();
        this.buttons.clear();
        this.buttons.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, translate.translateKey("selectWorld.renameButton")));
        this.buttons.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, translate.translateKey("gui.cancel")));

        ISaveFormat saveLoader = this.potion.getSaveLoader();
        WorldInfo worldInfo = saveLoader.readWorldInfo(this.worldName);
        String worldName = worldInfo.getWorldName();
        this.renameField = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 60, 200, 20, worldName);
        this.renameField.isFocused = true;
        this.renameField.setMaxStringLength(32);
    }

    @Override
    public void onGuiClosed() { }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (!button.enabled)
            return;

        if (button.id == 1) {
            this.potion.displayGuiScreen(this.guiScreen);
        } else if (button.id == 0) {
            ISaveFormat var2 = this.potion.getSaveLoader();
            var2.setLevelName(this.worldName, this.renameField.getText().trim());
            this.potion.displayGuiScreen(this.guiScreen);
        }
    }

    @Override
    public void charTyped(char ch, int key) {
        this.renameField.charTyped(ch, key);
        this.buttons.get(0).enabled = this.renameField.getText().trim().length() > 0;

        if (ch == '\r')
            this.actionPerformed(this.buttons.get(0));
    }

    @Override
    public void keyTyped(int keycode, int scancode, int action, int mods) {
        this.renameField.keyTyped(keycode, scancode, action, mods);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
        this.renameField.mouseClicked(x, y, button);
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        StringTranslate translate = StringTranslate.getInstance();
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, translate.translateKey("selectWorld.renameTitle"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
        this.drawString(this.fontRenderer, translate.translateKey("selectWorld.enterName"), this.width / 2 - 100, 47, 10526880);
        this.renameField.drawTextBox();
        super.drawScreen(var1, var2, var3);
    }
}
