package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet10Flying extends Packet {
    public double xPosition;
    public double yPosition;
    public double zPosition;
    public double stance;
    public float yaw;
    public float pitch;
    public boolean onGround;
    public boolean moving;
    public boolean rotating;

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.handleFlying(this);
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.onGround = inputStream.read() != 0;
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.write(this.onGround ? 1 : 0);
    }

    @Override
    public int getPacketSize() {
        return 1;
    }
}
