package net.potion.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.network.NetHandler;

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

    @Side(CodeSide.CLIENT)
    public Packet1Login(String var1, int var2) {
        this.username = var1;
        this.protocolVersion = var2;
    }

    @Side(CodeSide.SERVER)
    public Packet1Login(String var1, int var2, long var3, byte var5) {
        this.username = var1;
        this.protocolVersion = var2;
        this.mapSeed = var3;
        this.dimension = var5;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.protocolVersion = var1.readInt();
        this.username = readString(var1, 16);
        this.mapSeed = var1.readLong();
        this.dimension = var1.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.protocolVersion);
        writeString(this.username, var1);
        var1.writeLong(this.mapSeed);
        var1.writeByte(this.dimension);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleLogin(this);
    }

    @Override
    public int getPacketSize() {
        return 4 + this.username.length() + 4 + 5;
    }
}
