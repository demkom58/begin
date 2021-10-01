package net.potion.network.packet;

import net.potion.network.NetHandler;
import net.potion.world.chunk.ChunkPosition;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Packet60Explosion extends Packet {
    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public float explosionSize;
    public Set<ChunkPosition> destroyedBlockPositions;

    public Packet60Explosion() {
    }

    public Packet60Explosion(double var1, double var3, double var5, float var7, Set<ChunkPosition> var8) {
        this.explosionX = var1;
        this.explosionY = var3;
        this.explosionZ = var5;
        this.explosionSize = var7;
        this.destroyedBlockPositions = new HashSet<>(var8);
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.explosionX = inputStream.readDouble();
        this.explosionY = inputStream.readDouble();
        this.explosionZ = inputStream.readDouble();
        this.explosionSize = inputStream.readFloat();
        int var2 = inputStream.readInt();
        this.destroyedBlockPositions = new HashSet();
        int var3 = (int) this.explosionX;
        int var4 = (int) this.explosionY;
        int var5 = (int) this.explosionZ;

        for (int var6 = 0; var6 < var2; ++var6) {
            int var7 = inputStream.readByte() + var3;
            int var8 = inputStream.readByte() + var4;
            int var9 = inputStream.readByte() + var5;
            this.destroyedBlockPositions.add(new ChunkPosition(var7, var8, var9));
        }

    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeDouble(this.explosionX);
        outputStream.writeDouble(this.explosionY);
        outputStream.writeDouble(this.explosionZ);
        outputStream.writeFloat(this.explosionSize);
        outputStream.writeInt(this.destroyedBlockPositions.size());
        int var2 = (int) this.explosionX;
        int var3 = (int) this.explosionY;
        int var4 = (int) this.explosionZ;

        for (ChunkPosition var6 : this.destroyedBlockPositions) {
            int var7 = var6.x - var2;
            int var8 = var6.y - var3;
            int var9 = var6.z - var4;
            outputStream.writeByte(var7);
            outputStream.writeByte(var8);
            outputStream.writeByte(var9);
        }

    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.func_12001_a(this);
    }

    @Override
    public int getPacketSize() {
        return 32 + this.destroyedBlockPositions.size() * 3;
    }
}
