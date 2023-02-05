package net.minecraft.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet61DoorChange extends Packet {
    public int field1;
    public int field2;
    public int x;
    public int y;
    public int z;

    public Packet61DoorChange() {
    }

    @Side(CodeSide.SERVER)
    public Packet61DoorChange(int var1, int x, int y, int z, int var5) {
        this.field1 = var1;
        this.x = x;
        this.y = y;
        this.z = z;
        this.field2 = var5;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.field1 = var1.readInt();
        this.x = var1.readInt();
        this.y = var1.readByte();
        this.z = var1.readInt();
        this.field2 = var1.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.field1);
        var1.writeInt(this.x);
        var1.writeByte(this.y);
        var1.writeInt(this.z);
        var1.writeInt(this.field2);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleDoorChange(this);
    }

    @Override
    public int getPacketSize() {
        return 20;
    }
}
