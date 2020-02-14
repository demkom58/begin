package net.potion.network.packet;

import net.potion.entity.Entity;
import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet18Animation extends Packet {
    public int entityId;
    public int animate;

    public Packet18Animation() {
    }

    public Packet18Animation(Entity var1, int var2) {
        this.entityId = var1.entityId;
        this.animate = var2;
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.entityId = inputStream.readInt();
        this.animate = inputStream.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.entityId);
        outputStream.writeByte(this.animate);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.handleArmAnimation(this);
    }

    @Override
    public int getPacketSize() {
        return 5;
    }
}
