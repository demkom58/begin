package net.minecraft;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet106Transaction extends Packet {
    public int windowId;
    public short shortWindowId;
    public boolean field_20035_c;

    public Packet106Transaction() {
    }

    public Packet106Transaction(int var1, short var2, boolean var3) {
        this.windowId = var1;
        this.shortWindowId = var2;
        this.field_20035_c = var3;
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.func_20008_a(this);
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.windowId = inputStream.readByte();
        this.shortWindowId = inputStream.readShort();
        this.field_20035_c = inputStream.readByte() != 0;
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeByte(this.windowId);
        outputStream.writeShort(this.shortWindowId);
        outputStream.writeByte(this.field_20035_c ? 1 : 0);
    }

    public int getPacketSize() {
        return 4;
    }
}
