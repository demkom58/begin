package net.potion.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet32EntityLook extends Packet30Entity {
    public Packet32EntityLook() {
        this.rotating = true;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        super.readPacketData(var1);
        this.yaw = var1.readByte();
        this.pitch = var1.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        super.writePacketData(var1);
        var1.writeByte(this.yaw);
        var1.writeByte(this.pitch);
    }

    @Override
    public int getPacketSize() {
        return 6;
    }
}
