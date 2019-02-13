package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet14BlockDig extends Packet {
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int face;
    public int status;

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.status = inputStream.read();
        this.xPosition = inputStream.readInt();
        this.yPosition = inputStream.read();
        this.zPosition = inputStream.readInt();
        this.face = inputStream.read();
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.write(this.status);
        outputStream.writeInt(this.xPosition);
        outputStream.write(this.yPosition);
        outputStream.writeInt(this.zPosition);
        outputStream.write(this.face);
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handleBlockDig(this);
    }

    public int getPacketSize() {
        return 11;
    }
}
