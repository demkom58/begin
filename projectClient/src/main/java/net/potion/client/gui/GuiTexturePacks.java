package net.potion.client.gui;

import net.potion.client.PotionClient;
import net.potion.util.StringTranslate;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GuiTexturePacks extends GuiScreen {
    protected GuiScreen guiScreen;
    private int field_6454_o = -1;
    private String fileLocation = "";
    private GuiTexturePackSlot guiTexturePackSlot;

    public GuiTexturePacks(GuiScreen guiScreen) {
        this.guiScreen = guiScreen;
    }

    @Override
    public void initGui() {
        StringTranslate var1 = StringTranslate.getInstance();
        this.buttons.add(new GuiSmallButton(5, this.width / 2 - 154, this.height - 48, var1.translateKey("texturePack.openFolder")));
        this.buttons.add(new GuiSmallButton(6, this.width / 2 + 4, this.height - 48, var1.translateKey("gui.done")));
        this.potion.texturePackList.updateAvaliableTexturePacks();
        this.fileLocation = new File(PotionClient.getPotionDir(), "texturepacks").getAbsolutePath();
        this.guiTexturePackSlot = new GuiTexturePackSlot(this);
        this.guiTexturePackSlot.registerScrollButtons(this.buttons, 7, 8);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (!button.enabled)
            return;

        if (button.id == 5) {
            try {
                Desktop.getDesktop().open(new File(this.fileLocation));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (button.id == 6) {
            this.potion.renderEngine.refreshTextures();
            this.potion.displayGuiScreen(this.guiScreen);
        } else {
            this.guiTexturePackSlot.actionPerformed(button);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
    }

    @Override
    protected void mouseMovedOrUp(int x, int y, int button) {
        super.mouseMovedOrUp(x, y, button);
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        this.guiTexturePackSlot.drawScreen(var1, var2, var3);
        if (this.field_6454_o <= 0) {
            this.potion.texturePackList.updateAvaliableTexturePacks();
            this.field_6454_o += 20;
        }

        StringTranslate var4 = StringTranslate.getInstance();
        this.drawCenteredString(this.fontRenderer, var4.translateKey("texturePack.title"), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.fontRenderer, var4.translateKey("texturePack.folderInfo"), this.width / 2 - 77, this.height - 26, 8421504);
        super.drawScreen(var1, var2, var3);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        --this.field_6454_o;
    }
}
