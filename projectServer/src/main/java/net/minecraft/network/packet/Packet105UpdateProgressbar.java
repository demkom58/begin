package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

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

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.func_20002_a(this);
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.windowId = inputStream.readByte();
        this.progressBar = inputStream.readShort();
        this.progressBarValue = inputStream.readShort();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeByte(this.windowId);
        outputStream.writeShort(this.progressBar);
        outputStream.writeShort(this.progressBarValue);
    }

    @Override
    public int getPacketSize() {
        return 5;
    }
}
