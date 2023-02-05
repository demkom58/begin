package net.minecraft.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.NetClientHandler;
import net.minecraft.network.packet.Packet2Handshake;
import net.minecraft.util.StringTranslate;

import java.net.ConnectException;
import java.net.UnknownHostException;

public class GuiConnecting extends GuiScreen {
    private NetClientHandler clientHandler;
    private boolean cancelled = false;

    public GuiConnecting(MinecraftClient client, String host, int port) {
        System.out.println("Connecting to " + host + ", " + port);
        client.changeWorld(null);

        new Thread(() -> {
            try {
                clientHandler = new NetClientHandler(client, host, port);
                if (cancelled)
                    return;

                clientHandler.addToSendQueue(new Packet2Handshake(client.session.username));
            } catch (UnknownHostException e) {
                if (cancelled)
                    return;

                this.client.displayGuiScreen(new GuiConnectFailed("connect.failed", "disconnect.genericReason", "Unknown host '" + host + "'"));
            } catch (ConnectException e) {
                if (cancelled)
                    return;

                this.client.displayGuiScreen(new GuiConnectFailed("connect.failed", "disconnect.genericReason", e.getMessage()));
            } catch (Exception e) {
                if (cancelled)
                    return;

                e.printStackTrace();
                this.client.displayGuiScreen(new GuiConnectFailed("connect.failed", "disconnect.genericReason", e.toString()));
            }
        }).start();
    }

    @Override
    public void updateScreen() {
        if (this.clientHandler != null) {
            this.clientHandler.processReadPackets();
        }

    }

    @Override
    public void charTyped(char ch, int key) {
    }

    @Override
    public void keyTyped(int keycode, int scancode, int action, int mods) {
    }

    @Override
    public void initGui() {
        StringTranslate var1 = StringTranslate.getInstance();
        this.buttons.clear();
        this.buttons.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.cancelled = true;
            if (this.clientHandler != null) {
                this.clientHandler.disconnect();
            }

            this.client.displayGuiScreen(new GuiMainMenu());
        }

    }

    @Override
    public void drawScreen(int var1, int var2, float partialTicks) {
        this.drawDefaultBackground();
        StringTranslate var4 = StringTranslate.getInstance();
        if (this.clientHandler == null) {
            this.drawCenteredString(this.fontRenderer, var4.translateKey("connect.connecting"), this.width / 2, this.height / 2 - 50, 16777215);
            this.drawCenteredString(this.fontRenderer, "", this.width / 2, this.height / 2 - 10, 16777215);
        } else {
            this.drawCenteredString(this.fontRenderer, var4.translateKey("connect.authorizing"), this.width / 2, this.height / 2 - 50, 16777215);
            this.drawCenteredString(this.fontRenderer, this.clientHandler.field_1209_a, this.width / 2, this.height / 2 - 10, 16777215);
        }

        super.drawScreen(var1, var2, partialTicks);
    }
}
