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

    @Override
    public void processPacket(NetHandler var1) {
        var1.func_20087_a(this);
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.windowId = var1.readByte();
        this.inventoryType = var1.readByte();
        this.windowTitle = var1.readUTF();
        this.slotsCount = var1.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeByte(this.windowId);
        var1.writeByte(this.inventoryType);
        var1.writeUTF(this.windowTitle);
        var1.writeByte(this.slotsCount);
    }

    @Override
    public int getPacketSize() {
        return 3 + this.windowTitle.length();
    }
}
