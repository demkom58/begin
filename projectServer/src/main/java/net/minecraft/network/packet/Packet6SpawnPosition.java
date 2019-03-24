package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet6SpawnPosition extends Packet {
    public int xPosition;
    public int yPosition;
    public int zPosition;

    public Packet6SpawnPosition() {
    }

    public Packet6SpawnPosition(int var1, int var2, int var3) {
        this.xPosition = var1;
        this.yPosition = var2;
        this.zPosition = var3;
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.xPosition = inputStream.readInt();
        this.yPosition = inputStream.readInt();
        this.zPosition = inputStream.readInt();
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.xPosition);
        outputStream.writeInt(this.yPosition);
        outputStream.writeInt(this.zPosition);
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handleSpawnPosition(this);
    }

    public int getPacketSize() {
        return 12;
    }
}
