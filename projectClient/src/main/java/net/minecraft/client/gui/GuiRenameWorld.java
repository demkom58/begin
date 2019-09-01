package net.minecraft.client.gui;

import net.minecraft.util.StringTranslate;
import net.minecraft.world.WorldInfo;
import net.minecraft.world.storage.ISaveFormat;
import org.lwjgl.input.Keyboard;

public class GuiRenameWorld extends GuiScreen {
    private final String field_22113_i;
    private GuiScreen field_22112_a;
    private GuiTextField field_22114_h;

    public GuiRenameWorld(GuiScreen var1, String var2) {
        this.field_22112_a = var1;
        this.field_22113_i = var2;
    }

    public void updateScreen() {
        this.field_22114_h.updateCursorCounter();
    }

    public void initGui() {
        StringTranslate var1 = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.buttons.clear();
        this.buttons.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("selectWorld.renameButton")));
        this.buttons.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
        ISaveFormat var2 = this.mc.getSaveLoader();
        WorldInfo var3 = var2.readWorldInfo(this.field_22113_i);
        String var4 = var3.getWorldName();
        this.field_22114_h = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 60, 200, 20, var4);
        this.field_22114_h.isFocused = true;
        this.field_22114_h.setMaxStringLength(32);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 1) {
                this.mc.displayGuiScreen(this.field_22112_a);
            } else if (button.id == 0) {
                ISaveFormat var2 = this.mc.getSaveLoader();
                var2.setLevelName(this.field_22113_i, this.field_22114_h.getText().trim());
                this.mc.displayGuiScreen(this.field_22112_a);
            }

        }
    }

    protected void keyTyped(char var1, int var2) {
        this.field_22114_h.textboxKeyTyped(var1, var2);
        ((GuiButton) this.buttons.get(0)).enabled = this.field_22114_h.getText().trim().length() > 0;
        if (var1 == '\r') {
            this.actionPerformed((GuiButton) this.buttons.get(0));
        }

    }

    protected void mouseClicked(int x, int y, int var3) {
        super.mouseClicked(x, y, var3);
        this.field_22114_h.mouseClicked(x, y, var3);
    }

    public void drawScreen(int var1, int var2, float var3) {
        StringTranslate var4 = StringTranslate.getInstance();
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, var4.translateKey("selectWorld.renameTitle"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
        this.drawString(this.fontRenderer, var4.translateKey("selectWorld.enterName"), this.width / 2 - 100, 47, 10526880);
        this.field_22114_h.drawTextBox();
        super.drawScreen(var1, var2, var3);
    }
}
