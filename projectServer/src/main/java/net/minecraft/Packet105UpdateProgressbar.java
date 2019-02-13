package net.minecraft;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet105UpdateProgressbar extends Packet {
    public int windowId;
    public int progressBar;
    public int progressBarValue;

    public Packet105UpdateProgressbar() {
    }

    public Packet105UpdateProgressbar(int var1, int var2, int var3) {
        this.windowId = var1;
        this.progressBar = var2;
        this.progressBarValue = var3;
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.func_20002_a(this);
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.windowId = inputStream.readByte();
        this.progressBar = inputStream.readShort();
        this.progressBarValue = inputStream.readShort();
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeByte(this.windowId);
        outputStream.writeShort(this.progressBar);
        outputStream.writeShort(this.progressBarValue);
    }

    public int getPacketSize() {
        return 5;
    }
}
