package net.potion.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet32EntityLook extends Packet30Entity {
    public Packet32EntityLook() {
        this.rotating = true;
    }

    public Packet32EntityLook(int var1, byte var2, byte var3) {
        super(var1);
        this.yaw = var2;
        this.pitch = var3;
        this.rotating = true;
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        super.readPacketData(inputStream);
        this.yaw = inputStream.readByte();
        this.pitch = inputStream.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        super.writePacketData(outputStream);
        outputStream.writeByte(this.yaw);
        outputStream.writeByte(this.pitch);
    }

    @Override
    public int getPacketSize() {
        return 6;
    }
}
