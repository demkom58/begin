package net.minecraft;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet31RelEntityMove extends Packet30Entity {
    public Packet31RelEntityMove() {
    }

    public Packet31RelEntityMove(int var1, byte var2, byte var3, byte var4) {
        super(var1);
        this.xPosition = var2;
        this.yPosition = var3;
        this.zPosition = var4;
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        super.readPacketData(inputStream);
        this.xPosition = inputStream.readByte();
        this.yPosition = inputStream.readByte();
        this.zPosition = inputStream.readByte();
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        super.writePacketData(outputStream);
        outputStream.writeByte(this.xPosition);
        outputStream.writeByte(this.yPosition);
        outputStream.writeByte(this.zPosition);
    }

    public int getPacketSize() {
        return 7;
    }
}
