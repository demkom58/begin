package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet70Bed extends Packet {
    public static final String[] MESSAGES = new String[]{"tile.bed.notValid", null, null};
    public int field1;

    public Packet70Bed() {
    }

    public Packet70Bed(int var1) {
        this.field1 = var1;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.field1 = var1.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeByte(this.field1);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleBed(this);
    }

    @Override
    public int getPacketSize() {
        return 1;
    }
}
