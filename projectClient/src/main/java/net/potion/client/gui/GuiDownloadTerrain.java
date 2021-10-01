package net.potion.client.gui;

import net.potion.network.NetClientHandler;
import net.potion.network.packet.Packet0KeepAlive;
import net.potion.util.StringTranslate;

public class GuiDownloadTerrain extends GuiScreen {
    private NetClientHandler netHandler;
    private int updateCounter = 0;

    public GuiDownloadTerrain(NetClientHandler var1) {
        this.netHandler = var1;
    }

    @Override
    public void keyTyped(int keycode, int scancode, int action, int mods) {
    }

    @Override
    public void charTyped(char ch, int key) {
    }

    @Override
    public void initGui() {
        this.buttons.clear();
    }

    @Override
    public void updateScreen() {
        ++this.updateCounter;
        if (this.updateCounter % 20 == 0) {
            this.netHandler.addToSendQueue(new Packet0KeepAlive());
        }

        if (this.netHandler != null) {
            this.netHandler.processReadPackets();
        }

    }

    @Override
    protected void actionPerformed(GuiButton button) {
    }

    @Override
    public void drawScreen(int var1, int var2, float partialTicks) {
        this.drawBackground(0);
        StringTranslate translate = StringTranslate.getInstance();
        this.drawCenteredString(this.fontRenderer, translate.translateKey("multiplayer.downloadingTerrain"), this.width / 2, this.height / 2 - 50, 16777215);
        super.drawScreen(var1, var2, partialTicks);
    }
}
