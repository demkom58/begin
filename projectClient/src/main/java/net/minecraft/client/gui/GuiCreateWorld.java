package net.minecraft.client.gui;

import net.minecraft.entity.player.PlayerControllerSP;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.util.MathHelper;

import java.util.Random;

public class GuiCreateWorld extends GuiScreen {
    private GuiScreen field_22131_a;
    private GuiTextField textboxWorldName;
    private GuiTextField textboxSeed;
    private String folderName;
    private boolean createClicked;

    public GuiCreateWorld(GuiScreen var1) {
        this.field_22131_a = var1;
    }

    public static String generateUnusedFolderName(ISaveFormat saveFormat, String name) {
        while (saveFormat.readWorldInfo(name) != null) {
            name = name + "-";
        }

        return name;
    }

    public void updateScreen() {
        this.textboxWorldName.updateCursorCounter();
        this.textboxSeed.updateCursorCounter();
    }

    public void initGui() {
        StringTranslate translate = StringTranslate.getInstance();
        mc.keyboard.setRepeatingEvents(true);
        this.buttons.clear();
        this.buttons.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, translate.translateKey("selectWorld.create")));
        this.buttons.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, translate.translateKey("gui.cancel")));
        this.textboxWorldName = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 60, 200, 20, translate.translateKey("selectWorld.newWorld"));
        this.textboxWorldName.isFocused = true;
        this.textboxWorldName.setMaxStringLength(32);
        this.textboxSeed = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 116, 200, 20, "");
        this.func_22129_j();
    }

    private void func_22129_j() {
        this.folderName = this.textboxWorldName.getText().trim();

        for (char ch : ChatAllowedCharacters.ALLOWED_CHARACTERS_ARRAY) {
            this.folderName = this.folderName.replace(ch, '_');
        }

        if (MathHelper.stringNullOrLengthZero(this.folderName)) {
            this.folderName = "World";
        }

        this.folderName = generateUnusedFolderName(this.mc.getSaveLoader(), this.folderName);
    }

    public void onGuiClosed() {
        mc.keyboard.setRepeatingEvents(false);
    }

    protected void actionPerformed(GuiButton button) {
        if (!button.enabled)
            return;

        if (button.id == 1) {
            this.mc.displayGuiScreen(this.field_22131_a);
        } else if (button.id == 0) {
            this.mc.displayGuiScreen(null);
            if (this.createClicked)
                return;

            this.createClicked = true;
            long correctSeed = new Random().nextLong();
            String seedText = this.textboxSeed.getText();
            if (!MathHelper.stringNullOrLengthZero(seedText)) {
                try {
                    long seed = Long.parseLong(seedText);
                    if (seed != 0L) {
                        correctSeed = seed;
                    }
                } catch (NumberFormatException e) {
                    correctSeed = seedText.hashCode();
                }
            }

            this.mc.playerController = new PlayerControllerSP(this.mc);
            this.mc.startWorld(this.folderName, this.textboxWorldName.getText(), correctSeed);
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public void charTyped(char ch, int key) {
        if (this.textboxWorldName.isFocused) {
            this.textboxWorldName.textboxKeyTyped(ch, key);
        } else {
            this.textboxSeed.textboxKeyTyped(ch, key);
        }

        if (ch == '\r') {
            this.actionPerformed(this.buttons.get(0));
        }

        this.buttons.get(0).enabled = this.textboxWorldName.getText().length() > 0;
        this.func_22129_j();
    }

    @Override
    public void keyTyped(int keycode, int scancode, int action, int mods) {
        super.keyTyped(keycode, scancode, action, mods);
    }

    protected void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
        this.textboxWorldName.mouseClicked(x, y, button);
        this.textboxSeed.mouseClicked(x, y, button);
    }

    public void drawScreen(int var1, int var2, float var3) {
        StringTranslate translate = StringTranslate.getInstance();
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, translate.translateKey("selectWorld.create"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
        this.drawString(this.fontRenderer, translate.translateKey("selectWorld.enterName"), this.width / 2 - 100, 47, 10526880);
        this.drawString(this.fontRenderer, translate.translateKey("selectWorld.resultFolder") + " " + this.folderName, this.width / 2 - 100, 85, 10526880);
        this.drawString(this.fontRenderer, translate.translateKey("selectWorld.enterSeed"), this.width / 2 - 100, 104, 10526880);
        this.drawString(this.fontRenderer, translate.translateKey("selectWorld.seedInfo"), this.width / 2 - 100, 140, 10526880);
        this.textboxWorldName.drawTextBox();
        this.textboxSeed.drawTextBox();
        super.drawScreen(var1, var2, var3);
    }

    public void selectNextField() {
        if (this.textboxWorldName.isFocused) {
            this.textboxWorldName.setFocused(false);
            this.textboxSeed.setFocused(true);
        } else {
            this.textboxWorldName.setFocused(true);
            this.textboxSeed.setFocused(false);
        }
    }

}
