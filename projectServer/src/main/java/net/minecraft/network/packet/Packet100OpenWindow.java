package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet100OpenWindow extends Packet {
    public int windowId;
    public int inventoryType;
    public String windowTitle;
    public int slotsCount;

    public Packet100OpenWindow() {
    }

    public Packet100OpenWindow(int var1, int var2, String var3, int var4) {
        this.windowId = var1;
        this.inventoryType = var2;
        this.windowTitle = var3;
        this.slotsCount = var4;
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.func_20004_a(this);
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.windowId = inputStream.readByte();
        this.inventoryType = inputStream.readByte();
        this.windowTitle = inputStream.readUTF();
        this.slotsCount = inputStream.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeByte(this.windowId);
        outputStream.writeByte(this.inventoryType);
        outputStream.writeUTF(this.windowTitle);
        outputStream.writeByte(this.slotsCount);
    }

    @Override
    public int getPacketSize() {
        return 3 + this.windowTitle.length();
    }
}
