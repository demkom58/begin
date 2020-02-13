package net.minecraft.network.packet;

import net.minecraft.entity.Entity;
import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet17Sleep extends Packet {
    public int field_22041_a;
    public int field_22040_b;
    public int field_22044_c;
    public int field_22043_d;
    public int field_22042_e;

    public Packet17Sleep() {
    }

    public Packet17Sleep(Entity var1, int var2, int var3, int var4, int var5) {
        this.field_22042_e = var2;
        this.field_22040_b = var3;
        this.field_22044_c = var4;
        this.field_22043_d = var5;
        this.field_22041_a = var1.entityId;
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.field_22041_a = inputStream.readInt();
        this.field_22042_e = inputStream.readByte();
        this.field_22040_b = inputStream.readInt();
        this.field_22044_c = inputStream.readByte();
        this.field_22043_d = inputStream.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.field_22041_a);
        outputStream.writeByte(this.field_22042_e);
        outputStream.writeInt(this.field_22040_b);
        outputStream.writeByte(this.field_22044_c);
        outputStream.writeInt(this.field_22043_d);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.func_22002_a(this);
    }

    @Override
    public int getPacketSize() {
        return 14;
    }
}
