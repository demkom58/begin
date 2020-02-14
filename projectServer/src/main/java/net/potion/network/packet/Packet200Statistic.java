package net.potion.network.packet;

import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet200Statistic extends Packet {
    public int field_27041_a;
    public int field_27040_b;

    public Packet200Statistic() {
    }

    public Packet200Statistic(int var1, int var2) {
        this.field_27041_a = var1;
        this.field_27040_b = var2;
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.func_27001_a(this);
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.field_27041_a = inputStream.readInt();
        this.field_27040_b = inputStream.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.field_27041_a);
        outputStream.writeByte(this.field_27040_b);
    }

    @Override
    public int getPacketSize() {
        return 6;
    }
}
