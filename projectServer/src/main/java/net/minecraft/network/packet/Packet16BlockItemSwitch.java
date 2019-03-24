package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet16BlockItemSwitch extends Packet {
    public int id;

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.id = inputStream.readShort();
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeShort(this.id);
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handleBlockItemSwitch(this);
    }

    public int getPacketSize() {
        return 2;
    }
}
