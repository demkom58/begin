package net.minecraft.client.gui;

import net.minecraft.entity.player.EntityClientPlayerMP;
import net.minecraft.network.NetClientHandler;
import net.minecraft.network.packet.Packet19EntityAction;
import net.minecraft.util.StringTranslate;
import org.lwjgl.glfw.GLFW;

public class GuiSleepMP extends GuiChat {
    public void initGui() {
        mc.keyboard.setRepeatingEvents(true);
        StringTranslate translate = StringTranslate.getInstance();
        this.buttons.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, translate.translateKey("multiplayer.stopSleeping")));
    }

    public void onGuiClosed() {
        mc.keyboard.setRepeatingEvents(false);
    }

    @Override
    public void keyTyped(int keycode, int scancode, int action, int mods) {
    }

    @Override
    public void charTyped(char ch, int key) {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            this.func_22115_j();
        } else if (key == GLFW.GLFW_KEY_ENTER) {
            String var3 = this.message.trim();
            if (var3.length() > 0) {
                this.mc.thePlayer.sendChatMessage(this.message.trim());
            }

            this.message = "";
        } else {
            super.charTyped(ch, key);
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
