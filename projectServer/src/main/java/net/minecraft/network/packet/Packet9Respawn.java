package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

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

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.handleRespawnPacket(this);
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.field_28045_a = inputStream.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeByte(this.field_28045_a);
    }

    @Override
    public int getPacketSize() {
        return 1;
    }
}
