package net.minecraft.client.gui;

import net.minecraft.entity.player.EntityClientPlayerMP;
import net.minecraft.network.NetClientHandler;
import net.minecraft.network.packet.Packet19EntityAction;
import net.minecraft.util.StringTranslate;
import org.lwjgl.input.Keyboard;

public class GuiSleepMP extends GuiChat {
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        StringTranslate var1 = StringTranslate.getInstance();
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, var1.translateKey("multiplayer.stopSleeping")));
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void keyTyped(char ch, int key) {
        if (key == 1) {
            this.func_22115_j();
        } else if (key == 28) {
            String var3 = this.message.trim();
            if (var3.length() > 0) {
                this.mc.thePlayer.sendChatMessage(this.message.trim());
            }

            this.message = "";
        } else {
            super.keyTyped(ch, key);
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
            NetClientHandler var1 = ((EntityClientPlayerMP) this.mc.thePlayer).sendQueue;
            var1.addToSendQueue(new Packet19EntityAction(this.mc.thePlayer, 3));
        }

    }
}
