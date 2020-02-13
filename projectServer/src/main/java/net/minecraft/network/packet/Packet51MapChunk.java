package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;
import net.minecraft.world.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Packet51MapChunk extends Packet {
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int xSize;
    public int ySize;
    public int zSize;
    public byte[] chunk;
    private int chunkSize;

    public Packet51MapChunk() {
        this.isChunkDataPacket = true;
    }

    public Packet51MapChunk(int x, int y, int z, int xSize, int ySize, int zSize, World world) {
        this.isChunkDataPacket = true;
        this.xPosition = x;
        this.yPosition = y;
        this.zPosition = z;
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        byte[] data = world.getChunkData(x, y, z, xSize, ySize, zSize);
        Deflater deflater = new Deflater(-1);

        try {
            deflater.setInput(data);
            deflater.finish();
            this.chunk = new byte[xSize * ySize * zSize * 5 / 2];
            this.chunkSize = deflater.deflate(this.chunk);
        } finally {
            deflater.end();
        }

    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.xPosition = inputStream.readInt();
        this.yPosition = inputStream.readShort();
        this.zPosition = inputStream.readInt();
        this.xSize = inputStream.read() + 1;
        this.ySize = inputStream.read() + 1;
        this.zSize = inputStream.read() + 1;
        this.chunkSize = inputStream.readInt();
        byte[] data = new byte[this.chunkSize];
        inputStream.readFully(data);
        this.chunk = new byte[this.xSize * this.ySize * this.zSize * 5 / 2];
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        try {
            inflater.inflate(this.chunk);
        } catch (DataFormatException e) {
            throw new IOException("Bad compressed data format");
        } finally {
            inflater.end();
        }

    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.xPosition);
        outputStream.writeShort(this.yPosition);
        outputStream.writeInt(this.zPosition);
        outputStream.write(this.xSize - 1);
        outputStream.write(this.ySize - 1);
        outputStream.write(this.zSize - 1);
        outputStream.writeInt(this.chunkSize);
        outputStream.write(this.chunk, 0, this.chunkSize);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.handleMapChunk(this);
    }

    @Override
    public int getPacketSize() {
        return 17 + this.chunkSize;
    }
}
