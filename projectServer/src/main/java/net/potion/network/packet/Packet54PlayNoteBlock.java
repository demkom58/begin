package net.potion.network.packet;

import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet54PlayNoteBlock extends Packet {
    public int xLocation;
    public int yLocation;
    public int zLocation;
    public int instrumentType;
    public int pitch;

    public Packet54PlayNoteBlock() {
    }

    public Packet54PlayNoteBlock(int var1, int var2, int var3, int var4, int var5) {
        this.xLocation = var1;
        this.yLocation = var2;
        this.zLocation = var3;
        this.instrumentType = var4;
        this.pitch = var5;
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.xLocation = inputStream.readInt();
        this.yLocation = inputStream.readShort();
        this.zLocation = inputStream.readInt();
        this.instrumentType = inputStream.read();
        this.pitch = inputStream.read();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.xLocation);
        outputStream.writeShort(this.yLocation);
        outputStream.writeInt(this.zLocation);
        outputStream.write(this.instrumentType);
        outputStream.write(this.pitch);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.func_21004_a(this);
    }

    @Override
    public int getPacketSize() {
        return 12;
    }
}
