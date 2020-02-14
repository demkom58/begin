package net.potion.network.packet;

import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet14BlockDig extends Packet {
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int face;
    public int status;

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.status = inputStream.read();
        this.xPosition = inputStream.readInt();
        this.yPosition = inputStream.read();
        this.zPosition = inputStream.readInt();
        this.face = inputStream.read();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.write(this.status);
        outputStream.writeInt(this.xPosition);
        outputStream.write(this.yPosition);
        outputStream.writeInt(this.zPosition);
        outputStream.write(this.face);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.handleBlockDig(this);
    }

    @Override
    public int getPacketSize() {
        return 11;
    }
}
