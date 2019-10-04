package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet14BlockDig extends Packet {
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int face;
    public int status;

    public Packet14BlockDig() {
    }

    public Packet14BlockDig(int status, int x, int y, int z, int face) {
        this.status = status;
        this.xPosition = x;
        this.yPosition = y;
        this.zPosition = z;
        this.face = face;
    }

    @Override
    public void readPacketData(DataInputStream dis) throws IOException {
        this.status = dis.read();
        this.xPosition = dis.readInt();
        this.yPosition = dis.read();
        this.zPosition = dis.readInt();
        this.face = dis.read();
    }

    @Override
    public void writePacketData(DataOutputStream dos) throws IOException {
        dos.write(this.status);
        dos.writeInt(this.xPosition);
        dos.write(this.yPosition);
        dos.writeInt(this.zPosition);
        dos.write(this.face);
    }

    @Override
    public void processPacket(NetHandler handler) {
        handler.handleBlockDig(this);
    }

    @Override
    public int getPacketSize() {
        return 11;
    }
}
