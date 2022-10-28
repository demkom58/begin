package net.potion.network.packet;

import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet106Transaction extends Packet {
    public int windowId;
    public short shortWindowId;
    public boolean field1;

    public Packet106Transaction() {
    }

    public Packet106Transaction(int var1, short var2, boolean var3) {
        this.windowId = var1;
        this.shortWindowId = var2;
        this.field1 = var3;
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleTransaction(this);
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.windowId = var1.readByte();
        this.shortWindowId = var1.readShort();
        this.field1 = var1.readByte() != 0;
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeByte(this.windowId);
        var1.writeShort(this.shortWindowId);
        var1.writeByte(this.field1 ? 1 : 0);
    }

    @Override
    public int getPacketSize() {
        return 4;
    }
}
