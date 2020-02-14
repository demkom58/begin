package net.potion.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet12PlayerLook extends Packet10Flying {
    public Packet12PlayerLook() {
        this.rotating = true;
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.yaw = inputStream.readFloat();
        this.pitch = inputStream.readFloat();
        super.readPacketData(inputStream);
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeFloat(this.yaw);
        outputStream.writeFloat(this.pitch);
        super.writePacketData(outputStream);
    }

    @Override
    public int getPacketSize() {
        return 9;
    }
}
