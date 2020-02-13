package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet4UpdateTime extends Packet {
    public long time;

    public Packet4UpdateTime() {
    }

    public Packet4UpdateTime(long var1) {
        this.time = var1;
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.time = inputStream.readLong();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeLong(this.time);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.handleUpdateTime(this);
    }

    @Override
    public int getPacketSize() {
        return 8;
    }
}
