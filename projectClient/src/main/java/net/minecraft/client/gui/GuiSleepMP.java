package net.minecraft.client.gui;

import net.minecraft.entity.player.EntityClientPlayerMP;
import net.minecraft.network.NetClientHandler;
import net.minecraft.network.packet.Packet19EntityAction;
import net.minecraft.util.StringTranslate;
import org.lwjgl.glfw.GLFW;

public class GuiSleepMP extends GuiChat {
    @Override
    public void initGui() {
        StringTranslate translate = StringTranslate.getInstance();
        this.buttons.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, translate.translateKey("multiplayer.stopSleeping")));
    }

    @Override
    public void onGuiClosed() { }

    @Override
    public void keyTyped(int keycode, int scancode, int action, int mods) {
        if (keycode == GLFW.GLFW_KEY_ESCAPE) {
            this.func_22115_j();
        } else if (keycode == GLFW.GLFW_KEY_ENTER) {
            String msg = this.message.trim();
            if (msg.length() > 0)
                this.client.thePlayer.sendChatMessage(this.message.trim());
            this.message = "";
        }
    }

    @Override
    public void charTyped(char ch, int key) {
        if (key != GLFW.GLFW_KEY_ESCAPE && key != GLFW.GLFW_KEY_ENTER) {
            super.charTyped(ch, key);
        }
    }

    @Override
    public void drawScreen(int var1, int var2, float partialTicks) {
        super.drawScreen(var1, var2, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            this.func_22115_j();
        } else {
            super.actionPerformed(button);
        }

    }

    private void func_22115_j() {
        if (this.client.thePlayer instanceof EntityClientPlayerMP) {
            NetClientHandler clientHandler = ((EntityClientPlayerMP) this.client.thePlayer).sendQueue;
            clientHandler.addToSendQueue(new Packet19EntityAction(this.client.thePlayer, 3));
        }

    }
}
