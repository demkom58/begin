package net.potion.network.packet;

import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet39AttachEntity extends Packet {
    public int entityId;
    public int vehicleEntityId;

    @Override
    public int getPacketSize() {
        return 8;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.entityId = var1.readInt();
        this.vehicleEntityId = var1.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.entityId);
        var1.writeInt(this.vehicleEntityId);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.func_6497_a(this);
    }
}
