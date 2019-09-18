package net.minecraft.client.gui;

import net.minecraft.util.StringTranslate;

public class GuiMultiplayer extends GuiScreen {
    private GuiScreen parentScreen;
    private GuiTextField addressField;

    public GuiMultiplayer(GuiScreen var1) {
        this.parentScreen = var1;
    }

    public void updateScreen() {
        this.addressField.updateCursorCounter();
    }

    public void initGui() {
        StringTranslate translate = StringTranslate.getInstance();
        mc.keyboard.setRepeatingEvents(true);
        this.buttons.clear();
        this.buttons.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, translate.translateKey("multiplayer.connect")));
        this.buttons.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, translate.translateKey("gui.cancel")));
        String var2 = this.mc.gameSettings.lastServer.replaceAll("_", ":");
        this.buttons.get(0).enabled = var2.length() > 0;
        this.addressField = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, this.height / 4 - 10 + 50 + 18, 200, 20, var2);
        this.addressField.isFocused = true;
        this.addressField.setMaxStringLength(128);
    }

    public void onGuiClosed() {
        mc.keyboard.setRepeatingEvents(false);
    }

    protected void actionPerformed(GuiButton button) {
        if (!button.enabled)
            return;

        if (button.id == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
        } else if (button.id == 0) {
            String var2 = this.addressField.getText().trim();
            this.mc.gameSettings.lastServer = var2.replaceAll(":", "_");
            this.mc.gameSettings.saveOptions();
            String[] var3 = var2.split(":");
            if (var2.startsWith("[")) {
                int var4 = var2.indexOf("]");
                if (var4 > 0) {
                    String var5 = var2.substring(1, var4);
                    String var6 = var2.substring(var4 + 1).trim();
                    if (var6.startsWith(":")) {
                        var6 = var6.substring(1);
                        var3 = new String[]{var5, var6};
                    } else {
                        var3 = new String[]{var5};
                    }
                }
            }

            if (var3.length > 2) {
                var3 = new String[]{var2};
            }

            this.mc.displayGuiScreen(new GuiConnecting(this.mc, var3[0], var3.length > 1 ? this.parseIntWithDefault(var3[1], 25565) : 25565));
        }
    }

    private int parseIntWithDefault(String var1, int var2) {
        try {
            return Integer.parseInt(var1.trim());
        } catch (Exception e) {
            return var2;
        }
    }

    protected void keyTyped(char ch, int key) {
        this.addressField.textboxKeyTyped(ch, key);
        if (ch == '\r') {
            this.actionPerformed(this.buttons.get(0));
        }

        this.buttons.get(0).enabled = this.addressField.getText().length() > 0;
    }

    protected void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
        this.addressField.mouseClicked(x, y, button);
    }

    public void drawScreen(int var1, int var2, float var3) {
        StringTranslate translate = StringTranslate.getInstance();
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, translate.translateKey("multiplayer.title"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
        this.drawString(this.fontRenderer, translate.translateKey("multiplayer.info1"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 0, 10526880);
        this.drawString(this.fontRenderer, translate.translateKey("multiplayer.info2"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 9, 10526880);
        this.drawString(this.fontRenderer, translate.translateKey("multiplayer.ipinfo"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 36, 10526880);
        this.addressField.drawTextBox();
        super.drawScreen(var1, var2, var3);
    }
}
