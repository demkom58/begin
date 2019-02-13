package net.minecraft;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet130UpdateSign extends Packet {
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public String[] signLines;

    public Packet130UpdateSign() {
        this.isChunkDataPacket = true;
    }

    public Packet130UpdateSign(int var1, int var2, int var3, String[] var4) {
        this.isChunkDataPacket = true;
        this.xPosition = var1;
        this.yPosition = var2;
        this.zPosition = var3;
        this.signLines = var4;
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.xPosition = inputStream.readInt();
        this.yPosition = inputStream.readShort();
        this.zPosition = inputStream.readInt();
        this.signLines = new String[4];

        for (int var2 = 0; var2 < 4; ++var2) {
            this.signLines[var2] = readString(inputStream, 15);
        }

    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.xPosition);
        outputStream.writeShort(this.yPosition);
        outputStream.writeInt(this.zPosition);

        for (int var2 = 0; var2 < 4; ++var2) {
            writeString(this.signLines[var2], outputStream);
        }

    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handleUpdateSign(this);
    }

    public int getPacketSize() {
        int var1 = 0;

        for (int var2 = 0; var2 < 4; ++var2) {
            var1 += this.signLines[var2].length();
        }

        return var1;
    }
}
