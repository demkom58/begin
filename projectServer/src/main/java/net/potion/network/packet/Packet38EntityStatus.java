package net.potion.network.packet;

import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet38EntityStatus extends Packet {
    public int entityId;
    public byte entityStatus;

    public Packet38EntityStatus() {
    }

    public Packet38EntityStatus(int var1, byte var2) {
        this.entityId = var1;
        this.entityStatus = var2;
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.entityId = inputStream.readInt();
        this.entityStatus = inputStream.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.entityId);
        outputStream.writeByte(this.entityStatus);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.func_9001_a(this);
    }

    @Override
    public int getPacketSize() {
        return 5;
    }
}
