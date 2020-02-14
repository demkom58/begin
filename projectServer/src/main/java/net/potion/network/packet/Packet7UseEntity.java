package net.potion.network.packet;

import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet7UseEntity extends Packet {
    public int playerEntityId;
    public int targetEntity;
    public int isLeftClick;

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.playerEntityId = inputStream.readInt();
        this.targetEntity = inputStream.readInt();
        this.isLeftClick = inputStream.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.playerEntityId);
        outputStream.writeInt(this.targetEntity);
        outputStream.writeByte(this.isLeftClick);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.func_6006_a(this);
    }

    @Override
    public int getPacketSize() {
        return 9;
    }
}
