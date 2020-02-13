package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet8UpdateHealth extends Packet {
    public int healthMP;

    public Packet8UpdateHealth() {
    }

    public Packet8UpdateHealth(int var1) {
        this.healthMP = var1;
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.healthMP = inputStream.readShort();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeShort(this.healthMP);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.handleHealth(this);
    }

    @Override
    public int getPacketSize() {
        return 2;
    }
}
