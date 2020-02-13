package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet29DestroyEntity extends Packet {
    public int entityId;

    public Packet29DestroyEntity() {
    }

    public Packet29DestroyEntity(int var1) {
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
        netHandler.handleDestroyEntity(this);
    }

    @Override
    public int getPacketSize() {
        return 4;
    }
}
