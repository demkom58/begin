package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet19EntityAction extends Packet {
    public int entityId;
    public int state;

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.entityId = inputStream.readInt();
        this.state = inputStream.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.entityId);
        outputStream.writeByte(this.state);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.func_21001_a(this);
    }

    @Override
    public int getPacketSize() {
        return 5;
    }
}
