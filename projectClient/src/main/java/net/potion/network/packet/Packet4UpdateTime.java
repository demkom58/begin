package net.potion.network.packet;

import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet4UpdateTime extends Packet {
    public long time;

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.time = var1.readLong();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeLong(this.time);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleUpdateTime(this);
    }

    @Override
    public int getPacketSize() {
        return 8;
    }
}
