package net.minecraft.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet13PlayerLookMove extends Packet10Flying {
    public Packet13PlayerLookMove() {
        this.rotating = true;
        this.moving = true;
    }

    public Packet13PlayerLookMove(double var1, double var3, double var5, double var7, float var9, float var10, boolean var11) {
        this.xPosition = var1;
        this.yPosition = var3;
        this.stance = var5;
        this.zPosition = var7;
        this.yaw = var9;
        this.pitch = var10;
        this.onGround = var11;
        this.rotating = true;
        this.moving = true;
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.xPosition = inputStream.readDouble();
        this.yPosition = inputStream.readDouble();
        this.stance = inputStream.readDouble();
        this.zPosition = inputStream.readDouble();
        this.yaw = inputStream.readFloat();
        this.pitch = inputStream.readFloat();
        super.readPacketData(inputStream);
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeDouble(this.xPosition);
        outputStream.writeDouble(this.yPosition);
        outputStream.writeDouble(this.stance);
        outputStream.writeDouble(this.zPosition);
        outputStream.writeFloat(this.yaw);
        outputStream.writeFloat(this.pitch);
        super.writePacketData(outputStream);
    }

    @Override
    public int getPacketSize() {
        return 41;
    }
}
