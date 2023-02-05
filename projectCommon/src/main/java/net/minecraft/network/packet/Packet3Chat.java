package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet3Chat extends Packet {
    public String message;

    public Packet3Chat() {
    }

    public Packet3Chat(String message) {
        if (message.length() > 119) {
            message = message.substring(0, 119);
        }

        this.message = message;
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.message = readString(inputStream, 119);
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        writeString(this.message, outputStream);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleChat(this);
    }

    @Override
    public int getPacketSize() {
        return this.message.length();
    }
}
