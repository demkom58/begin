package net.minecraft.client.gui;

import net.minecraft.util.StringTranslate;
import net.minecraft.world.WorldInfo;
import net.minecraft.world.storage.ISaveFormat;

public class GuiRenameWorld extends GuiScreen {
    private final String worldName;
    private GuiScreen guiScreen;
    private GuiTextField renameField;

    public GuiRenameWorld(GuiScreen guiScreen, String worldName) {
        this.guiScreen = guiScreen;
        this.worldName = worldName;
    }

    public void updateScreen() {
        this.renameField.updateCursorCounter();
    }

    public void initGui() {
        StringTranslate translate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.buttons.clear();
        this.buttons.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, translate.translateKey("selectWorld.renameButton")));
        this.buttons.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, translate.translateKey("gui.cancel")));

        ISaveFormat saveLoader = this.mc.getSaveLoader();
        WorldInfo worldInfo = saveLoader.readWorldInfo(this.worldName);
        String worldName = worldInfo.getWorldName();
        this.renameField = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 60, 200, 20, worldName);
        this.renameField.isFocused = true;
        this.renameField.setMaxStringLength(32);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton button) {
        if (!button.enabled)
            return;

        if (button.id == 1) {
            this.mc.displayGuiScreen(this.guiScreen);
        } else if (button.id == 0) {
            ISaveFormat var2 = this.mc.getSaveLoader();
            var2.setLevelName(this.worldName, this.renameField.getText().trim());
            this.mc.displayGuiScreen(this.guiScreen);
        }
    }

    protected void keyTyped(char ch, int key) {
        this.renameField.textboxKeyTyped(ch, key);
        this.buttons.get(0).enabled = this.renameField.getText().trim().length() > 0;
        if (ch == '\r') {
            this.actionPerformed(this.buttons.get(0));
        }

    }

    protected void mouseClicked(int x, int y, int var3) {
        super.mouseClicked(x, y, var3);
        this.renameField.mouseClicked(x, y, var3);
    }

    public void drawScreen(int var1, int var2, float var3) {
        StringTranslate translate = StringTranslate.getInstance();
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, translate.translateKey("selectWorld.renameTitle"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
        this.drawString(this.fontRenderer, translate.translateKey("selectWorld.enterName"), this.width / 2 - 100, 47, 10526880);
        this.renameField.drawTextBox();
        super.drawScreen(var1, var2, var3);
    }
}
