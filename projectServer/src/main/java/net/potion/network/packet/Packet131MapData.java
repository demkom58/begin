package net.potion.network.packet;

import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet131MapData extends Packet {
    public short field_28052_a;
    public short field_28051_b;
    public byte[] field_28053_c;

    public Packet131MapData() {
        this.isChunkDataPacket = true;
    }

    public Packet131MapData(short var1, short var2, byte[] var3) {
        this.isChunkDataPacket = true;
        this.field_28052_a = var1;
        this.field_28051_b = var2;
        this.field_28053_c = var3;
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.field_28052_a = inputStream.readShort();
        this.field_28051_b = inputStream.readShort();
        this.field_28053_c = new byte[inputStream.readByte() & 255];
        inputStream.readFully(this.field_28053_c);
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeShort(this.field_28052_a);
        outputStream.writeShort(this.field_28051_b);
        outputStream.writeByte(this.field_28053_c.length);
        outputStream.write(this.field_28053_c);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.func_28001_a(this);
    }

    @Override
    public int getPacketSize() {
        return 4 + this.field_28053_c.length;
    }
}
