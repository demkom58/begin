package net.potion.network.packet;

import net.potion.network.NetHandler;
import net.potion.world.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet53BlockChange extends Packet {
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int type;
    public int metadata;

    public Packet53BlockChange() {
        this.isChunkDataPacket = true;
    }

    public Packet53BlockChange(int var1, int var2, int var3, World var4) {
        this.isChunkDataPacket = true;
        this.xPosition = var1;
        this.yPosition = var2;
        this.zPosition = var3;
        this.type = var4.getBlockId(var1, var2, var3);
        this.metadata = var4.getBlockMetadata(var1, var2, var3);
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.xPosition = inputStream.readInt();
        this.yPosition = inputStream.read();
        this.zPosition = inputStream.readInt();
        this.type = inputStream.read();
        this.metadata = inputStream.read();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.xPosition);
        outputStream.write(this.yPosition);
        outputStream.writeInt(this.zPosition);
        outputStream.write(this.type);
        outputStream.write(this.metadata);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.handleBlockChange(this);
    }

    @Override
    public int getPacketSize() {
        return 11;
    }
}
