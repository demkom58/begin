package net.potion.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet38EntityStatus extends Packet {
    public int entityId;
    public byte entityStatus;

    public Packet38EntityStatus() {
    }

    @Side(CodeSide.SERVER)
    public Packet38EntityStatus(int var1, byte var2) {
        this.entityId = var1;
        this.entityStatus = var2;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.entityId = var1.readInt();
        this.entityStatus = var1.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.entityId);
        var1.writeByte(this.entityStatus);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleEntityStatus(this);
    }

    @Override
    public int getPacketSize() {
        return 5;
    }
}
