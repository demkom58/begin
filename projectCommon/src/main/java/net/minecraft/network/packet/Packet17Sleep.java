package net.minecraft.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet17Sleep extends Packet {
    public int entityId;
    public int x;
    public int y;
    public int z;
    public int field1;

    public Packet17Sleep() {
    }

    @Side(CodeSide.SERVER)
    public Packet17Sleep(Entity var1, int var2, int var3, int var4, int var5) {
        this.field1 = var2;
        this.x = var3;
        this.y = var4;
        this.z = var5;
        this.entityId = var1.entityId;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.entityId = var1.readInt();
        this.field1 = var1.readByte();
        this.x = var1.readInt();
        this.y = var1.readByte();
        this.z = var1.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.entityId);
        var1.writeByte(this.field1);
        var1.writeInt(this.x);
        var1.writeByte(this.y);
        var1.writeInt(this.z);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleSleep(this);
    }

    @Override
    public int getPacketSize() {
        return 14;
    }
}
