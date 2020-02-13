package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet70Bed extends Packet {
    public static final String[] field_25016_a = new String[]{"tile.bed.notValid", null, null};
    public int field_25015_b;

    public Packet70Bed() {
    }

    public Packet70Bed(int var1) {
        this.field_25015_b = var1;
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.field_25015_b = inputStream.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeByte(this.field_25015_b);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.func_25001_a(this);
    }

    @Override
    public int getPacketSize() {
        return 1;
    }
}
