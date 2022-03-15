package net.potion.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet4UpdateTime extends Packet {
    public long time;

    public Packet4UpdateTime() {
    }

    @Side(CodeSide.SERVER)
    public Packet4UpdateTime(long time) {
        this.time = time;
    }

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
