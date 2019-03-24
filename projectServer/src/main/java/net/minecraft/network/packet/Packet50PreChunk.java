package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet50PreChunk extends Packet {
    public int xPosition;
    public int yPosition;
    public boolean mode;

    public Packet50PreChunk() {
        this.isChunkDataPacket = false;
    }

    public Packet50PreChunk(int x, int y, boolean mode) {
        this.isChunkDataPacket = false;
        this.xPosition = x;
        this.yPosition = y;
        this.mode = mode;
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.xPosition = inputStream.readInt();
        this.yPosition = inputStream.readInt();
        this.mode = inputStream.read() != 0;
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.xPosition);
        outputStream.writeInt(this.yPosition);
        outputStream.write(this.mode ? 1 : 0);
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handlePreChunk(this);
    }

    public int getPacketSize() {
        return 9;
    }
}
