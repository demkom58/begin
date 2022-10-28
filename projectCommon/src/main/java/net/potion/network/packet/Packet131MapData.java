package net.potion.network.packet;

import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet131MapData extends Packet {
    public short field1;
    public short field2;
    public byte[] field3;

    public Packet131MapData() {
        this.isChunkDataPacket = true;
    }

    public Packet131MapData(short var1, short var2, byte[] var3) {
        this.isChunkDataPacket = true;
        this.field1 = var1;
        this.field2 = var2;
        this.field3 = var3;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.field1 = var1.readShort();
        this.field2 = var1.readShort();
        this.field3 = new byte[var1.readByte() & 255];
        var1.readFully(this.field3);
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeShort(this.field1);
        var1.writeShort(this.field2);
        var1.writeByte(this.field3.length);
        var1.write(this.field3);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleMapData(this);
    }

    @Override
    public int getPacketSize() {
        return 4 + this.field3.length;
    }
}
