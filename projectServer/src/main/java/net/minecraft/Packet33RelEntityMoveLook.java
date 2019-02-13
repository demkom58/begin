package net.minecraft;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet33RelEntityMoveLook extends Packet30Entity {
    public Packet33RelEntityMoveLook() {
        this.rotating = true;
    }

    public Packet33RelEntityMoveLook(int var1, byte var2, byte var3, byte var4, byte var5, byte var6) {
        super(var1);
        this.xPosition = var2;
        this.yPosition = var3;
        this.zPosition = var4;
        this.yaw = var5;
        this.pitch = var6;
        this.rotating = true;
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        super.readPacketData(inputStream);
        this.xPosition = inputStream.readByte();
        this.yPosition = inputStream.readByte();
        this.zPosition = inputStream.readByte();
        this.yaw = inputStream.readByte();
        this.pitch = inputStream.readByte();
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        super.writePacketData(outputStream);
        outputStream.writeByte(this.xPosition);
        outputStream.writeByte(this.yPosition);
        outputStream.writeByte(this.zPosition);
        outputStream.writeByte(this.yaw);
        outputStream.writeByte(this.pitch);
    }

    public int getPacketSize() {
        return 9;
    }
}
