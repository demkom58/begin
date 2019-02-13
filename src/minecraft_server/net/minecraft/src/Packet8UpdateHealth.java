package net.minecraft.src;

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

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.healthMP = inputStream.readShort();
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeShort(this.healthMP);
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handleHealth(this);
    }

    public int getPacketSize() {
        return 2;
    }
}
