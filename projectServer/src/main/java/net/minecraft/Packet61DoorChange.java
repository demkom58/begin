package net.minecraft;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet61DoorChange extends Packet {
    public int field_28047_a;
    public int field_28046_b;
    public int field_28050_c;
    public int field_28049_d;
    public int field_28048_e;

    public Packet61DoorChange() {
    }

    public Packet61DoorChange(int var1, int var2, int var3, int var4, int var5) {
        this.field_28047_a = var1;
        this.field_28050_c = var2;
        this.field_28049_d = var3;
        this.field_28048_e = var4;
        this.field_28046_b = var5;
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.field_28047_a = inputStream.readInt();
        this.field_28050_c = inputStream.readInt();
        this.field_28049_d = inputStream.readByte();
        this.field_28048_e = inputStream.readInt();
        this.field_28046_b = inputStream.readInt();
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.field_28047_a);
        outputStream.writeInt(this.field_28050_c);
        outputStream.writeByte(this.field_28049_d);
        outputStream.writeInt(this.field_28048_e);
        outputStream.writeInt(this.field_28046_b);
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.func_28002_a(this);
    }

    public int getPacketSize() {
        return 20;
    }
}
