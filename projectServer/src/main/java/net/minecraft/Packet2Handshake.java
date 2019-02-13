package net.minecraft;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet2Handshake extends Packet {
    public String username;

    public Packet2Handshake() {
    }

    public Packet2Handshake(String var1) {
        this.username = var1;
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.username = readString(inputStream, 32);
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        writeString(this.username, outputStream);
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handleHandshake(this);
    }

    public int getPacketSize() {
        return 4 + this.username.length() + 4;
    }
}
