package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet101CloseWindow extends Packet {
    public int windowId;

    public Packet101CloseWindow() {
    }

    public Packet101CloseWindow(int var1) {
        this.windowId = var1;
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handleCraftingGuiClosedPacked(this);
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.windowId = inputStream.readByte();
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeByte(this.windowId);
    }

    public int getPacketSize() {
        return 1;
    }
}
