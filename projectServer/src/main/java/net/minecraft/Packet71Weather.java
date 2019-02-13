package net.minecraft;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet71Weather extends Packet {
    public int field_27043_a;
    public int field_27042_b;
    public int field_27046_c;
    public int field_27045_d;
    public int field_27044_e;

    public Packet71Weather() {
    }

    public Packet71Weather(Entity var1) {
        this.field_27043_a = var1.entityId;
        this.field_27042_b = MathHelper.floor_double(var1.posX * 32.0D);
        this.field_27046_c = MathHelper.floor_double(var1.posY * 32.0D);
        this.field_27045_d = MathHelper.floor_double(var1.posZ * 32.0D);
        if (var1 instanceof EntityLightningBolt) {
            this.field_27044_e = 1;
        }

    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.field_27043_a = inputStream.readInt();
        this.field_27044_e = inputStream.readByte();
        this.field_27042_b = inputStream.readInt();
        this.field_27046_c = inputStream.readInt();
        this.field_27045_d = inputStream.readInt();
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.field_27043_a);
        outputStream.writeByte(this.field_27044_e);
        outputStream.writeInt(this.field_27042_b);
        outputStream.writeInt(this.field_27046_c);
        outputStream.writeInt(this.field_27045_d);
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.func_27002_a(this);
    }

    public int getPacketSize() {
        return 17;
    }
}
