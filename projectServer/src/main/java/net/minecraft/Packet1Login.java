package net.minecraft;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet1Login extends Packet {
    public int protocolVersion;
    public String username;
    public long mapSeed;
    public byte dimension;

    public Packet1Login() {
    }

    public Packet1Login(String var1, int var2, long var3, byte var5) {
        this.username = var1;
        this.protocolVersion = var2;
        this.mapSeed = var3;
        this.dimension = var5;
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.protocolVersion = inputStream.readInt();
        this.username = readString(inputStream, 16);
        this.mapSeed = inputStream.readLong();
        this.dimension = inputStream.readByte();
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.protocolVersion);
        writeString(this.username, outputStream);
        outputStream.writeLong(this.mapSeed);
        outputStream.writeByte(this.dimension);
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handleLogin(this);
    }

    public int getPacketSize() {
        return 4 + this.username.length() + 4 + 5;
    }
}
