package net.minecraft.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet19EntityAction extends Packet {
    public int entityId;
    public int state;

    public Packet19EntityAction() {
    }

    @Side(CodeSide.CLIENT)
    public Packet19EntityAction(Entity var1, int var2) {
        this.entityId = var1.entityId;
        this.state = var2;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.entityId = var1.readInt();
        this.state = var1.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.entityId);
        var1.writeByte(this.state);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleEntityAction(this);
    }

    @Override
    public int getPacketSize() {
        return 5;
    }
}
