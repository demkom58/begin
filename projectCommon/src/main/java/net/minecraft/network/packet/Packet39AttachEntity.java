package net.minecraft.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet39AttachEntity extends Packet {
    public int entityId;
    public int vehicleEntityId;

    public Packet39AttachEntity() {
    }

    @Side(CodeSide.SERVER)
    public Packet39AttachEntity(Entity var1, Entity var2) {
        this.entityId = var1.entityId;
        this.vehicleEntityId = var2 != null ? var2.entityId : -1;
    }

    @Override
    public int getPacketSize() {
        return 8;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.entityId = var1.readInt();
        this.vehicleEntityId = var1.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.entityId);
        var1.writeInt(this.vehicleEntityId);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleAttachEntity(this);
    }
}
