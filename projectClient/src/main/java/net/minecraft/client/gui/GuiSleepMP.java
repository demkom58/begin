package net.minecraft.client.gui;

import net.minecraft.entity.player.EntityClientPlayerMP;
import net.minecraft.network.NetClientHandler;
import net.minecraft.network.packet.Packet19EntityAction;
import net.minecraft.util.StringTranslate;

public class GuiSleepMP extends GuiChat {
    public void initGui() {
        mc.keyboard.setRepeatingEvents(true);
        StringTranslate translate = StringTranslate.getInstance();
        this.buttons.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, translate.translateKey("multiplayer.stopSleeping")));
    }

    public void onGuiClosed() {
        mc.keyboard.setRepeatingEvents(false);
    }

    protected void keyTyped(char ch, int keycode) {
        if (keycode == 1) {
            this.func_22115_j();
        } else if (keycode == 28) {
            String var3 = this.message.trim();
            if (var3.length() > 0) {
                this.mc.thePlayer.sendChatMessage(this.message.trim());
            }

            this.message = "";
        } else {
            super.keyTyped(ch, keycode);
        }

    }

    public void drawScreen(int var1, int var2, float var3) {
        super.drawScreen(var1, var2, var3);
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            this.func_22115_j();
        } else {
            super.actionPerformed(button);
        }

    }

    private void func_22115_j() {
        if (this.mc.thePlayer instanceof EntityClientPlayerMP) {
            NetClientHandler clientHandler = ((EntityClientPlayerMP) this.mc.thePlayer).sendQueue;
            clientHandler.addToSendQueue(new Packet19EntityAction(this.mc.thePlayer, 3));
        }

    }
}
