package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet9Respawn extends Packet {
    public byte field_28045_a;

    public Packet9Respawn() {
    }

    public Packet9Respawn(byte var1) {
        this.field_28045_a = var1;
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handleRespawnPacket(this);
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.field_28045_a = inputStream.readByte();
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeByte(this.field_28045_a);
    }

    public int getPacketSize() {
        return 1;
    }
}
