package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet30Entity extends Packet {
    public int entityId;
    public byte xPosition;
    public byte yPosition;
    public byte zPosition;
    public byte yaw;
    public byte pitch;
    public boolean rotating = false;

    public Packet30Entity() {
    }

    public Packet30Entity(int var1) {
        this.entityId = var1;
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.entityId = inputStream.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.entityId);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.handleEntity(this);
    }

    @Override
    public int getPacketSize() {
        return 4;
    }
}
