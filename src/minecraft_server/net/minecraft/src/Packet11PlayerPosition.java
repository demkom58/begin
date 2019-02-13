package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet11PlayerPosition extends Packet10Flying {
    public Packet11PlayerPosition() {
        this.moving = true;
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.xPosition = inputStream.readDouble();
        this.yPosition = inputStream.readDouble();
        this.stance = inputStream.readDouble();
        this.zPosition = inputStream.readDouble();
        super.readPacketData(inputStream);
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeDouble(this.xPosition);
        outputStream.writeDouble(this.yPosition);
        outputStream.writeDouble(this.stance);
        outputStream.writeDouble(this.zPosition);
        super.writePacketData(outputStream);
    }

    public int getPacketSize() {
        return 33;
    }
}
