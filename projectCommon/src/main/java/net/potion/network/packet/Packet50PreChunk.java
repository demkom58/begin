package net.potion.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.network.NetHandler;

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

    @Side(CodeSide.SERVER)
    public Packet50PreChunk(int x, int y, boolean mode) {
        this.isChunkDataPacket = false;
        this.xPosition = x;
        this.yPosition = y;
        this.mode = mode;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.xPosition = var1.readInt();
        this.yPosition = var1.readInt();
        this.mode = var1.read() != 0;
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.xPosition);
        var1.writeInt(this.yPosition);
        var1.write(this.mode ? 1 : 0);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handlePreChunk(this);
    }

    @Override
    public int getPacketSize() {
        return 9;
    }
}
