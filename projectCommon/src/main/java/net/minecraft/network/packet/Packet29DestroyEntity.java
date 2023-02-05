package net.minecraft.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet29DestroyEntity extends Packet {
    public int entityId;

    public Packet29DestroyEntity() {
    }

    @Side(CodeSide.SERVER)
    public Packet29DestroyEntity(int var1) {
        this.entityId = var1;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.entityId = var1.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.entityId);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleDestroyEntity(this);
    }

    @Override
    public int getPacketSize() {
        return 4;
    }
}
