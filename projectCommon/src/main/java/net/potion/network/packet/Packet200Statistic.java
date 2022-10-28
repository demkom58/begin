package net.potion.network.packet;

import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet200Statistic extends Packet {
    public int statId;
    public int value;

    public Packet200Statistic() {
    }

    public Packet200Statistic(int statId, int value) {
        this.statId = statId;
        this.value = value;
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleStatistic(this);
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.statId = var1.readInt();
        this.value = var1.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.statId);
        var1.writeByte(this.value);
    }

    @Override
    public int getPacketSize() {
        return 6;
    }
}
