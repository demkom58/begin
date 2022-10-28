package net.potion.network.packet;

import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet9Respawn extends Packet {
    public byte dimension;

    public Packet9Respawn() {
    }

    public Packet9Respawn(byte dimension) {
        this.dimension = dimension;
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleRespawnPacket(this);
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.dimension = var1.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeByte(this.dimension);
    }

    @Override
    public int getPacketSize() {
        return 1;
    }
}
