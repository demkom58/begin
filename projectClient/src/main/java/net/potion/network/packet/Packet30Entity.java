package net.potion.network.packet;

import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet30Entity extends Packet {
    public int entityId;
    public byte xPosition;
    public byte yPosition;
    public byte zPosition;
    public byte yaw;
    public byte pitch;
    public boolean rotating = false;

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.entityId = var1.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.entityId);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleEntity(this);
    }

    @Override
    public int getPacketSize() {
        return 4;
    }
}
