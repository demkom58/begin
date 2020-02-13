package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet16BlockItemSwitch extends Packet {
    public int id;

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.id = inputStream.readShort();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeShort(this.id);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.handleBlockItemSwitch(this);
    }

    @Override
    public int getPacketSize() {
        return 2;
    }
}
